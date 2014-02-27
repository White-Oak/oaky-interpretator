/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

/**
 *
 * @author LPzhelud
 */
public abstract class Value extends DefinitiveValue{

    String name;
    String value;

    Value(String name, String value) {
	this.name = name;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public String getValue() {
	return value;
    }
}
