/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import org.whiteoak.parsing.interpretating.ast.Value;

/**
 *
 * @author LPzhelud
 */
public class DefinedHolders extends Holders {

    private final ValueHolders valueHolders;
    private final Functions functions;
    private Runner runner;

    public DefinedHolders(ValueHolders valueHolders, Functions functions) {
	this.valueHolders = valueHolders;
	this.functions = functions;
    }

    public Functions getFunctions() {
	return functions;
    }

    public ValueHolders getValueHolders() {
	return valueHolders;
    }

    public Runner getRunner() {
	return runner;
    }

    void setRunner(Runner runner) {
	this.runner = runner;
    }

    public Value getReturn() {
	return valueHolders.get("return");
    }
}
