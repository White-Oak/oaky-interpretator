/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.DefinedHolders;
import org.whiteoak.parsing.interpretating.Values;
import org.whiteoak.parsing.interpretating.exceptions.AccessChangedException;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;
import org.whiteoak.parsing.interpretating.exceptions.InvalidCallExcepion;

/**
 *
 * @author LPzhelud
 */
public class Function extends DefinitionExpression {

    private final String name;
    private final int amountOfParameters;
    private final boolean isNative;

    public Function(String name, int amountofpars, boolean isn) {
	this.name = name;
	this.amountOfParameters = amountofpars;
	isNative = isn;
	//if native function then there is nothing to process
	if (isNative) {
	    expressions = null;
	}
    }

    public int getAmountOfParameters() {
	return amountOfParameters;
    }

    public String getName() {
	return amountOfParameters != 0 ? name.concat("_").concat(String.valueOf(amountOfParameters)) : name;
    }

    public String getRealName() {
	return name;
    }

    @Override
    public synchronized void addElement(AbstractExpression abstractExpression) throws InterpretatingException {
	if (isNative) {
	    throw new AccessChangedException("Trying to define native function");
	}
	expressions.add(abstractExpression);
    }

    public void runTry(DefinedHolders dh, Values v) throws InterpretatingException {
	Value[] val = null;
	if (amountOfParameters != 0) {
	    if (v.size() != amountOfParameters) {
		throw new InvalidCallExcepion(
			"Wrong number of parameters (" + v.size() + ") instead of " + amountOfParameters);
	    }

	    val = new Value[amountOfParameters];
	    Value temp = null;
	    for (int i = 0; i < val.length; i++) {
		if (v.wasQuoted(i) || (temp = dh.getValueHolders().get(v.get(i))) == null) {
		    val[i] = new PreDefinedConstant(v.get(i));
		} else {
		    val[i] = temp;
		}
	    }
	}
	if (isNative) {
	    dh.getRunner().callNativeFunction(name, val);
	} else {
	    //undefined
	}
    }

    public boolean isIsnative() {
	return isNative;
    }
}
