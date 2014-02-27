/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;
import org.whiteoak.parsing.interpretating.exceptions.AccessChangedException;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;

/**
 *
 * @author LPzhelud
 */
public class VariableAssigning extends Expression {

    private final CallVariable assignableVariable;
    private final CallExpression value;

    public VariableAssigning(CallVariable assignableVariable, CallExpression value) {
	this.assignableVariable = assignableVariable;
	this.value = value;
    }

    public void assign(DefinedHolders valueHolders) throws InterpretatingException {
	if (valueHolders.getValueHolders().getConstants().containsConstant(assignableVariable.getName())) {
	    throw new AccessChangedException("Cannot change type of " + assignableVariable.getName() + " from constant to variable");
	}
	Value v = (Value) value.getDefinitiveValue(valueHolders);

	String str = v.getValue();
	valueHolders.getValueHolders().getVariables().addVariable(new Variable(assignableVariable.getName(), str));
    }
}
