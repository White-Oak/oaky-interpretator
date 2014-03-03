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
public class IfCall extends DefinitionExpression {

    private final BooleanExpression booleanExpression;
    private ElseBranch elseBlock;

    public IfCall(BooleanExpression booleanExpression) {
	this.booleanExpression = booleanExpression;
    }

    public boolean testTruth(DefinedHolders valueHolders) throws InterpretatingException {
	return booleanExpression.testTruth(valueHolders);
    }

    public IfCall getInvertedInstance() {
	return new IfCall(booleanExpression.getInvertedInstance());
    }

    public ElseBranch getElseBlock() {
	return elseBlock;
    }

    public void setElseBlock(ElseBranch elseBlock) {
	this.elseBlock = elseBlock;
    }

}
