/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;
import org.whiteoak.parsing.interpretating.exceptions.BadCalculateableException;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;

/**
 *
 * @author LPzhelud
 */
public class CalculateableValue extends CallExpression {

    public CallExpression value1, value2;
    public Type type;

    public CalculateableValue(CallExpression value1, Type type, CallExpression value2) {
	super(null);
	this.value1 = value1;
	this.value2 = value2;
	this.type = type;
    }

    /**
     * 1 - +
     * 2 - -
     * 3 - *
     * 4 - /
     *
     * @return
     */
    public Type getType() {
	return type;
    }

    @Override
    public DefinitiveValue getDefinitiveValue(DefinedHolders definedHolders) throws InterpretatingException {
	Value v1, v2;
	if (value1 instanceof CallFunction) {
	    ((Function) value1.getDefinitiveValue(definedHolders)).runTry(definedHolders,
		    ((CallFunction) value1).getValues());
	    v1 = definedHolders.getReturn();
	} else {
	    v1 = (Value) value1.getDefinitiveValue(definedHolders);
	}
	if (value2 instanceof CallFunction) {
	    ((Function) value2.getDefinitiveValue(definedHolders)).runTry(definedHolders,
		    ((CallFunction) value2).getValues());
	    v2 = definedHolders.getReturn();
	} else {
	    v2 = (Value) value2.getDefinitiveValue(definedHolders);
	}
	int i1, i2;
	try {
	    i1 = Integer.parseInt(v1.getValue());
	    i2 = Integer.parseInt(v2.getValue());
	} catch (NumberFormatException e) {
	    throw new BadCalculateableException("Bad:\n" + e.getMessage());
	}
	int result;
	switch (type) {
	    case ADD:
		result = i1 + i2;
		break;
	    case SUBSTRACT:
		result = i1 - i2;
		break;
	    case MULTIPLY:
		result = i1 * i2;
		break;
	    case DIVIDE:
		result = i1 / i2;
		break;
	    default:
		result = 0;
	}
	return new PreDefinedConstant(String.valueOf(result));
    }

    public enum Type {

	ADD, SUBSTRACT, MULTIPLY, DIVIDE
    }
}
