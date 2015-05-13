package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.EmptyStackException;

import java.lang.StringBuilder;

/**
 * Parses a custom type script, parsing results are located in mainNode.
 * Throws SmartScriptParserException if there is any problem anywhere.
 * 
 * @author Erik Banek
 */
public class SmartScriptParser {
	/**
	 * Head node that contains all other nodes, id est all document information
	 * after parsing.
	 */
	private DocumentNode mainNode;
	
	/** String of document to parse.*/
	private String docString;
	/**
	 * Parser goes iteratively through sequence, and generates parsing results.
	 */
	private char[] docArray;
	
	/** Makes generating graph of nodes easier. */
	private ObjectStack Stack;
	
	/**
	 * Constructs a read-only parser, that will contain all the 
	 * parsed information in the mainNode field.
	 * 
	 * @param docBody String to be parsed.
	 */
	public SmartScriptParser(String docBody) {
		parseIt(docBody);
	}
	
	/**
	 * Main body of parser. Goes through sequence of chars in string
	 * and separates TextNodes and tags. 
	 * Parses the String in wanted form, initializes mainNode, Stack and charSequence.
	 * 
	 * @param docBody
	 * @throws SmartScriptParserException
	 */
	private void parseIt(String docBody) {
		this.docArray = docBody.toCharArray();
		this.Stack = new ObjectStack();
		this.mainNode = new DocumentNode();
		this.docString = docBody;
		this.Stack.push(this.mainNode);
		
		boolean inTag = false;
		boolean inString = false;
		int lastEventIndex = 0;
		
		int length = docArray.length;
		for (int i=0; i<length; i++) {
			if (!inTag) {
				//escape if needed (actually skip)
				if (this.docArray[i] == '\\') {
					if (i != length-1 &&
							(this.docArray[i+1] == '{' || this.docArray[i+1] == '\\')) {

						if (i == length-2) {
							if (this.Stack.isEmpty()) {
								throw new SmartScriptParserException();
							}
							Node nodeOnStack = (Node) this.Stack.peek();
							nodeOnStack.addChildNode(
									stringToTextNode(docString.substring(lastEventIndex)));
						} 
						i++;
						continue;
					}
				}//if tag is opened, put TextNode on stack, and update lastEvend and get into tag
				if (i == this.docArray.length-1 ||
						(this.docArray[i] == '{' && this.docArray[i+1] == '$')) {
					if (this.Stack.isEmpty()) {
						throw new SmartScriptParserException();
					}
					Node nodeOnStack = (Node) this.Stack.peek();
					nodeOnStack.addChildNode(
							stringToTextNode(docString.substring(lastEventIndex, i)));
					lastEventIndex = i;
					inTag = true;
				}
			} //in tag
			else {
				if (i == this.docArray.length-1) {
					throw new SmartScriptParserException();
				}
				//index is inside string inside tag
				if (inString) {
					if (this.docArray[i] == '\\' && 
							(this.docArray[i+1] == '\"' || this.docArray[i+1] == '\\')) {
						i++;
						continue;
					}
					if (this.docArray[i] == '\"') {
						inString = false;
						continue;
					}
				}//index is not inside string
				else {
					if (this.docArray[i] == '$' && this.docArray[i+1] == '}') {
						String tagString = this.docString.substring(lastEventIndex, i+2);
						lastEventIndex = i+2;
						String tagName = null;
						tagName=getTagName(tagString);
						tagName = tagName.trim();
						
						if (tagName.toUpperCase().startsWith("END")) {
							try {
								Stack.pop();
							} catch (EmptyStackException e) {
								throw new SmartScriptParserException();
							}
						} else {
							if (tagName.toUpperCase().startsWith("FOR")) {
								Node nodeOnStack = (Node) this.Stack.peek();
								ForLoopNode fNode = tagStringToForNode(tagString);
								nodeOnStack.addChildNode(fNode);
								this.Stack.push(fNode);
							} else if (tagName.startsWith("=")) {
								Node nodeOnStack = (Node) this.Stack.peek();
								nodeOnStack.addChildNode(
										tagStringToEchoNode(tagString));
							} else {
								//DEBUG
								/*
								System.out.println(tagName);
								System.out.println("FOR");
								System.out.println("FOR" == tagName);*/
								throw new SmartScriptParserException();
							}
						}
						inTag = false;
						i++;
						continue;
					}
				}
			}
		}
	}
	
	/**
	 * Gets the mainNode that contains parsed string information that was
	 * passed as an argument to the constructor.
	 * 
	 * @return mainNode containing the parsed document information.
	 * @throws NullPointerException if parsing wasn't successful, but the
	 * 								mainNode is wanted.
	 */
	public DocumentNode getDocumentNode() {
		if (mainNode == null) {
			throw new NullPointerException();
		}
		return mainNode;
	}
	
	/**
	 * Tests if char can be in variable.
	 * 
	 * @param c char to be tested.
	 * @return true if char can be in variable.
	 */
	private boolean isCharValidForVariable(char c) {
		return (Character.isLetterOrDigit(c) || c == '_');
	}
	
	/**
	 * Gets tag name from String containing a tag.
	 * Tag is expected to look like: {$...$}.
	 * 
	 * @param tagString
	 */
	private String getTagName(String tagString) {
		char[] array = tagString.toCharArray();
		boolean found = false;
		int positionFound = -1;
		
		for (int i=2; i<array.length; i++) {
			if (found && !isCharValidForVariable(array[i])) {
				String tagName = tagString.substring(positionFound, i);
				if (tagName.toUpperCase() == "END") {
					if (!isEndTagValid(tagString)) {
						throw new SmartScriptParserException();
					}
				}
				return tagName;
			}
			if (!found && Character.isLetter(array[i])) {
				positionFound = i;
				found = true;
			}
			if (!found && array[i]=='=') {
				return "=";
			}
			if (!found && array[i]!=' ') {
				throw new SmartScriptParserException();
			}
		}
		throw new SmartScriptParserException();
	}
	
	/**
	 * Checks whether a tag with name END is correct.
	 * 
	 * @param endTagString
	 * @return true if END tag is valid.
	 */
	private boolean isEndTagValid(String endTagString) {
		String newString = endTagString.substring(2, endTagString.length()-2);
		if (newString.trim().length() == 3) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Makes a ForLoopNode representing the tag contained in forTagString.
	 * Presumes that all tokens in tag are separated by space, and that strings
	 * do not have spaces in them.
	 * 
	 * @param forTagString
	 * @return ForLoopNode that represents forTagString.
	 * @throws SmartScriptParserException if tag is not valid.
	 */
	private ForLoopNode tagStringToForNode(String forTagString) {
		//System.out.println(forTagString);
		String newString = forTagString.substring(2, forTagString.length()-2);
		newString = newString.trim();
		newString = newString.substring(3).trim();
		String[] strArray = newString.split("[\\s]");
		Token[] tokenArray = convertStrArrToTokenArr(strArray);
		
		if (tokenArray.length == 3) {
			return new ForLoopNode((TokenVariable)tokenArray[0], tokenArray[1],
					tokenArray[2]);
		} else if (tokenArray.length == 4) {
			return new ForLoopNode((TokenVariable)tokenArray[0], tokenArray[1],
					tokenArray[2], tokenArray[3]);
		} else {
			throw new SmartScriptParserException();
		}
	}
	
	/**
	 * Makes an EchoNode representing the tag contained in echoTagString.
	 * Presumes that all tokens in tag are separated by space, and that strings
	 * do not have spaces in them.
	 * 
	 * @param echoTagString
	 * @return EchoNode that represents echoTagString.
	 * @throws 
	 */
	private EchoNode tagStringToEchoNode(String echoTagString) {
		//System.out.println(echoTagString);
		String newString = echoTagString.substring(2, echoTagString.length()-2);
		newString = newString.trim();
		newString = newString.substring(1).trim();
		String[] strArray = newString.split("[\\s]");
		Token[] tokenArray = convertStrArrToTokenArr(strArray);
		return new EchoNode(tokenArray);
	}
	
	/**
	 * Converts each string in array to a token.
	 * 
	 * @param strArray
	 * @return
	 */
	private Token[] convertStrArrToTokenArr(String[] strArray) {
		Token[] arrayTok = new Token[strArray.length];
		for (int i=0; i<strArray.length; i++) {
			arrayTok[i] = convertStringToToken(strArray[i]); 
		}
		return arrayTok;
	}
	
	/**
	 * Handles converting and checking which type of token is string.
	 * 
	 * @param str to be converted.
	 * @return token that is in string.
	 */
	private Token convertStringToToken(String str) {
		char[] array = str.toCharArray();
		if (str.startsWith("\"")) {
			if (str.endsWith("\"")) {
				return stringToTokenString(str);
			} else {
				throw new SmartScriptParserException();
			}
		}
		if (str.startsWith("@")) {
			return stringToTokenFunction(str);
		}
		if (str.equals("+") || str.equals("-") || str.equals("/") || str.equals("*")) {
			return stringToTokenOperator(str);
		}
		if (Character.isLetter(array[0])) {
			return stringToTokenVariable(str);
		}
		if (Character.isDigit(array[0])) {
			if (str.lastIndexOf('.') != -1) {
				return stringToTokenDouble(str);
			}
			else {
				return stringToTokenInteger(str);
			}
		}
		System.out.println(str);
		throw new SmartScriptParserException();
	}
	
	/**
	 * Converts text for TextNode to form that acknowledges escaping.
	 * 
	 * @param toConvert
	 * @return
	 */
	private TextNode stringToTextNode(String toConvert) {
		//System.out.println(toConvert);
		char[] arrayToConvert = toConvert.toCharArray();
		StringBuilder convertBuilder = new StringBuilder();

		for (int i=0; i<arrayToConvert.length; i++) {
			if (arrayToConvert[i] == '\\' && i != (arrayToConvert.length-1)) {
				if (arrayToConvert[i+1] == '\\' || arrayToConvert[i+1] == '{') {
					//append that which is escaped, and skip
					convertBuilder.append(arrayToConvert[i+1]);
					i++;
					continue;
				}
			}
			//if there is no escape, standard append
			convertBuilder.append(arrayToConvert[i]);
		}
		return new TextNode(convertBuilder.toString());
	}
	
	/**
	 * Converts string in tag to TokenString. Escaping is handled and converted here.
	 * 
	 * @param toConvert
	 * @return
	 */
	private TokenString stringToTokenString(String toConvert) {
		//get rid of "", and convert to array
		char[] arrayToConvert = 
				toConvert.substring(1, toConvert.length()-1).toCharArray();
		StringBuilder convertBuilder = new StringBuilder();

		for (int i=0; i<arrayToConvert.length; i++) {
			if (arrayToConvert[i] == '\\' && i != (arrayToConvert.length-1)) {
				if (arrayToConvert[i+1] == '\\' || 
						arrayToConvert[i+1] == 'n'  ||
						arrayToConvert[i+1] == '\"' ||
						arrayToConvert[i+1] == 't'  ||
						arrayToConvert[i+1] == 'r') {

					char toAppend = 0;
					switch(arrayToConvert[i+1]) {
						case '\\' : toAppend = '\\';
									break;
						case '\"' : toAppend = '\"';
								    break;
						case 'n' : toAppend = '\n';
						   		   break;
						case 't' : toAppend = '\t';
								   break;
						case 'r' : toAppend = '\r';
								   break;
					}
					
					convertBuilder.append(toAppend);
					i++;
					continue;
				}
			}
			//if there is no escape, standard append
			convertBuilder.append(arrayToConvert[i]);
		}
		return new TokenString(convertBuilder.toString());
	}
	
	/** 
	 * Converts string of double to TokenConstantDouble.
	 * 
	 * @param toConvert
	 * @return
	 */
	private TokenConstantDouble stringToTokenDouble(String toConvert) {
		double doubleForToken=0.0;
		try {
			doubleForToken = Double.parseDouble(toConvert);
		} catch (NumberFormatException e) {
			throw new SmartScriptParserException();
		}
		
		return new TokenConstantDouble(doubleForToken);
	}
	
	/** 
	 * Converts string of integer to TokenConstantInteger.
	 * 
	 * @param toConvert
	 * @return
	 */
	private TokenConstantInteger stringToTokenInteger(String toConvert) {
		//System.out.println(toConvert);
		int intForToken=0;
		try {
			intForToken = Integer.parseInt(toConvert);
		} catch (NumberFormatException e) {
			throw new SmartScriptParserException();
		}
		
		return new TokenConstantInteger(intForToken);
	}
	
	/** 
	 * Converts string of variable to TokenVariable.
	 * 
	 * @param toConvert
	 * @return
	 */
	private TokenVariable stringToTokenVariable(String toConvert) {
		return new TokenVariable(toConvert);
	}
	
	/** 
	 * Converts string of function TokenFunction (without @).
	 *
	 * @param toConvert
	 * @return
	 */
	private TokenFunction stringToTokenFunction(String toConvert) {
		return new TokenFunction(toConvert.substring(1));
	}
	
	/** 
	 * Converts string of operator to TokenOperator. 
	 * 
	 * @param toConvert
	 * @return
	 */
	private TokenOperator stringToTokenOperator(String toConvert) {
		return new TokenOperator(toConvert);
	}
}
