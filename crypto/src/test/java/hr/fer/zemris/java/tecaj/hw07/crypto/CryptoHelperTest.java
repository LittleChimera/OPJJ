package hr.fer.zemris.java.tecaj.hw07.crypto;

import static org.junit.Assert.*;

import org.junit.Test;

public class CryptoHelperTest {

	@Test
	public void hextobyteTest() {
		String keyText = "0d3d4424461e22a458c6c716395f07dd9cea2180a996e78349985eda78e8b800";
		byte[] byteText = CryptoHelper.hextobyte(keyText);
	    StringBuilder sb = new StringBuilder();
	    for (byte b : byteText) {
	        sb.append(String.format("%02X", b));
	    }
		assertEquals(keyText, sb.toString().toLowerCase());
	}
	
	@Test
	public void hextobyteTestUnevenNumberOfHexDigits() {
		String keyText = "d3d4424461e22a458c6c716395f07dd9cea2180a996e78349985eda78e8b800";
		byte[] byteText = CryptoHelper.hextobyte(keyText);
	    StringBuilder sb = new StringBuilder();
	    for (byte b : byteText) {
	        sb.append(String.format("%02X", b));
	    }
		assertEquals("0" + keyText, sb.toString().toLowerCase());
	}
	
	@Test(expected=NumberFormatException.class)
	public void nonHexString() {
		String keyText = "d3d4424461e22a458c6c716395f07dd9ceL2180a996e78349985eda78e8b800";
		CryptoHelper.hextobyte(keyText);
	}

}
