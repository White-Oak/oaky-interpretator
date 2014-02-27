/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;

/**
 *
 * @author LPzhelud
 */
public abstract class CallExpression extends Expression {

    private final String name;

    public String getName() {
	return name;
    }

    public abstract DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) throws InterpretatingException;

    public CallExpression(String name) {
	this.name = name;
    }

}
