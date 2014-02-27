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
public class CallPreDefinedConstant extends CallVariable {

    private final PreDefinedConstant constant;

    public CallPreDefinedConstant(PreDefinedConstant definedConstant) {
	super(null);
	constant = definedConstant;
    }

    public PreDefinedConstant getConstant() {
	return constant;
    }

    @Override
    public DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) {
	return getConstant();
    }
}
