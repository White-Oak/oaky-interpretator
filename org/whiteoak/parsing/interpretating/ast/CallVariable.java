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
public class CallVariable extends CallExpression{

    public CallVariable(String name) {
	super(name);
    }

    @Override
    public DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) {
	return definedHolders.getValueHolders().get(getName());
    }
    
}
