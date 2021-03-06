/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author LPzhelud
 */
public class File {

    private final byte[] is;
    private final String name;

    public static byte[] getContents(InputStream is) {
	try {
	    ByteVector bv = new ByteVector();
//	    ByteArrayOutputStream baos=new ByteArrayOutputStream();
	    int c;
	    while ((c = is.read()) != -1) {
		bv.addElement(c);
	    }
	    return bv.getBytes();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	return null;
    }

    public File(byte[] is, String name) {
	this.is = is;
	this.name = name;
    }

    public byte[] getIs() {
	return is;
    }

    public String getName() {
	return name;
    }
}
