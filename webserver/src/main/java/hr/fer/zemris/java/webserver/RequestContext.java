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

public class RequestContext {
	
	private static final Charset HEADER_CHARSET = Charset.forName("ISO_8859_1");
	private static final String HTTP_VERSION = "HTTP/1.1";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String SET_COOKIE = "Set-Cookie";

	private OutputStream outputStream;
	private Charset charset;
	
	private String encoding = "UTF-8";
	private int statusCode = 200;
	private String statusText = "OK";
	private String mimeType = "text/html";

	private Map<String, String> parameters;
	private Map<String, String> temporaryParameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;

	private boolean headerGenerated = false;

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

	}

	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (encoding == null) {
			throw new IllegalArgumentException("Null encoding is not allowed");
		}
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		}
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (statusText == null) {
			throw new IllegalArgumentException("Null status text is not allowed");
		}
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (mimeType == null) {
			throw new IllegalArgumentException("Null mime-type is not allowed");
		}
		this.mimeType = mimeType;
	}

	public void addRCCookie(RCCookie cookie) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header has already been generated and you're not allowed to modify it.");
		} else if (cookie == null) {
			throw new IllegalArgumentException("Null cookies are not allowed");
		}
		outputCookies.add(cookie);
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			createHeader();
		}
		outputStream.write(data);
		return this;
	}

	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			createHeader();
		}
		outputStream.write(text.getBytes(charset));
		return this;
	}

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
		
		writeHeader("\r\n");
	}
	
	private void writeHeader(String text) throws IOException {
		outputStream.write(text.getBytes(HEADER_CHARSET));
	}

	public static class RCCookie {
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;

		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public Integer getMaxAge() {
			return maxAge;
		}
		
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			
			stringBuilder.append(name).append("=\"").append(value).append("\"");
			
			if (domain != null) {
				stringBuilder.append("; ").append("Domain").append("=").append(domain);
			}
			if (path != null) {
				stringBuilder.append("; ").append("Path").append("=").append(path);
			}
			if (maxAge != null) {
				stringBuilder.append("; ").append("Max-Age").append("=").append(maxAge);
			}
			
			return stringBuilder.toString();
		}
	}

}
