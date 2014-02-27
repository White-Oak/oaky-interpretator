/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

import java.util.Vector;

/**
 *
 * @author LPzhelud
 */
public class StringsWorker {

    public static String[] splitBy(String str, char splitter) {
	if (str == null || str.trim().length() == 0) {
	    throw new IllegalArgumentException("input string is invalid");
	}
	if (str.indexOf(splitter) == -1) {
	    return new String[]{str.trim()};
	}
	str = str.trim();
	Vector v = new Vector();
	int lastindex = 0;
	char c = 0;
	for (int i = 0; i < str.length(); i++) {
	    c = str.charAt(i);
	    if (c == splitter) {
		String temp = str.substring(lastindex, i).trim();
		if (temp.length() > 0) {
//		    System.out.println(temp);
		    v.addElement(temp);
		}
		lastindex = i + 1;
	    }
	}
	v.addElement(str.substring(lastindex).trim());
	String[] stra = new String[v.size()];
	v.copyInto(stra);
	return stra;
    }

    public static String replace(String str, char old, char newc) {
	String[] s = splitBy(str, old);
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < s.length; i++) {
	    String string = s[i];
	    sb.append(s[i]);
	    if (i != s.length - 1) {
		sb.append(newc);
	    }
	}
	return sb.toString();
    }

    public static int count(String str, String searchingfor) {
	int count = 0;
	int last = 0;
	while (str.indexOf(searchingfor, last) != -1) {
	    last = str.indexOf(searchingfor, last) + 1;
	    count++;
	}
	return count;
    }

    public static String[] getLines(String s) {
	Vector v = new Vector();
	int previouscut = 0;
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (c == 13) {
		v.addElement(s.substring(previouscut, i));
		i++;
		previouscut = i + 1;
		continue;
	    }
	    if (c == 10) {
		v.addElement(s.substring(previouscut, i));
		previouscut = i + 1;
		continue;
	    }
	}
	if (previouscut < s.length() && previouscut != -1) {
	    v.addElement(s.substring(previouscut));//Adding last line
	}
	String[] str = new String[v.size()];
	v.copyInto(str);
	return str;
    }

    public static String castToLinuxWayLF(String s) {
	String[] ret = getLines(s);
	StringBuilder res = new StringBuilder();
	for (int i = 0; i < ret.length; i++) {
	    String string = ret[i];
	    res.append(string).append('\n');
	}
	return res.toString();
    }
}
