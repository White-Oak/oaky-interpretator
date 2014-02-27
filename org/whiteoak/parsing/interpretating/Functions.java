/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import org.whiteoak.parsing.interpretating.ast.Function;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author LPzhelud
 */
public class Functions {

    private final HashMap<String, Function> hashtable = new HashMap<>();

    public synchronized void addFunction(Function value) {
	hashtable.put(value.getName(), value);
    }

    public synchronized Function get(String key) {
	return hashtable.get(key);
    }

    public synchronized boolean containsFunction(String key) {
	return hashtable.containsKey(key);
    }
}
