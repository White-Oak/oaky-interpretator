/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import org.whiteoak.parsing.interpretating.ast.Variable;
import java.util.HashMap;

/**
 *
 * @author LPzhelud
 */
public class Variables {

    private final HashMap<String, Variable> hashtable = new HashMap<>();

    public synchronized void addVariable(Variable value) {
	hashtable.put(value.getName(), value);
    }

    public synchronized Variable get(String key) {
	return hashtable.get(key);
    }

    public synchronized boolean containsVariable(String key) {
	return hashtable.containsKey(key);
    }

    public void removeVariable(String key) {
	hashtable.remove(key);
    }
}
