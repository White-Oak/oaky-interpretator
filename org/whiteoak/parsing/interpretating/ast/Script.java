/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

/**
 *
 * @author LPzhelud
 */
public class Script extends DefinitionExpression {

    private final String name;

    public Script(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}
