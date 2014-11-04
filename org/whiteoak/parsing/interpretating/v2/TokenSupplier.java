package org.whiteoak.parsing.interpretating.v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.*;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
class TokenSupplier {

    private final BufferedReader reader;
    private final Queue<Token> tokens = new LinkedList<>();

    public Token get() {

	if (!tokens.isEmpty()) {
	    return tokens.poll();
	}
	try {
	    //processes an input and puts tokens right into the queue returning nothing
	    queueNextTokens();
	} catch (IOException ex) {
	    Logger.getLogger(TokenSupplier.class.getName()).log(Level.SEVERE, null, ex);
	}
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

    private void queueNextTokens() throws IOException {
	String readLine = reader.readLine();
	GrowingToken currentToken = null;
	if (readLine != null) {
	    readLine = readLine.trim();
	    for (int i = 0; i < readLine.length(); i++) {
		char c = readLine.charAt(i);
		if (!checkIfComment(c, readLine, i, currentToken)) {
		    //if it is a start of a line or we've just been finished with a previous token
		    if (currentToken == null) {
			currentToken = new GrowingToken(detect(c, null));
			currentToken.grow(c);
		    } else {
			currentToken.grow(c);
		    }
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

    private boolean checkIfComment(char c, String readLine, int i, GrowingToken currentToken) {
	//if suddenly '/' character is met
	if (c == '/') {
	    //if it is '//' suddenly
	    if (isComment(readLine, i)) {
		//get the fuck out of here the rest is a comment anyway
		if (currentToken != null) {
		    //don't forget about unfinished token though
		    tokens.add(currentToken.finish());
		}
		return true;
	    } else {
		addErrorToken("Single '/' was met. Is it a comment?..");
	    }
	}
	return false;
    }

    private void addErrorToken(String message) {
	Token errorToken = getErrorToken(message);
	tokens.add(errorToken);
	tokens.add(null);
    }

    private Token getErrorToken(String message) {
	return new Token(Token.Type.ERROR, message);
    }

    private boolean isComment(String readLine, int i) {
	return i != readLine.length() - 1 && readLine.charAt(i + 1) == '/';
    }

    private Token.Type detect(char c, GrowingToken currentToken) {
	if (Character.isAlphabetic(c)) {
	    return Token.Type.IDENT;
	}
	if (Character.isDigit(c)) {
	    if (currentToken != null && currentToken.getType() == Token.Type.IDENT) {
		return Token.Type.IDENT;
	    } else {
		return Token.Type.NUMBER;
	    }
	} else {
	    return Token.Type.OTHER;
	}
    }
}
