/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

/**
 *
 * @author LPzhelud
 */
public class MathMy {

    public static float pow(float f, int pow) {
	float res = 1;
	int abspow = Math.abs(pow);
	for (int i = 0; i < abspow; i++) {
	    res = res * f;
	}
	if (pow < 0) {
	    return 1f / res;
	}
	return res;
    }

    public static double pow(double f, int pow) {
	double res = 1;
	int abspow = Math.abs(pow);
	for (int i = 0; i < abspow; i++) {
	    res = res * f;
	}
	if (pow < 0) {
	    return 1d / res;
	}
	return res;
    }

    public static int pow(int f, int pow) {
	pow = Math.abs(pow);
	int res = 1;
	for (int i = 0; i < pow; i++) {
	    res = res * f;
	}
	return res;
    }
}
