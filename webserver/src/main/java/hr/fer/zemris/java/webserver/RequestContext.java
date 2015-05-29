package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RequestContext represents a context of a GET HTTP request. It stores
 * request's parameters and user cookies. Additionally, it has temporary
 * parameters which be used for calculation. It can also generate a response to
 * the request which is written directly to the client's output stream.
 *
 * Response header has some default values:
 *
 * Request body has encoding in UTF-8.
 *
 * Request's status is 200 with message "OK".
 *
 * Type of request's body is text/html.
 *
 * @author Luka Skugor
 *
 */
public class RequestContext {

	/**
	 * Default header encoding.
	 */
	private static final Charset HEADER_CHARSET = Charset.forName("ISO_8859_1");
	/**
	 * Default header HTTP version.
	 */
	private static final String HTTP_VERSION = "HTTP/1.1";
	/**
	 * Content type header name.
	 */
	private static final String CONTENT_TYPE = "Content-Type";
	/**
	 * Set cookie header name.
	 */
	private static final String SET_COOKIE = "Set-Cookie";

	/**
	 * Client's output stream.
	 */
	private OutputStream outputStream;
	/**
	 * Body encoding.
	 */
	private Charset charset;

	/**
	 * Body encoding as string.
	 */
	private String encoding = "UTF-8";
	/**
	 * Response status code.
	 */
	private int statusCode = 200;
	/**
	 * Response status text.
	 */
	private String statusText = "OK";
	/**
	 * Response mime type.
	 */
	private String mimeType = "text/html";

	/**
	 * Client's request's parameters.
	 */
	private Map<String, String> parameters;
	/**
	 * Context's temporary parameters.
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * User cookies.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * Additional response headers.
	 */
	private Map<String, String> headers = new HashMap<String, String>();
	/**
	 * Newly set user cookies.
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Indicates if header was generated.
	 */
	private boolean headerGenerated = false;

	/**
	 * Creates a new RequestContext with given parameters.
	 *
	 * @param outputStream
	 *            client's output stream.
	 * @param parameters
	 *            client's request's parameters
	 * @param persistentParameters
	 *            client's cookies
	 * @param outputCookies
	 *            new cookie which will be set
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {
		if (outputStream == null) {
			throw new IllegalArgumentException("Output stream can't be null");
		}
		this.outputStream = outputStream;
		this.parameters = (parameters != null) ? parameters
				: new HashMap<String, String>();
		this.persistentParameters = (persistentParameters != null) ? persistentParameters
				: new HashMap<String, String>();
		this.outputCookies = (outputCookies != null) ? outputCookies
				: new LinkedList<>();
		temporaryParameters = new HashMap<String, String>();

	}

	/**
	 * Creates a new RequestContext without any cookies.
	 *
	 * @param outputStream
	 *            client's output stream
	 * @param parameters
	 *            client's request's parameters
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters) {
		this(outputStream, parameters, null, null);
	}

	/**
	 * Adds a header to the response
	 *
	 * @param headerName
	 *            header name
	 * @param headerValue
	 *            header value
	 */
	public void addHeader(String headerName, String headerValue) {
		headers.put(headerName, headerValue);
	}

	/**
	 * Sets encoding for response's body.
	 *
	 * @param encoding
	 *            set encoding
	 * @throws RuntimeException
	 *             if header has already been generated
	 * @throws IllegalArgumentException
	 *             if given encoding is <code>null</code>
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (encoding == null) {
			throw new IllegalArgumentException("Null encoding is not allowed");
		}
		this.encoding = encoding;
	}

	/**
	 * Sets response's status code.
	 *
	 * @param statusCode
	 *            status code
	 * @throws RuntimeException
	 *             if header has already been generated
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Sets response's status text.
	 *
	 * @param statusText
	 *            status text
	 * @throws RuntimeException
	 *             if header has already been generated
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (statusText == null) {
			throw new IllegalArgumentException(
					"Null status text is not allowed");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets response's mime type.
	 *
	 * @param mimeType
	 *            body's mime type
	 * @throws RuntimeException
	 *             if header has already been generated
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (mimeType == null) {
			throw new IllegalArgumentException("Null mime-type is not allowed");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Adds a cookie to the list of cookies which will be newly created on
	 * client's end.
	 *
	 * @param cookie
	 *            newly created cookie
	 * @throws RuntimeException
	 *             if header has already been generated
	 */
	public void addRCCookie(RCCookie cookie) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (cookie == null) {
			throw new IllegalArgumentException("Null cookies are not allowed");
		}
		outputCookies.add(cookie);
	}


	/**
	 * Sets output cookies list.
	 *
	 * @param outputCookies
	 *            output cookies
	 * @throws RuntimeException
	 *             if header has already been generated
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (outputCookies == null) {
			throw new IllegalArgumentException("Null output-cookies is not allowed");
		}
		this.outputCookies = outputCookies;
	}

	/**
	 * Gets context's parameter by parameter name.
	 *
	 * @param name
	 *            parameter's name
	 * @return requested parameter
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets all parameters' names.
	 *
	 * @return set of parameters' names
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Gets persistent parameter by persistent parameter name.
	 *
	 * @param name
	 *            persistent parameter's name
	 * @return requested persistent parameter
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Gets all persistent parameters' names.
	 *
	 * @return set of persistent parameters' names
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Sets persistent parameter for given persistent parameter name.
	 *
	 * @param name
	 *            persistent parameter's name
	 * @param value
	 *            set parameter value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes persistent parameter by given persistent parameter name.
	 *
	 * @param name
	 *            persistent parameter's name
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Gets temporary parameter by temporary parameter name.
	 *
	 * @param name
	 *            temporary parameter's name
	 * @return requested temporary parameter
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Gets all temporary parameters' names.
	 *
	 * @return set of temporary parameters' names
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Sets temporary parameter for given temporary parameter name.
	 *
	 * @param name
	 *            temporary parameter's name
	 * @param value
	 *            set parameter value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes temporary parameter by given temporary parameter name.
	 *
	 * @param name
	 *            temporary parameter's name
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Writes data to response's body.
	 *
	 * @param data
	 *            data which will be written
	 * @return this request context
	 * @throws IOException
	 *             if IO exception occurs
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}

	/**
	 * Writes data of the given length to response's body with given offset in
	 * data.
	 *
	 * @param data
	 *            data which will be written
	 * @param offset
	 *            index from which data is written
	 * @param length
	 *            length of written data
	 * @return this request context
	 * @throws IOException if IO error occurs
	 */
	public RequestContext write(byte[] data, int offset, int length)
			throws IOException {
		if (!headerGenerated) {
			createHeader();
		}
		outputStream.write(data, offset, length);
		return this;
	}

	/**
	 * Writes a string to response's body encoded in request context's charset.
	 *
	 * @param text
	 *            text to be written
	 * @return this request context
	 * @throws IOException
	 *             if IO exception occurs
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			createHeader();
		}
		outputStream.write(text.getBytes(charset));
		return this;
	}

	/**
	 * Creates the response header.
	 *
	 * @throws IOException
	 *             if IO exception occurs
	 * @throws RuntimeException if response's encoding is doesn't exist
	 */
	private void createHeader() throws IOException {
		headerGenerated = true;
		try {
			charset = Charset.forName(encoding);
		} catch (Exception illegalCharset) {
			throw new RuntimeException("Illegal charset given");
		}

		// Header first line
		writeHeader(HTTP_VERSION);
		writeHeader(" ");
		writeHeader(Integer.toString(statusCode));
		writeHeader(" ");
		writeHeader(statusText);
		writeHeader("\r\n");

		// Header content-type
		writeHeader(CONTENT_TYPE);
		writeHeader(": ");
		writeHeader(mimeType);
		if (mimeType.startsWith("text/")) {
			writeHeader("; charset=");
			writeHeader(encoding);
		}
		writeHeader("\r\n");

		// Header set cookies
		for (RCCookie rcCookie : outputCookies) {
			writeHeader(SET_COOKIE);
			writeHeader(": ");
			writeHeader(rcCookie.toString());
			writeHeader("\r\n");
		}

		for (Map.Entry<String, String> headerLine : headers.entrySet()) {
			writeHeader(headerLine.getKey() + ": " + headerLine.getValue()
					+ "\r\n");
		}

		writeHeader("\r\n");
	}

	/**
	 * Writes a String to header encoded with {@link #HEADER_CHARSET}.
	 *
	 * @param text
	 *            text to be written to header
	 * @throws IOException
	 *             if IO exception occurs
	 */
	private void writeHeader(String text) throws IOException {
		outputStream.write(text.getBytes(HEADER_CHARSET));
	}

	/**
	 * This class represents a HTTP cookie.
	 *
	 * @author Luka Skugor
	 *
	 */
	public static class RCCookie {
		/**
		 * Cookie name.
		 */
		private String name;
		/**
		 * Cookie value.
		 */
		private String value;
		/**
		 * Cookie domain.
		 */
		private String domain;
		/**
		 * Cookie path.
		 */
		private String path;
		/**
		 * Cookie's maximum age in milliseconds.
		 */
		private Integer maxAge;
		/**
		 * Indicates if cookies is HTTP only.
		 */
		private boolean httpOnly;

		/**
		 * Creates a new cookie with given parameters.
		 *
		 * @param name
		 *            cookie's name
		 * @param value
		 *            cookie's value
		 * @param maxAge
		 *            cookie's maximum age in milliseconds
		 * @param domain
		 *            cookie's domain
		 * @param path
		 *            cookie's path
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Gets cookie's name.
		 *
		 * @return cookie's name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets cookie's value.
		 *
		 * @return cookie's value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets cookie's domain.
		 *
		 * @return cookie's domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Gets cookie's path.
		 *
		 * @return cookie's path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Gets cookie's maximum age in milliseconds.
		 *
		 * @return cookie's maximum age in milliseconds
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

		/**
		 * Sets whether the cookie will be HTTP only.
		 *
		 * @param set
		 *            if true cookie will be HTTP only and won't otherwise
		 */
		public void setHttpOnly(boolean set) {
			httpOnly = set;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append(name).append("=\"").append(value).append("\"");

			if (domain != null) {
				stringBuilder.append("; ").append("Domain").append("=")
				.append(domain);
			}
			if (path != null) {
				stringBuilder.append("; ").append("Path").append("=")
				.append(path);
			}
			if (maxAge != null) {
				stringBuilder.append("; ").append("Max-Age").append("=")
				.append(maxAge);
			}
			if (httpOnly) {
				stringBuilder.append("; HttpOnly");
			}

			return stringBuilder.toString();
		}
	}

	/**
	 * Sets persistent parameters map.
	 *
	 * @param persistentParameters
	 *            persistent parameters map
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}

	/**
	 * Sets temporary parameters map.
	 *
	 * @param temporaryParameters
	 *            temporary parameters map
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Gets unmodifiable map of parameters.
	 *
	 * @return unmodifiable map of parameters
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

}
