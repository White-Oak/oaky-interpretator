/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

/**
 *
 * @author LPzhelud
 */
public class IntVector {

    private int[] buf;
    int capacity, size = 0;

    public IntVector(int cap) {
	capacity = cap;
	buf = new int[capacity];
    }

    public IntVector() {
	this(10);
    }

    public void addElement(int b) {
	if (size < capacity) {
	    buf[size] = b;
	    size++;
	} else {
	    int bufnew[] = new int[capacity * 2];
	    System.arraycopy(buf, 0, bufnew, 0, capacity);
	    buf = bufnew;
	    capacity *= 2;
	    addElement(b);
	}
    }

    public int elementAt(int i) {
	return buf[i];
    }

    public int size() {
	return size;
    }

    public int[] toArray() {
	int[] ar = new int[size];
	System.arraycopy(buf, 0, ar, 0, size);
	return ar;
    }

    public int removeElement(int i) {

	for (int j = 0; j < size; j++) {
	    int k = buf[j];
	    if (k == i) {
		for (int l = j; l < size - 1; l++) {
		    buf[l] = buf[l + 1];
		}
		size--;
		return j;
	    }
	}
	return -1;
    }
}
