package hr.fer.java.tecaj.hw15.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class AuthenticationUtilityTest {

	@Test
	public void testEncryptPassword() {
		assertEquals("1cf4c502ddd89b918c4bfefea76dadd590693b48".toUpperCase(),
				AuthenticationUtility.encryptPassword("mypassword123"));
	}

}
