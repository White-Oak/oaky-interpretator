package org.whiteoak.parsing.interpretating.v2;

import lombok.Data;

/**
 *
 * @author White Oak
 */
@Data
class Token {

    private final Type type;
    private final String value;

    enum Type {

	IDENT, NUMBER, OTHER, ERROR, OPENING_BRACKET,
	CLOSING_BRACKET, COMMENT, WHITESPACE, QUOTAGE
    }
}
