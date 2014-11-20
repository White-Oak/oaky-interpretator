package org.whiteoak.parsing.interpretating.v2;

import lombok.Getter;

/**
 *
 * @author White Oak
 */
class GrowingToken {

    private final Token.Type type;
    private final StringBuilder value = new StringBuilder();
    @Getter private boolean grown = false;

    private Token.Type detect(char c) {
	if (Character.isAlphabetic(c)) {
	    return Token.Type.IDENT;
	}
	if (Character.isDigit(c)) {
	    if (getType() == Token.Type.IDENT) {
		return Token.Type.IDENT;
	    } else {
		return Token.Type.NUMBER;
	    }
	} else {
	    return Token.Type.OTHER;
	}
    }

    public GrowingToken(Token.Type type) {
	this.type = type;
    }

    public void grow(char c, char lookahead) {
	if (grown) {
	    return;
	}
//	Token.Type detect = detect(c);
	if (type == Token.Type.QUOTAGE) {
	    if (c != '"') {
		value.append(c);
	    } else if (value.length() != 0) {
		grown = true;
	    }
	    return;
	}
	value.append(c);
	if (Character.isWhitespace(lookahead)) {
	    grown = true;
	}
	if (!Character.isAlphabetic(lookahead)) {
	    grown = true;
	}
    }

    public Token.Type getType() {
	return type;
    }

    public Token finish() {
	return new Token(type, value.toString());
    }

}
