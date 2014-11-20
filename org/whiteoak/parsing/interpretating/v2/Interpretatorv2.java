package org.whiteoak.parsing.interpretating.v2;

import java.io.*;
import org.whiteoak.parsing.interpretating.exceptions.BadScriptException;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;

/**
 *
 * @author White Oak
 */
public class Interpretatorv2 {

    public static void parse(InputStream is) throws IOException, InterpretatingException {
	BufferedReader r = new BufferedReader(new InputStreamReader(is));
	TokenSupplier tokenSupplier = new TokenSupplier(r);
	proc(tokenSupplier);
    }

    private static void proc(TokenSupplier supplier) throws BadScriptException, IOException {
	Token get;
	while ((get = supplier.get()) != null) {
	    System.out.println(get.getType() + " token: " + get.getValue());
	}
    }

}
