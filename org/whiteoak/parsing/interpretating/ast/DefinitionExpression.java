/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

import org.whiteoak.parsing.interpretating.exceptions.InterpretatingException;
import java.util.ArrayList;

/**
 *
 * @author LPzhelud
 */
public abstract class DefinitionExpression extends DefinitiveValue {

    ArrayList<AbstractExpression> expressions = new ArrayList<>();
    private AbstractExpression[] exps;
    private boolean breaked;

    public int size() {
	return expressions == null ? exps.length : expressions.size();
    }

    public synchronized void addElement(AbstractExpression obj) throws InterpretatingException {
	expressions.add(obj);
    }

    public void finishedParsing() throws NullPointerException {
	exps = new AbstractExpression[expressions.size()];
	expressions.toArray(exps);
	expressions = null;
    }

    public AbstractExpression getLine(int i) throws NullPointerException {
	return exps[i];
    }

    public void breakIt() {
	breaked = true;
    }

    public boolean isBreaked() {
	return breaked;
    }
}
