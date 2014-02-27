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
public class While extends Cycle {

    private final BooleanExpression be;

    @Override
    public boolean testBooleanExpression(DefinedHolders dh) throws InterpretatingException {
	return be.testTruth(dh) & !isBreaked();
    }

    public While(BooleanExpression be) {
	this.be = be;
    }
}
