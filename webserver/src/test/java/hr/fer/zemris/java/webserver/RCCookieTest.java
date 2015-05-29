package hr.fer.zemris.java.webserver;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import org.junit.Test;

public class RCCookieTest {

	@Test
	public void toStringWithSetNameValue() {
		RCCookie cookie = new RCCookie("name", "john", null, null, null);
		assertEquals("name=\"john\"", cookie.toString());
	}

	@Test
	public void toStringWithSetNameValueMaxAge() {
		RCCookie cookie = new RCCookie("name", "john", 3600, null, null);
		assertEquals("name=\"john\"; Max-Age=3600", cookie.toString());
	}

	@Test
	public void toStringWithSetNameValueDomain() {
		RCCookie cookie = new RCCookie("name", "john", null, "localhost", null);
		assertEquals("name=\"john\"; Domain=localhost", cookie.toString());
	}

	@Test
	public void toStringWithSetNameValuePath() {
		RCCookie cookie = new RCCookie("name", "john", null, null, "/");
		assertEquals("name=\"john\"; Path=/", cookie.toString());
	}

	@Test
	public void toStringHTTPOnly() {
		RCCookie cookie = new RCCookie("name", "john", null, null, null);
		cookie.setHttpOnly(true);
		assertEquals("name=\"john\"; HttpOnly", cookie.toString());
	}

	@Test
	public void toStringWithAllParametersSet() {
		RCCookie cookie = new RCCookie("name", "john", 3600, "localhost", "/");
		cookie.setHttpOnly(true);
		assertEquals(
				cookie.getName() + "=\"" + cookie.getValue() + "\"; Domain="
						+ cookie.getDomain() + "; Path=" + cookie.getPath()
						+ "; Max-Age=" + cookie.getMaxAge() + "; HttpOnly",
						cookie.toString());
	}

}
