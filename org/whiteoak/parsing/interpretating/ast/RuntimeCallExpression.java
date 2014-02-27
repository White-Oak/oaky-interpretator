/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;

/**
 *
 * @author LPzhelud
 */
public class RuntimeCallExpression extends CallExpression {

    public RuntimeCallExpression(String value) {
	super(value);
    }

    @Override
    public DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) {
	Value temp;
	if ((temp = definedHolders.getValueHolders().get(getName())) != null) {
	    return temp;
	} else {
	    return new PreDefinedConstant(getName());
	}
    }
}
