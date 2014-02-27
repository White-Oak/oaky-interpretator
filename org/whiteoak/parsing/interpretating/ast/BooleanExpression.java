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
public class BooleanExpression {

    private final CallExpression value1, value2;
    private final Type type;

    public BooleanExpression(CallExpression first, Type type, CallExpression second) {
	this.value1 = first;
	this.value2 = second;
	this.type = type;
    }

    /**
     * 1 - == 2 - < 3 - > 4 - !=
     *
     * @return
     */
    public Type getType() {
	return type;
    }

    public boolean testTruth(DefinedHolders valueHolders) throws InterpretatingException {
	//Get values
	String value1s, value2s;
	DefinitiveValue v1 = value1.getDefinitiveValue(valueHolders);
	DefinitiveValue v2 = value2.getDefinitiveValue(valueHolders);
	value1s = get(v1, valueHolders, value1);
	value2s = get(v2, valueHolders, value2); //compare
	switch (type) {
	    case EQUALS:
		return value1s.equals(value2s);
	    case LESS:
		return Integer.parseInt(value1s) < Integer.parseInt(value2s);
	    case BIGGER:
		return Integer.parseInt(value1s) > Integer.parseInt(value2s);
	    case NOT_EQUALS:
		return !value1s.equals(value2s);
	    default:
		return false;
	}
    }

    private String get(DefinitiveValue v1, DefinedHolders valueHolders, CallExpression ce) throws InterpretatingException {
	if (v1 instanceof Function) {
	    ((Function) v1).runTry(valueHolders, ((CallFunction) ce).getValues());
	    return valueHolders.getReturn().getValue();
	} else {
	    return ((Value) v1).getValue();
	}
    }

    public enum Type {

	EQUALS, LESS, BIGGER, NOT_EQUALS
    }

    private Type getInvertedType(Type type) {
	switch (type) {
	    case BIGGER:
		return Type.LESS;
	    case LESS:
		return Type.BIGGER;
	    case EQUALS:
		return Type.NOT_EQUALS;
	    case NOT_EQUALS:
		return Type.EQUALS;
	    default:
		throw new RuntimeException(type + " has no inverted type");
	}
    }

    public BooleanExpression getInvertedInstance() {
	return new BooleanExpression(value1, getInvertedType(type), value1);
    }
}
