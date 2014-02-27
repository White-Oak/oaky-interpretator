/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

/**
 *
 * @author LPzhelud
 */
public class BooleanVector {
    private boolean[] buf;
    int capacity, size = 0;

    public BooleanVector(int cap) {
	capacity = cap;
	buf = new boolean[capacity];
    }

    public BooleanVector() {
	this(10);
    }

    public void addElement(boolean b) {
	if (size < capacity) {
	    buf[size] = b;
	    size++;
	} else {
	    boolean bufnew[] = new boolean[capacity + 10];
	    System.arraycopy(buf, 0, bufnew, 0, capacity);
	    buf = bufnew;
	    capacity += 10;
	    addElement(b);
	}
    }

    public boolean elementAt(int i) {
	return buf[i];
    }

    public int size() {
	return size;
    }
    public void removeElementAt(int at){
	for (int i = at; i < buf.length-1; i++) {
	    buf[i]=buf[i+1];
	}
	size--;
    }
}
