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

public class SmartScriptEngineDemo {

	
	public static void main(String[] args) {
		runDemo1();
	}
	
	private static void runDemo1() {
		String documentBody;
		try {
			documentBody = readFromDisk("scripts/osnovni.smscr");
		} catch (IOException e) {
			System.err.println("demo1: file not found");
			return;
		}
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
			).execute();
	}

	private static String readFromDisk(String string) throws IOException {
		return new String(Files.readAllBytes(Paths.get(string)), Charset.forName("UTF-8"));
	}
}
