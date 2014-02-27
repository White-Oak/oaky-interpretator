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
public interface IAcceptable {

    public String callNativeFunction(String name, Value[] values);

    public void paused();
}
