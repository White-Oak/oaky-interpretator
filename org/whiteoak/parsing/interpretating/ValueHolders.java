/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import org.whiteoak.parsing.interpretating.ast.Value;

/**
 *
 * @author LPzhelud
 */
public class ValueHolders extends Holders {

    private final Constants constants;

    public Constants getConstants() {
	return constants;
    }

    public Variables getVariables() {
	return variables;
    }
    private final Variables variables;

    public ValueHolders(Constants c, Variables v) {
	this.constants = c;
	this.variables = v;
    }

    public Value get(String key) {
	return constants.containsConstant(key) ? constants.get(key) : variables.get(key);
    }

    public boolean contains(String key) {
	return constants.containsConstant(key) | variables.containsVariable(key);
    }
}
