/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;
import org.whiteoak.parsing.interpretating.Values;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;
import org.whiteoak.parsing.interpretating.exceptions.InvalidCallExcepion;

/**
 *
 * @author LPzhelud
 */
public class CallFunction extends CallExpression {

    private Values values;

    public CallFunction(String name, Values v) {
	super(v != null ? name + "_" + v.size() : name);
	values = v;
    }

    public Values getValues() {
	return values;
    }

    public void setValues(Values values) {
	this.values = values;
    }

    @Override
    public DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) throws InterpretatingException {
	DefinitiveValue dv = definedHolders.getFunctions().get(getName());
	if (dv == null) {
	    throw new InvalidCallExcepion("no function with a name " + getName());
	}
	return dv;
    }
}
