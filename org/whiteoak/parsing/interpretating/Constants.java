/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import org.whiteoak.parsing.interpretating.ast.Constant;
import java.util.HashMap;

/**
 *
 * @author LPzhelud
 */
public class Constants {

    private final HashMap<String, Constant> hashtable = new HashMap<>();

    public synchronized void addConstant(Constant value) {
	hashtable.put(value.getName(), value);
    }

    public synchronized Constant get(String key) {
	return hashtable.get(key);
    }

    public synchronized boolean containsConstant(String key) {
	return hashtable.containsKey(key);
    }

    public void setReturn(String value) {
	addConstant(new Constant("return", value));
    }
}
