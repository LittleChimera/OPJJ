package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartHttpServer {
	
	private String address;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String,String> mimeTypes = new HashMap<String, String>();
	private Map<String, IWebWorker> workersMap;
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	final static private String WORKERS_PACKAGE = "hr.fer.zemris.java.webserver.workers";
	public SmartHttpServer(String configFileName) throws IOException {
		Properties serverProperties = new Properties();
		serverProperties.load(new BufferedReader(new FileReader(Paths.get(configFileName).toFile())));
		
		address = loadProperty(serverProperties, "server.address");
		
		final String portProperty = "server.port";
		String portValue = loadProperty(serverProperties, portProperty);
		try {
			port = Integer.parseInt(portValue);			
		} catch (Exception e) {
			throw new RuntimeException("Illegal " + portProperty + " value");
		}
		final int maxPort = 65535;
		if (port < 0 || port > maxPort) {
			throw new RuntimeException("Illegal " + portProperty + " number: " + port);
		}
		
		final String workersProperty = "server.workerThreads";
		String workersValue = loadProperty(serverProperties, workersProperty);
		try {
			workerThreads = Integer.parseInt(workersValue);			
		} catch (Exception e) {
			throw new RuntimeException("Illegal " + workersProperty + " value");
		}
		if (workerThreads <= 0) {
			throw new RuntimeException("Illegal " + workersProperty + " count: " + workerThreads);
		}
		
		final String timeoutPropery = "session.timeout";
		String timeoutValue = loadProperty(serverProperties, timeoutPropery);
		try {
			sessionTimeout = Integer.parseInt(timeoutValue);			
		} catch (Exception e) {
			throw new RuntimeException("Illegal " + timeoutPropery + " value");
		}
		if (sessionTimeout < 0) {
			throw new RuntimeException("Illegal " + timeoutPropery + ": " + sessionTimeout);
		}
		
		documentRoot = Paths.get(loadProperty(serverProperties, "server.documentRoot"));
		
		Properties mimeTypesProperties = new Properties();
		mimeTypesProperties.load(new BufferedReader(new FileReader(Paths.get(
				serverProperties.getProperty("server.mimeConfig")).toFile())));
		mimeTypesProperties.forEach((k, v) -> {
			mimeTypes.put((String)k, (String)v);
		});
		
		Path workersPath = Paths.get(serverProperties.getProperty("server.workers"));
		checkForDuplicates(workersPath);
		Properties workersProperties = new Properties();
		workersProperties.load(new BufferedReader(new FileReader(workersPath.toFile())));
		
		workersMap = new HashMap<String, IWebWorker>();
		workersProperties.forEach((k, fqcn) -> {
			IWebWorker iww = loadWorker((String)fqcn);
			String path = (String)k;
			path = path.startsWith("/")?path.substring(1):path;
			workersMap.put(path, iww);
		});		
	}
	
	private IWebWorker loadWorker(String fqcn) {
		Class<?> referenceToClass;
		Object newObject;
		try {
			referenceToClass = this.getClass().getClassLoader().loadClass((String) fqcn);
			newObject = referenceToClass.newInstance();
		} catch (ClassNotFoundException notFound) {
			throw new RuntimeException("Class doesn't exist: " + fqcn);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("Unable to create an instance of the class: " + fqcn);
		}
		IWebWorker iww = (IWebWorker)newObject;
		return iww;
	}
	
	private void checkForDuplicates(Path propertiesPath) throws IOException {
		Set<String> keys = new HashSet<String>();
		List<String> lines = Files.readAllLines(propertiesPath);
		lines.forEach((line) -> {
			int separator = line.indexOf("=");
			String key = line.substring(0, separator).trim();
			if (keys.contains(key)) {
				throw new RuntimeException(propertiesPath + " contains duplicate keys. One of them is " + key);
			}
			keys.add(key);
		});
	}

	private String loadProperty(Properties serverProperties, String name) {
		String value = serverProperties.getProperty(name);
		if (value == null) {
			throw new RuntimeException("Illegal config file: missing " + name + " property.");
		}
		return value;
	}
	protected synchronized void start() {
		// … start server thread if not already running …
		if (serverThread == null || !serverThread.isAlive()) {
			serverThread = new ServerThread();
			serverThread.start();
		}
		//init threadpool
		if (threadPool == null || threadPool.isShutdown()) {
			threadPool = Executors.newFixedThreadPool(workerThreads);
		}
	}
	protected synchronized void stop() {
		if (serverThread != null) {
			serverThread.interrupt();			
		}
		if (threadPool != null) {
			threadPool.shutdown();			
		}
	}
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(
					new InetSocketAddress(address, port)
				);
				serverSocket.setSoTimeout(sessionTimeout);
			} catch (IOException e) {
				throw new RuntimeException("Unable to open socket on configured port.");
			}
			
			while (!isInterrupted()) {
				Socket client;
				try {
					client = serverSocket.accept();
				} catch (IOException nextClient) {
					continue;
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
			
			try {
				serverSocket.close();
			} catch (IOException ignorable) {
			}
		}
	}
	private class ClientWorker implements Runnable {
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private Map<String,String> params = new HashMap<String, String>();
		private Map<String,String> permPrams = null;
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}
		@Override
		public void run() {
			try {
				// obtain input stream from socket and wrap it to pushback input stream
				istream = new PushbackInputStream(csocket.getInputStream(), 4);
				// obtain output stream from socket
				ostream = new BufferedOutputStream(csocket.getOutputStream());
			
 			// Then read complete request header from your client in separate method...
 			List<String> request = readRequest();
 			// If header is invalid (less then a line at least) return response status 400
 			if (request == null || request.size() < 1) {
 				sendError(400, "Invalid header");
 				return;
			}
 			String firstLine = request.get(0);
 			// Extract (method, requestedPath, version) from firstLine
 			String[] firstLineParams = firstLine.trim().split("\\s+");
 			// if method not GET or version not HTTP/1.0 or HTTP/1.1 return response status 400
 			if (firstLineParams.length != 3 || !firstLineParams[0].equals("GET") ||
 					!(firstLineParams[2].equals("HTTP/1.1") || firstLineParams[2].equals("HTTP/1.1"))) {
				sendError(400, "Invalid request");
				return;
			}
 			String[] requestedArguments = firstLineParams[1].split("\\?", 2);
 			String path = requestedArguments[0]; 
 			String paramString = (requestedArguments.length == 2)?requestedArguments[1]:null;
 			// (path, paramString) = split requestedPath to path and parameterString
 			parseParameters(paramString);
 			Path requestedPath = documentRoot.resolve(path.substring(1));
 			//TODO if requestedPath is not below documentRoot, return response status 403 forbidden
 			if (!requestedPath.normalize().toAbsolutePath().startsWith(documentRoot)) {
				sendError(403, "Forbidden");
				return;
			}
 			
 			RequestContext rc = new RequestContext(ostream, params);
 			
 			final String extensionPath = "ext/";
 			String workerPath = documentRoot.relativize(requestedPath).toString();
 			if (workersMap.containsKey(workerPath)) {
				workersMap.get(workerPath).processRequest(rc);
				ostream.flush();
				ostream.close();
 				return;
			} else if (workerPath.startsWith(extensionPath)) {
				String workerName = workerPath.substring(extensionPath.length());
				loadWorker(WORKERS_PACKAGE + '.' + workerName).processRequest(rc);
				ostream.flush();
				ostream.close();
				return;
			}
 			// check if requestedPath exists, is file and is readable; if not, return status 404
 			// else extract file extension
 			if (Files.exists(requestedPath) && Files.isRegularFile(requestedPath) && Files.isReadable(requestedPath)) {				
 				String fileName = requestedPath.getFileName().toString();
 				String extension = fileName.substring(fileName.indexOf(".") + 1);
 				if (extension.equals("smscr")) {
					runSmartScript(requestedPath);
				} else {
					// find in mimeTypes map appropriate mimeType for current file extension
					// (you filled that map during the construction of SmartHttpServer from mime.properties)
					String extMimeType = mimeTypes.get(extension);
					// if no mime type found, assume application/octet-stream
					// create a rc = new RequestContext(...); set mime-type; set status to 200
					rc.setMimeType((extMimeType != null)?extMimeType:"application/octet-stream");
					rc.addHeader("Content-Length",
							Long.toString(Files.size(requestedPath)));
					rc.addHeader("Connection", "close");
					
					try (InputStream is = new BufferedInputStream(
							new FileInputStream(requestedPath.toFile()))) {
						while (true) {
							byte[] buffer = new byte[4096];
							int r = is.read(buffer);
							if (r == -1) {
								break;
							}
							
							rc.write(buffer, 0, r);
						}
					}
				}
			} else {
				sendError(404, "File not found.");
			}
 			ostream.flush();
 			// If you want, you can modify RequestContext to allow you to add additional headers
 			// so that you can add “Content-Length: 12345” if you know that file has 12345 bytes
 			// open file, read its content and write it to rc (that will generate header and send
 			// file bytes to client)
			} catch (IOException e) {
				System.err.println("IO error occured at " + new Date() + e);
			}
		}
		private void runSmartScript(Path requestedPath) throws IOException {
			new SmartScriptEngine(
					new SmartScriptParser(new String(
							Files.readAllBytes(requestedPath),
							StandardCharsets.UTF_8)).getDocumentNode(),
					new RequestContext(ostream, params)).execute();
			ostream.close();
		}
		private  void parseParameters(String paramString) {
			Map<String, String> params = new HashMap<String, String>();
			if (paramString == null) {
				this.params = params;
				return;
			}
			for (String param : paramString.split("&")) {
				String[] paramComponents = param.split("=", 2);
				if (paramComponents.length != 2) {
					throw new IllegalArgumentException("Invalid parameters");
				}
				params.put(paramComponents[0], paramComponents[1]);
			}
			this.params = params;
		}
		private List<String> readRequest() throws IOException {
			
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			List<String> lines = new LinkedList<>();
			boolean foundEnding = false;
			while (!foundEnding) {
				int b = istream.read();
				if (b == -1) {
					return null;
				}
				bos.write(b);
				if (b == 13 || bos.size() == 0) {
					return null;
				}
				
				byte[] buffer = new byte[4];
				int r = istream.read(buffer);
				if (r == -1) {
					return null;
				}
				String end = new String(buffer, 0, r, StandardCharsets.US_ASCII);
				if (end.equals("\r\n\r\n") || end.equals("\n\n")) {
					foundEnding = true;
				}
				if (end.startsWith("\r\n") || end.startsWith("\n")) {
					if (!foundEnding) {
						int lineBreak = end.indexOf("\n");
						istream.unread(buffer, lineBreak, r - lineBreak);
					}
					lines.add(new String(bos.toByteArray(), StandardCharsets.US_ASCII));
					bos.reset();
				} else {
					istream.unread(buffer, 0, r);
				}
			}
			return lines;
		}
		
		private void sendError(int statusCode, String statusText)
				throws IOException {
			RequestContext rc = new RequestContext(ostream,
					new HashMap<String, String>());
			rc.setStatusCode(statusCode);
			rc.setStatusText(statusText);
			rc.addHeader("Connection", "close");
			String body = "<h1>" + statusCode + ": " + statusText + "</h1>";
			rc.addHeader("Content-Length", Integer.toString(body
					.getBytes(StandardCharsets.UTF_8).length));
			rc.write("<h1>" + statusCode + ": " + statusText + "</h1>");

			ostream.flush();
		}
	}
	
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected single argument: server config file");
			return;
		}
		SmartHttpServer server;
		try {
			server = new SmartHttpServer(args[0]);
		} catch (IOException e) {
			System.err.println("No such file or file isn't readable.");
			return;
		}
		
		server.start();
	}
}
