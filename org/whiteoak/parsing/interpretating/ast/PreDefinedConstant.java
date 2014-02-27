/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

/**
 *
 * @author LPzhelud
 */
public class PreDefinedConstant extends Value {

    public PreDefinedConstant(String value) {
	super(value, value);
    }

}
