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
public class For extends While {

    VariableAssigning va1;
    private boolean notfirsttime;

    public For(BooleanExpression be, VariableAssigning variableAssigning2) {
	super(be);
	va1 = variableAssigning2;
    }

    @Override
    public boolean testBooleanExpression(DefinedHolders dh) throws InterpretatingException {
	if (notfirsttime) {
	    va1.assign(dh);
	} else {
	    notfirsttime = true;
	}
	return super.testBooleanExpression(dh);
    }
}
