package hr.fer.zemris.java.hw2;

import hr.fer.zemris.java.custom.scripting.nodes.*;

import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


/**
 * Razred <code>SmartScriptTester</code> je razred koji parsita ulazni string, stavlja ga na stog, i po potrebi vraća
 * orginalni string iz parsiranih čvorova.
 * Kao args[0] prima put do text file kojeg je potrebno parsirati
 * Da bi program radio mora dobiti ispravan redoslijed znakova. U slučaju krivog stringa program će uputiti na 
 * pogrešku.<p>
 * Primjer args[0] je "/Users/RadeBebek/VjestinaJava/HW02-0036447892/Examples/doc1.txt"
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class SmartScriptTester {

	/**
	 * Metoda <code>createOriginalDocumentBody</code> stvara novi string iz DocumenNode čvora.
	 * @param document čvor document noda
	 * @return string vraća parsirani string koji se može po potrebi opet parsirati.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public static String createOriginalDocumentBody(Node document) {

		String string = "";

		if (document.getchildren() == null) {
			boolean uvjet= false;
			try {
				String string2=((ForLoopNode) document).getVarijabla().asText();
				string += "{$ ";
				string += "FOR ";
				string +=string2;
				string += " ";
				string += ((ForLoopNode) document).getStartExpression().asText();
				string += " ";
				string += ((ForLoopNode) document).getEndExpression().asText();
				string += " ";
				string += ((ForLoopNode) document).getStepExpression().asText();
				string += " ";
				string += "$}";
				return string;
			} catch (ClassCastException ex) {
				uvjet = true;
			}
			if (uvjet) {
				uvjet = false;
				try {
					Token[] tokens = ((EchoNode) document).getTokeni();
					string = "{$= ";
					for (int i = 0; i < tokens.length; i++) {
						string=string.concat(tokens[i].asText());
					}
					string+=" $}";
					return string;
				} catch (ClassCastException ez) {
					uvjet = true;
				}
			}
			string = ((TextNode) document).getTekst();
			return string;
		} 

		boolean uvjet= true;
		try {
			String string2=((ForLoopNode) document).getVarijabla().asText();
			string += "{$ ";
			string += "FOR ";
			string +=string2;
			string += " ";
			string += ((ForLoopNode) document).getStartExpression().asText();
			string += " ";
			string += ((ForLoopNode) document).getEndExpression().asText();
			string += " ";
			string += ((ForLoopNode) document).getStepExpression().asText();
			string += " ";
			string += "$}";
			uvjet=false;

		} catch (ClassCastException ex) {
			uvjet = true;
		}
		if (uvjet) {
			uvjet = false;
			try {
				Token[] tokens = ((EchoNode) document).getTokeni();
				string = "{$= ";
				for (int i = 0; i < tokens.length; i++) {
					string=string.concat(tokens[i].asText());
				}
				string+=" $}";

			} catch (ClassCastException ez) {
				uvjet = true;
			}
		}
		if(uvjet){
			try{
				string = ((TextNode) document).getTekst();
				uvjet=false;
			}catch(ClassCastException ez){
				uvjet = true;	
			}
		}
		if(uvjet){		
			string += ((DocumentNode) document).getTekst();
		}


		for(int i = 0; i < document.numberOfChildren(); i++){
			string += (createOriginalDocumentBody(document.getChild(i)));
		}
		string += "{$END$}";
		return string;		
	}

	/**
	 * Metoda koja se poziva prilikom pokretanja programa.
	 * @author Rade Bebek
	 * @since 2015-03-14  
	 */
	public static void main(String[] args) throws IOException {

		String filepath=args[0];

		String docBody = new String(Files.readAllBytes(Paths.get(filepath)),StandardCharsets.UTF_8);

		SmartScriptParser parser = null;

		try {

			parser = new SmartScriptParser(docBody);
		} 
		catch(SmartScriptParserException e) {

			System.out.println("Ne moze parsirati dokument!");
			System.exit(-1);
		} 
		catch(Exception e) {

			System.out.println("Provjerite ulazni string, doslo je do nedozvoljenog unosa");
			System.exit(-1);
		}


		DocumentNode document =(DocumentNode)parser.getCvor();
		String originalDocumentBody = createOriginalDocumentBody(document);
		originalDocumentBody=originalDocumentBody.substring(0,originalDocumentBody.length()-7);
		System.out.println(originalDocumentBody);

	}	

}
