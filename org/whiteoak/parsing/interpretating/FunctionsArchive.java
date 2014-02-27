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
public final class FunctionsArchive {

    private HashMap<String, int[]> hashtable = new HashMap<>();

    public FunctionsArchive(Function[] fs) {
	String prefix = null;
	for (int i = 0, type = 0, subtype = 0; i < fs.length; i++, type++) {
	    Function f = fs[i];
	    String name = f.getRealName();
	    if (prefix != null) {
		String temp = name.substring(0, name.indexOf('_'));
		if (!prefix.equals(temp)) {
		    subtype++;
		    type = 0;
		    prefix = temp;
		} else if (name.equals(fs[i - 1].getRealName())) {
		    type--;
		    continue;
		}
	    } else {
		prefix = name.substring(0, name.indexOf('_'));
	    }
	    put(name, new int[]{subtype, type});
	}
    }

    private synchronized Object put(String key, int[] value) {
	return hashtable.put(key, value);
    }

    public synchronized int[] get(String key) {
	return (int[]) hashtable.get(key);
    }

    public synchronized void clear() {
	hashtable.clear();
    }
}
