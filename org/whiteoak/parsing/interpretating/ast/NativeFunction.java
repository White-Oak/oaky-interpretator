/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating.ast;

public class NativeFunction extends Function {

    public NativeFunction(String name, int amountofpars) {
	super(name, amountofpars, true);
    }

    public NativeFunction(String name) {
	super(name, 0, true);
    }

}
