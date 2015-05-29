package hr.fer.zemris.java.webserver;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RequestContextTest {

	private static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
	private static final Map<String, String> PARAMETERS = new HashMap<String, String>();
	private static final Map<String, String> PERSISTENT_PARAMETERS = new HashMap<String, String>();
	private static final List<RCCookie> OUTPUT_COOKIES = new LinkedList<RequestContext.RCCookie>();
	private static final RequestContext EMPTY_REQUEST_CONTEXT = new RequestContext(OUTPUT_STREAM, PARAMETERS, PERSISTENT_PARAMETERS, OUTPUT_COOKIES);

	public RequestContextTest() throws IOException {
		//generate header for empty request context
		EMPTY_REQUEST_CONTEXT.write("");
		OUTPUT_STREAM.reset();
	}

	@Test(expected=IllegalArgumentException.class)
	public void createContextWithNullOutput() {
		new RequestContext(null, null);
	}

	@Test
	public void getParametersTest() {
		assertEquals(null, EMPTY_REQUEST_CONTEXT.getParameter("none"));
		assertEquals(PARAMETERS, EMPTY_REQUEST_CONTEXT.getParameters());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void modifyGetParametersTest() {
		Map<String, String> getParameters = EMPTY_REQUEST_CONTEXT.getParameters();
		getParameters.put("something", "new");
	}

	@Test
	public void getPersistentParametersTest() {
		assertEquals(null, EMPTY_REQUEST_CONTEXT.getPersistentParameter("none"));
	}
	@Test
	public void getTemporaryParametersTest() {
		assertEquals(null, EMPTY_REQUEST_CONTEXT.getTemporaryParameter("none"));
	}

	@Test
	public void writeBytesToContextTest() throws IOException {
		OUTPUT_STREAM.reset();

		String testOutput = "Some text";
		byte[] data = testOutput.getBytes(StandardCharsets.UTF_8);
		EMPTY_REQUEST_CONTEXT.write(data);

		assertEquals(testOutput, new String(OUTPUT_STREAM.toByteArray(), StandardCharsets.UTF_8));

		OUTPUT_STREAM.reset();
	}

	@Test
	public void writeStringToContextTest() throws IOException {
		OUTPUT_STREAM.reset();

		String testOutput = "Some text";
		EMPTY_REQUEST_CONTEXT.write(testOutput);

		assertEquals(testOutput, new String(OUTPUT_STREAM.toByteArray(), StandardCharsets.UTF_8));

		OUTPUT_STREAM.reset();
	}

	@Test
	public void testCreatedHeader() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<RCCookie> cookies = new LinkedList<RequestContext.RCCookie>();
		cookies.add(new RCCookie("some", "value", null, null, null));
		RequestContext rc = new RequestContext(outputStream, null, null, cookies);

		String testOutput = "Some text";
		rc.write(testOutput.substring(0, 4)).write(testOutput.substring(4));

		String expectedOutput =
				"HTTP/1.1 200 OK\r\n"
						+ "Content-Type: text/html; charset=UTF-8\r\n"
						+ "Set-Cookie: some=\"value\"\r\n"
						+ "\r\n"
						+ testOutput;

		assertEquals(expectedOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
	}

	@Test
	public void testCreatedHeaderWithCustomHeaders() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<RCCookie> cookies = new LinkedList<RequestContext.RCCookie>();
		cookies.add(new RCCookie("some", "value", null, null, null));
		RequestContext rc = new RequestContext(outputStream, null, null, cookies);
		rc.addHeader("Server", "Smart HTTP Server");

		String testOutput = "Some text";
		rc.write(testOutput.substring(0, 4)).write(testOutput.substring(4));

		String expectedOutput =
				"HTTP/1.1 200 OK\r\n"
						+ "Content-Type: text/html; charset=UTF-8\r\n"
						+ "Set-Cookie: some=\"value\"\r\n"
						+ "Server: Smart HTTP Server\r\n"
						+ "\r\n"
						+ testOutput;

		assertEquals(expectedOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
	}

	@Test(expected=RuntimeException.class)
	public void modifyEncodingAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.setEncoding("US_ASCII");
	}

	@Test(expected=RuntimeException.class)
	public void modifyMimeTypeAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.setMimeType("text/plain");
	}

	@Test(expected=RuntimeException.class)
	public void modifyStatusCodeAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.setStatusCode(400);
	}

	@Test(expected=RuntimeException.class)
	public void modifyStatusTextAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.setStatusText("Error");
	}

	@Test(expected=RuntimeException.class)
	public void changeOutputCookiesAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.setOutputCookies(new LinkedList<RequestContext.RCCookie>());
	}

	@Test(expected=RuntimeException.class)
	public void modifyOutputCookiesAfterGenerationOfHeader() {
		EMPTY_REQUEST_CONTEXT.addRCCookie(new RCCookie("some", "text", null, null, null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void setEncodingNull() {
		new RequestContext(OUTPUT_STREAM, PARAMETERS).setEncoding(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setMimeTypeNull() {
		new RequestContext(OUTPUT_STREAM, PARAMETERS).setMimeType(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setStatusTextNull() {
		new RequestContext(OUTPUT_STREAM, PARAMETERS).setStatusText(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void addNullCookie() {
		new RequestContext(OUTPUT_STREAM, PARAMETERS).addRCCookie(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setOutputCookiesNull() {
		new RequestContext(OUTPUT_STREAM, PARAMETERS).setOutputCookies(null);
	}




}
