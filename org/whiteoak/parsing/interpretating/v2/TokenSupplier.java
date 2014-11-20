package org.whiteoak.parsing.interpretating.v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import lombok.*;
import org.whiteoak.parsing.interpretating.exceptions.BadScriptException;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
class TokenSupplier {

    private final BufferedReader reader;
    private final Queue<Token> tokens = new LinkedList<>();

    public Token get() throws BadScriptException, IOException {

	if (!tokens.isEmpty()) {
	    return tokens.poll();
	}
	//processes an input and puts tokens right into the queue returning nothing
	queueNextTokens();
	//well, if zero tokens were added to queue
	//then there is no more code to be read 
	if (tokens.isEmpty()) {
	    return null;
	} else {
	    //in case if processed several tokens in one turn
	    while (!tokens.isEmpty()) {
		return tokens.poll();
	    }
	}
	return null;
    }

    private void queueNextTokens() throws IOException, BadScriptException {
	String readLine = reader.readLine();
	GrowingToken currentToken = null;
	if (readLine != null) {
	    readLine = readLine.trim();
	    for (int i = 0; i < readLine.length(); i++) {
		char c = readLine.charAt(i);
		char lookahead = i + 1 < readLine.length() ? readLine.charAt(i + 1) : ' ';
		if (checkIfComment(c, lookahead, readLine)) {
		    //get the fuck out of here the rest is a comment anyway
		    if (currentToken != null) {
			//don't forget about unfinished token though
			tokens.add(currentToken.finish());
		    }
		    addNewToken(Token.Type.COMMENT, readLine.substring(i + 2));
		    return;
		} else {
		    //if it is a start of a line or we've just been finished with a previous token
		    if (currentToken == null) {
			currentToken = startNewToken(c);
			if (currentToken == null) {
			    continue;
			}
		    }
		    currentToken.grow(c, lookahead);

		    if (currentToken.isGrown()) {
			tokens.add(currentToken.finish());
			currentToken = null;
		    }
		}
	    }
	    if (currentToken != null) {
		tokens.add(currentToken.finish());
	    }
	}
    }

    private GrowingToken startNewToken(char c) {
	final Token.Type detect = detect(c, null);
	if (detect != Token.Type.WHITESPACE) {
	    return new GrowingToken(detect);
	}
	return null;
    }

    private void addNewToken(Token.Type type, String body) {
	tokens.add(new Token(type, body));
    }

    private boolean checkIfComment(char c, char lookahead, String line) throws BadScriptException {
	//if suddenly '/' character is met
	if (c == '/') {
	    //if it is '//' suddenly
	    if (lookahead == '/') {
		return true;
	    } else {
//		addErrorToken("Single '/' was met. Is it a comment?.. Line:\n" + line);
		throw new BadScriptException("Single '/' was met. Is it a comment?.. Line:\n" + line);
	    }
	}
	return false;
    }

    @Deprecated
    private void addErrorToken(String message) {
	Token errorToken = getErrorToken(message);
	tokens.add(errorToken);
	tokens.add(null);
    }

    private Token getErrorToken(String message) {
	return new Token(Token.Type.ERROR, message);
    }

    private Token.Type detect(char c, GrowingToken currentToken) {
	if (Character.isWhitespace(c)) {
	    return Token.Type.WHITESPACE;
	} else if (Character.isAlphabetic(c)) {
	    return Token.Type.IDENT;
	} else if (Character.isDigit(c)) {
	    if (currentToken != null && currentToken.getType() == Token.Type.IDENT) {
		return Token.Type.IDENT;
	    } else {
		return Token.Type.NUMBER;
	    }
	} else {
	    switch (c) {
		case '(':
		    return Token.Type.OPENING_BRACKET;
		case ')':
		    return Token.Type.CLOSING_BRACKET;
		case '"':
		    return Token.Type.QUOTAGE;
		default:
		    return Token.Type.OTHER;
	    }
	}

    }
}
