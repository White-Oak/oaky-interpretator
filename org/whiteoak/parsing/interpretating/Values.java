/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import java.util.ArrayList;

/**
 * The class that represents the aray of values passed into accept/write methods
 *
 * @author LPzhelud
 */
public class Values {

    private final String[] strings;
    private boolean quoted[];

    public Values(String[] strings) {
	this.strings = strings;
	quoted = new boolean[strings.length];
	check();
    }

    public Values(ArrayList<String> v) {
	this(v.toArray(new String[v.size()]));
    }

    /**
     * Recheck the inner array. Trims, excludes quotes.
     */
    public final void check() {
	for (int i = 0; i < strings.length; i++) {
	    String string = strings[i];
	    string = string.trim();
	    if (string.length() == 0) {
		continue;
	    }
	    int cutleft = 0, cutright = string.length();//Not to cut many items
	    if (string.charAt(0) == '"') {
		cutleft = 1;
		quoted[i] = true;
	    } else {
		strings[i] = string;
		continue;
	    }
	    if (string.charAt(string.length() - 1) == '"') {
		cutright = string.length() - 1;
	    } else {
		throw new Error("INVALID QUOTATION");
	    }
	    strings[i] = string.substring(cutleft, cutright);
	}
    }

    /**
     * @return the values of method
     */
    public String[] getStrings() {
	return strings;
    }

    /**
     * @return the size of array
     */
    public int size() {
	return strings.length;
    }

    public String get(int i) {
	return strings[i];
    }

    public String getQuoted(int i) {
	if (strings[i].indexOf(',') != -1) {
	    return '"' + strings[i] + '"';
	}
	return strings[i];
    }

    //@working strange method
    public Values quoteInstance() {
	String[] ss = new String[strings.length];
	for (int i = 0; i < strings.length; i++) {
	    String string = strings[i];
	    ss[i] = '"' + string + '"';
	}
	return this;
    }

    public void set(int i, String s) {
	strings[i] = s;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder(strings[0]);
	for (int i = 1; i < strings.length; i++) {
	    String string = strings[i];
	    sb.append(", ").append(string);
	}
	return sb.toString();
    }

    public boolean wasQuoted(int i) {
	return quoted[i];
    }
}
