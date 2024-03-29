package hr.fer.zemris.java.custom.scripting.exec.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstration of a {@link SmartScriptEngine}.
 * @author Luka Skugor
 *
 */
public class SmartScriptEngineDemo {

	/**
	 * Called on program start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			runDemo1();
			runDemo2();
			runDemo3();
			runDemo4();
		} catch (IOException e) {
			System.err.println("Demo script not found " + e);
			return;
		}
	}

	/**
	 * First demo.
	 * @throws IOException if demo script doesn't exist.
	 */
	private static void runDemo1() throws IOException {
		String documentBody = readFromDisk("scripts/osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}

	/**
	 * Second demo.
	 * @throws IOException if demo script doesn't exist.
	 */
	private static void runDemo2() throws IOException {
		String documentBody = readFromDisk("scripts/zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}

	/**
	 * Third demo.
	 * @throws IOException if demo script doesn't exist.
	 */
	private static void runDemo3() throws IOException {
		String documentBody = readFromDisk("scripts/brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters,
				persistentParameters, cookies);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc)
		.execute();
		System.out.println("Vrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));
	}

	/**
	 * Fourth demo.
	 * @throws IOException if demo script doesn't exist.
	 */
	private static void runDemo4() throws IOException {
		String documentBody = readFromDisk("scripts/fibonacci.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}

	/**
	 * Reads a scripts from disk as string using UTF-8 encoding.
	 * @param string script path
	 * @return read string
	 * @throws IOException if reading error occurs
	 */
	private static String readFromDisk(String string) throws IOException {
		return new String(Files.readAllBytes(Paths.get(string)),
				Charset.forName("UTF-8"));
	}
}
