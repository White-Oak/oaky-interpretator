/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

import java.util.Vector;

/**
 *
 * @author LPzhelud
 */
public class Queue extends Vector {

    public void firstIn(Object obj) {
	addElement(obj);
    }

    public Object peek() {
	return elementAt(0);
    }

    public Object firstOut() {
	Object obj = elementAt(0);
	removeElementAt(0);
	return obj;
    }
}