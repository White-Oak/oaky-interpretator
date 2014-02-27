/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

/**
 *
 * @author LPzhelud
 */
public class Variable extends Constant {

    public Variable(String name, String value) {
	super(name, value);
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
