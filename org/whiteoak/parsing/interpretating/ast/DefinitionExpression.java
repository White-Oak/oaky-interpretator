/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import java.util.ArrayList;
import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;

/**
 *
 * @author LPzhelud
 */
public abstract class DefinitionExpression extends DefinitiveValue {

    ArrayList<AbstractExpression> expressions = new ArrayList<>();
    private boolean breaked;

    public int size() {
	return expressions.size();
    }

    public synchronized void addElement(AbstractExpression obj) throws InterpretatingException {
	expressions.add(obj);
    }

    public AbstractExpression getLine(int i) throws NullPointerException {
	return expressions.get(i);
    }

    public void breakIt() {
	breaked = true;
    }

    public boolean isBreaked() {
	return breaked;
    }

    public AbstractExpression getLastOne() {
	return expressions.get(expressions.size() - 1);
    }
}
