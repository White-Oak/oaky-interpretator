package org.whiteoak.parsing.interpretating.v2;

import java.io.*;
import java.util.*;

/**
 *
 * @author White Oak
 */
public class Interpretatorv2 {

    public static void parse(InputStream is) throws IOException {
	BufferedReader r = new BufferedReader(new InputStreamReader(is));
	TokenSupplier tokenSupplier = new TokenSupplier(r);
	proc(tokenSupplier);
    }

    private static void proc(TokenSupplier supplier) {
	List<Token> tokens = new LinkedList();
	Token get;
	while ((get = supplier.get()) != null) {
	    tokens.add(get);
	}
	tokens.stream().forEach((token) -> {
	    System.out.println(token.getType() + " token: " + token.getValue());
	});
    }

}
