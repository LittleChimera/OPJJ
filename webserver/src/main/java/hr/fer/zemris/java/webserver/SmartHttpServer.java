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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartHttpServer {

	private Path documentRoot;
	private String address;
	private int port;
	private int sessionTimeout;

	private Map<String, String> mimeTypes = new HashMap<String, String>();
	private String smartScriptExt;

	private Map<String, IWebWorker> workersMap;
	private int workerThreads;
	private String workersSubfolder;

	private ServerThread serverThread;
	private ExecutorService threadPool;

	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	Thread sessionGarbageCollector = new Thread(() -> {
		while (true) {
			try {
				Thread.sleep(1000*60*5);
			} catch (InterruptedException ignorable) {
			}
			synchronized (sessions) {
				sessions.values().removeIf(e -> {
					return e.validUntil < System.currentTimeMillis();
				});
			}
		}
	});
	private Random sessionRandom = new Random();

	final static private String WORKERS_PACKAGE = "hr.fer.zemris.java.webserver.workers";

	public SmartHttpServer(String configFileName) throws IOException {
		sessionGarbageCollector.setDaemon(true);
		sessionGarbageCollector.start();

		Properties serverProperties = new Properties();
		serverProperties.load(new BufferedReader(new FileReader(Paths.get(
				configFileName).toFile())));

		address = loadProperty(serverProperties, "server.address");

		final int maxPort = 65535;
		port = loadIntegerProperty(serverProperties, "server.port", 0, maxPort);
		workerThreads = loadIntegerProperty(serverProperties,
				"server.workerThreads", 0, null);
		sessionTimeout = loadIntegerProperty(serverProperties,
				"session.timeout", 0, null);

		documentRoot = Paths.get(loadProperty(serverProperties,
				"server.documentRoot"));
		workersSubfolder = loadProperty(serverProperties,
				"server.workersSubfolder");

		smartScriptExt = loadProperty(serverProperties, "script.smartScriptExt");
		Properties mimeTypesProperties = new Properties();
		mimeTypesProperties.load(new BufferedReader(new FileReader(Paths.get(
				serverProperties.getProperty("server.mimeConfig")).toFile())));
		mimeTypesProperties.forEach((k, v) -> {
			mimeTypes.put((String) k, (String) v);
		});

		Path workersPath = Paths.get(serverProperties
				.getProperty("server.workers"));
		checkForDuplicates(workersPath);
		Properties workersProperties = new Properties();
		workersProperties.load(new BufferedReader(new FileReader(workersPath
				.toFile())));

		workersMap = new HashMap<String, IWebWorker>();
		workersProperties.forEach((k, fqcn) -> {
			IWebWorker iww = loadWorker((String) fqcn);
			String path = (String) k;
			path = path.startsWith("/") ? path.substring(1) : path;
			workersMap.put(path, iww);
		});
	}

	private IWebWorker loadWorker(String fqcn) {
		Class<?> referenceToClass;
		Object newObject;
		try {
			referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			newObject = referenceToClass.newInstance();
		} catch (ClassNotFoundException notFound) {
			throw new IllegalArgumentException("Class doesn't exist: " + fqcn);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(
					"Unable to create an instance of the class: " + fqcn);
		}
		IWebWorker iww = (IWebWorker) newObject;
		return iww;
	}

	private void checkForDuplicates(Path propertiesPath) throws IOException {
		Set<String> keys = new HashSet<String>();
		List<String> lines = Files.readAllLines(propertiesPath);
		lines.forEach((line) -> {
			int separator = line.indexOf("=");
			String key = line.substring(0, separator).trim();
			if (keys.contains(key)) {
				throw new RuntimeException(propertiesPath
						+ " contains duplicate keys. One of them is " + key);
			}
			keys.add(key);
		});
	}

	private String loadProperty(Properties properties, String name) {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new RuntimeException("Illegal config file: missing " + name
					+ " property.");
		}
		return value;
	}

	private int loadIntegerProperty(Properties properties, String name,
			Integer minValue, Integer maxValue) {
		int value;
		try {
			value = Integer.parseInt(loadProperty(properties, name));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Expected an integer between "
					+ (minValue == null ? "-inf" : minValue) + " and "
					+ (maxValue == null ? "inf" : maxValue) + " for '" + name
					+ "' property");
		}
		if (minValue != null && value < minValue) {
			throw new IllegalArgumentException("Minimum value for '" + name
					+ "' property is " + minValue);
		}
		if (maxValue != null && value > maxValue) {
			throw new IllegalArgumentException("Maximum value for '" + name
					+ "' property is " + maxValue);
		}
		return value;
	}

	protected synchronized void start() {
		// … start server thread if not already running …
		if (serverThread == null || !serverThread.isAlive()) {
			serverThread = new ServerThread();
			serverThread.start();
		}
		// init threadpool
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
				serverSocket.bind(new InetSocketAddress(address, port));
			} catch (IOException e) {
				throw new RuntimeException(
						"Unable to open socket on configured port.");
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
		private Path requestedPath;

		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> permParams = null;

		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;

		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream(), 4);
				ostream = new BufferedOutputStream(csocket.getOutputStream());


				// get headers
				List<String> request = readRequest();

				checkSession(request);
				parseFirstLine(request);

				RequestContext rc = new RequestContext(ostream, params, permParams, outputCookies);

				// if request path is above document root "403 forbidden"
				// respond is sent
				if (!requestedPath.normalize().toAbsolutePath()
						.startsWith(documentRoot)) {
					sendError(403, "Forbidden", rc);
					return;
				}


				// inspect if requested path should be delegated to a worker
				try {
					if (giveToWorker(requestedPath, rc)) {
						ostream.flush();
						ostream.close();
						return;
					}
				} catch (Exception e) {
					sendError(404, "No such worker.", rc);
					return;
				}

				if (!(Files.exists(requestedPath)
						&& Files.isRegularFile(requestedPath) && Files
						.isReadable(requestedPath))) {
					sendError(404, "File not found.", rc);
					return;
				} else {
					String fileName = requestedPath.getFileName().toString();
					String extension = fileName
							.substring(fileName.indexOf(".") + 1);

					if (extension.equals(smartScriptExt)) {
						runSmartScript(requestedPath, rc);
					} else {
						String extMimeType = mimeTypes.get(extension);
						final String defaultMime = "application/octet-stream";

						rc.setMimeType((extMimeType != null) ? extMimeType
								: defaultMime);
						rc.addHeader("Content-Length",
								Long.toString(Files.size(requestedPath)));
						rc.addHeader("Connection", "close");

						sendFile(requestedPath, rc);
					}
				}
				ostream.flush();
				ostream.close();
			} catch (IOException e) {
				System.err.println("IO error occured at " + new Date() + e);
			}
		}

		private void checkSession(List<String> headers) {

			final String cookieHeader = "Cookie:";

			String sidCandidate = null;
			for (String line : headers) {
				if (!line.startsWith(cookieHeader)) {
					continue;
				}
				sidCandidate = parseCookies(line.substring(cookieHeader.length())).get("sid");
				break;
			}
			if (sidCandidate == null) {
				createSID();
			} else {
				SessionMapEntry sessionEntry;
				synchronized (sessions) {
					sessionEntry = sessions.get(sidCandidate);
					if (sessionEntry == null
							|| sessionEntry.validUntil < System
							.currentTimeMillis()) {
						createSID();
					} else {
						sessionEntry.updateValidity(sessionTimeout);
						permParams = sessionEntry.map;
					}
				}
			}
		}

		private void createSID() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			sessionRandom.ints(20).forEach(i -> {
				// ASCII values of A and Z
				final int A = 65;
				final int Z = 90;
				bos.write(Math.abs(i) % (Z - A) + A);
			});
			SID = new String(bos.toByteArray(), StandardCharsets.US_ASCII);

			SessionMapEntry sessionEntry = new SessionMapEntry();
			sessionEntry.map = new ConcurrentHashMap<String, String>();
			sessionEntry.sid = SID;
			sessionEntry.updateValidity(sessionTimeout);
			permParams = sessionEntry.map;

			synchronized (sessions) {
				sessions.put(SID, sessionEntry);
			}

			RCCookie newSid = new RCCookie("sid", SID, null, address, "/");
			newSid.setHttpOnly(true);
			outputCookies.add(newSid);
		}

		private Map<String, String> parseCookies(String cookiesString) {
			Map<String, String> cookies = new HashMap<String, String>();

			for (String cookie : cookiesString.trim().split(";")) {
				String[] cookieArgs = cookie.split("=", 2);
				if (cookieArgs[1].startsWith("\"") && cookieArgs[1].endsWith("\"")) {
					cookieArgs[1] = cookieArgs[1].substring(1, cookieArgs[1].length() - 1);
				}
				cookies.put(cookieArgs[0],
						(cookieArgs.length == 2) ? cookieArgs[1] : null);
			}
			return cookies;
		}

		private void sendFile(Path requestedPath, RequestContext rc)
				throws IOException {
			try (InputStream is = new BufferedInputStream(new FileInputStream(
					requestedPath.toFile()))) {
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

		private boolean giveToWorker(Path requestedPath, RequestContext rc) {
			String workerPath = documentRoot.relativize(requestedPath)
					.toString();

			IWebWorker worker = null;
			if (workersMap.containsKey(workerPath)) {
				worker = workersMap.get(workerPath);

			} else if (workerPath.startsWith(workersSubfolder)) {
				String workerName = workerPath.substring(workersSubfolder
						.length());
				worker = loadWorker(WORKERS_PACKAGE + '.' + workerName);
			}

			if (worker != null) {
				worker.processRequest(rc);
				return true;
			}
			return false;
		}

		private boolean parseFirstLine(List<String> request) throws IOException {
			if (request == null || request.size() < 1) {
				sendError(400, "Invalid header", null);
				return false;
			}

			String firstLine = request.get(0);
			// Extract (method, requestedPath, version) from firstLine
			String[] firstLineParams = firstLine.trim().split("\\s+");

			method = firstLineParams[0];
			version = firstLineParams[2];

			// if method not GET or version not HTTP/1.0 or HTTP/1.1 return
			// response status 400
			if (firstLineParams.length != 3
					|| !method.equals("GET")
					|| !(version.equals("HTTP/1.1") || version
							.equals("HTTP/1.1"))) {
				sendError(400, "Invalid request", null);
				return false;
			}

			String[] requestedPathArguments = firstLineParams[1]
					.split("\\?", 2);
			String path = requestedPathArguments[0];
			parseParameters((requestedPathArguments.length == 2) ? requestedPathArguments[1]
					: null);
			requestedPath = documentRoot.resolve(path.substring(1));
			return true;
		}

		private void parseParameters(String paramString) {
			Map<String, String> params = new HashMap<String, String>();
			if (paramString == null) {
				this.params = params;
				return;
			}
			for (String param : paramString.split("&")) {
				String[] paramComponents = param.split("=", 2);
				params.put(paramComponents[0],
						(paramComponents.length == 2) ? paramComponents[1]
								: null);
			}
			this.params = params;
		}

		private void runSmartScript(Path requestedPath, RequestContext rc) throws IOException {
			new SmartScriptEngine(
					new SmartScriptParser(new String(
							Files.readAllBytes(requestedPath),
							StandardCharsets.UTF_8)).getDocumentNode(),
							rc).execute();
			ostream.close();
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
						int lineBreak = end.indexOf("\n") + 1;
						istream.unread(buffer, lineBreak, r - lineBreak);
					}
					lines.add(new String(bos.toByteArray(),
							StandardCharsets.US_ASCII));
					bos.reset();
				} else {
					istream.unread(buffer, 0, r);
				}
			}
			return lines;
		}

		private void sendError(int statusCode, String statusText, RequestContext rc)
				throws IOException {
			if (rc == null) {
				rc = new RequestContext(ostream, params);
			}

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

	private static class SessionMapEntry {
		String sid;
		long validUntil;
		Map<String, String> map;

		public void updateValidity(long timeout) {
			validUntil = System.currentTimeMillis() + timeout*1000;
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
		} catch (RuntimeException e) {
			System.err.println("Initialization error" + e.getMessage());
			return;
		}

		server.start();
	}
}
