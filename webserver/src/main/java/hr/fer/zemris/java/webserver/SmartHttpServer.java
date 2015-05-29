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

/**
 * SmartHttpServer is a simple HTTP server which takes GET requests. It's run by
 * typing "start" in program's input and shutdown by typing "stop".
 *
 * Server can be configured with a .properties which which should be given as
 * only argument to the program. Configuration file should have all the
 * properties which default configuration has. No additional properties are
 * defined. Configuration file also needs a path to two additional .properties
 * files. One is a workers configuration file which specifies all workers paths.
 * Second configuration file defines all supported mime types.
 *
 * Smart scripts are also supported and are executed with
 * {@link SmartScriptEngine} which writes their output to the client.
 *
 * @author Luka Skugor
 *
 */
public class SmartHttpServer {

	/**
	 * Root folder of the server where accessible documents are stored.
	 */
	private Path documentRoot;
	/**
	 * Server's address.
	 */
	private String address;
	/**
	 * Server's access port.
	 */
	private int port;
	/**
	 * Session timeout in milliseconds.
	 */
	private int sessionTimeout;

	/**
	 * Map of supported mime types.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Extension of smart script files.
	 */
	private String smartScriptExt;

	/**
	 * Map of workers. Keys are worker's names and values are actual workers.
	 */
	private Map<String, IWebWorker> workersMap;
	/**
	 * Number of worker threads.
	 */
	private int workerThreads;
	/**
	 * Path in which only workers should be loaded.
	 */
	private String workersSubfolder;

	/**
	 * Thread which takes requests. This thread only takes requests and
	 * delegates processing of that request to other threads.
	 */
	private ServerThread serverThread;
	/**
	 * Thread pool which processes requests.
	 */
	private ExecutorService threadPool;

	/**
	 * Map which stores clients' sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/**
	 * This thread cleans up {@link #sessions}.
	 */
	Thread sessionGarbageCollector = new Thread(() -> {
		while (true) {
			try {
				Thread.sleep(1000 * 60 * 5);
			} catch (InterruptedException ignorable) {
			}
			synchronized (sessions) {
				sessions.values().removeIf(e -> {
					return e.validUntil < System.currentTimeMillis();
				});
			}
		}
	});
	/**
	 * Random generator which is used to creates {@link ClientWorker#SID}.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Name of the package where workers are packaged.
	 */
	final static private String WORKERS_PACKAGE = "hr.fer.zemris.java.webserver.workers";

	/**
	 * Creates a new SmartHttpServer with given configuration file.
	 *
	 * @param configFileName
	 *            path to configuration file
	 * @throws IOException
	 *             if configuration file doesn't exist or isn't readable.
	 * @throws RuntimeException
	 *             if configuration is invalid
	 */
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
		checkPropertyForDuplicates(workersPath);
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

	/**
	 * Loads a worker for given fully qualified class name.
	 *
	 * @param fqcn
	 *            fully qualified class name of the worker
	 * @return loaded worker
	 * @throws RuntimeException
	 *             if worker couldn't be loaded.
	 */
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

	/**
	 * Checks property file for duplicate keys and throws exception if
	 * duplicates are found.
	 *
	 * @param propertiesPath
	 *            path to .properties file
	 * @throws IOException
	 *             if properties file doesn't exist or isn't readable
	 * @throws RuntimeException
	 *             if .properties file contains duplicate keys
	 */
	private void checkPropertyForDuplicates(Path propertiesPath)
			throws IOException {
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

	/**
	 * Loads a property from {@link Properties}. If such property doesn't exist
	 * throws an exception.
	 *
	 * @param properties
	 *            collection which contains a property
	 * @param name
	 *            name of property which will be loaded
	 * @return loaded property
	 */
	private String loadProperty(Properties properties, String name) {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new RuntimeException("Illegal config file: missing " + name
					+ " property.");
		}
		return value;
	}

	/**
	 * Loads an integer property which must be within given constraints.
	 *
	 * @param properties
	 *            collection which contains a property
	 * @param name
	 *            name of the property to be loaded
	 * @param minValue
	 *            minimum value which property should have or null for -infinity
	 * @param maxValue
	 *            maximum value which property should have or null for +infinity
	 * @return loaded property
	 * @throws RuntimeException
	 *             if property's value is not a number
	 * @throws IllegalArgumentException
	 *             if property doesn't satisfies given constraints
	 */
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

	/**
	 * Starts server thread.
	 */
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

	/**
	 * Stops servers thread.
	 */
	protected synchronized void stop() {
		if (serverThread != null) {
			serverThread.interrupt();
		}
		if (threadPool != null) {
			threadPool.shutdown();
		}
	}

	/**
	 * ServerThread is server's thread for accepting requests.
	 *
	 * @author Luka Skugor
	 *
	 */
	protected class ServerThread extends Thread {
		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Thread#run()
		 */
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

	/**
	 * ClientWorker processes a request taken by the server. In outer class is
	 * documented how ClientWorker processes a request.
	 *
	 * @author Luka Skugor
	 *
	 */
	private class ClientWorker implements Runnable {

		/**
		 * Client's socket.
		 */
		private Socket csocket;
		/**
		 * Input stream of the client's request.
		 */
		private PushbackInputStream istream;
		/**
		 * Client's output stream.
		 */
		private OutputStream ostream;

		/**
		 * Request's version.
		 */
		private String version;
		/**
		 * Request's method.
		 */
		private String method;
		/**
		 * Requested path.
		 */
		private Path requestedPath;

		/**
		 * Request parameters.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Request's cookies.
		 */
		private Map<String, String> permParams = null;

		/**
		 * List of cookies which will be set in the response.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session identifier.
		 */
		private String SID;

		/**
		 * Creates a new ClientWorker with given client's socket.
		 *
		 * @param csocket
		 *            client's socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream(), 4);
				ostream = new BufferedOutputStream(csocket.getOutputStream());

				// get headers
				List<String> request = readRequest();

				checkSession(request);
				parseFirstLine(request);

				RequestContext rc = new RequestContext(ostream, params,
						permParams, outputCookies);

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

		/**
		 * Checks client's session. If session hasn't yet been created or has
		 * expired it creates a new session. If session already exists and
		 * hasn't timeout yet it updates session age.
		 *
		 * @param headers
		 *            request's headers
		 */
		private void checkSession(List<String> headers) {

			final String cookieHeader = "Cookie:";

			String sidCandidate = null;
			for (String line : headers) {
				if (!line.startsWith(cookieHeader)) {
					continue;
				}
				sidCandidate = parseCookies(
						line.substring(cookieHeader.length())).get("sid");
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
						SID = sessionEntry.sid;
					}
				}
			}
		}

		/**
		 * Creates a session {@link #SID}.
		 */
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

		/**
		 * Parses cookies from request's cookie header value.
		 *
		 * @param cookiesString
		 *            cookie header value
		 * @return map of parsed cookies
		 */
		private Map<String, String> parseCookies(String cookiesString) {
			Map<String, String> cookies = new HashMap<String, String>();

			for (String cookie : cookiesString.trim().split(";")) {
				String[] cookieArgs = cookie.split("=", 2);
				if (cookieArgs[1].startsWith("\"")
						&& cookieArgs[1].endsWith("\"")) {
					cookieArgs[1] = cookieArgs[1].substring(1,
							cookieArgs[1].length() - 1);
				}
				cookies.put(cookieArgs[0],
						(cookieArgs.length == 2) ? cookieArgs[1] : null);
			}
			return cookies;
		}

		/**
		 * Loads a file and sends it to a client as a response.
		 *
		 * @param requestedPath
		 *            full path of requested path
		 * @param rc
		 *            client's request's context
		 * @throws IOException
		 *             if file doesn't exist or isn't readable
		 */
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

		/**
		 * Delegates a request to a worker. Delegation to worker is based on
		 * requested path.
		 *
		 * @param requestedPath
		 *            requested path
		 * @param rc
		 *            client's request's context
		 * @return true if request was given to a worker
		 * @throws RuntimeException
		 *             if it fails to load a worker
		 */
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

		/**
		 * Parses first line of the request's header.
		 *
		 * @param request
		 *            request headers
		 * @return true if parsing was successful and otherwise false
		 * @throws IOException
		 *             if IO exception occurs
		 */
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

		/**
		 * Parses request's parameters.
		 *
		 * @param paramString
		 *            string containing all parameters
		 */
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

		/**
		 * Runs a Smart Script and writes its result as a response to the
		 * client.
		 *
		 * @param requestedPath
		 *            requested script to be executed
		 * @param rc
		 *            client's request's context
		 * @throws IOException
		 *             if IO exception occurs
		 */
		private void runSmartScript(Path requestedPath, RequestContext rc)
				throws IOException {
			try {
				new SmartScriptEngine(new SmartScriptParser(new String(
						Files.readAllBytes(requestedPath),
						StandardCharsets.UTF_8)).getDocumentNode(), rc)
				.execute();
			} catch (Exception e) {
				rc.write("Error: " + e.getMessage());
			}
			ostream.flush();
			ostream.close();
		}

		/**
		 * Reads client's request line by line.
		 *
		 * @return lines of read request or <code>null</code> if header is
		 *         invalid
		 * @throws IOException
		 *             if IO error occurs
		 */
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

		/**
		 * Sends an error response to the client.
		 *
		 * @param statusCode
		 *            error status code
		 * @param statusText
		 *            error status text
		 * @param rc
		 *            client's request's context
		 * @throws IOException
		 *             if IO exception occurs
		 */
		private void sendError(int statusCode, String statusText,
				RequestContext rc) throws IOException {
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

	/**
	 * SessionMapEntry stores data of a client session.
	 *
	 * @author Luka Skugor
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * Session's identifier.
		 */
		String sid;
		/**
		 * Time until the session is valid in milliseconds.
		 */
		long validUntil;
		/**
		 * Session's data.
		 */
		Map<String, String> map;

		/**
		 * Updates validity of the session.
		 *
		 * @param timeout
		 *            session timeout
		 */
		public void updateValidity(long timeout) {
			validUntil = System.currentTimeMillis() + timeout * 1000;
		}
	}

	/**
	 * Called on program start.
	 *
	 * @param args
	 *            command line arguments
	 */
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
