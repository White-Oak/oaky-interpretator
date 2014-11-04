/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;

/**
 *
 * @author White Oak
 */
public class Main implements IAcceptable, ExceptionHandler {

    Function[] f = {
	new NativeFunction("test", 0)
    };
    Constant[] c = {};

    public static void main(String[] args) throws Exception {
//	new Main().doThing();
	Interpretatorv2.parse(new FileInputStream(new File("script")));
    }

    public void doThing() {
	Interpretator interpretator = new Interpretator(c, f, "lol", this);
	try {
	    interpretator.parse(getClass().getResourceAsStream("/test/test.script"), true);
	} catch (IOException ex) {
	    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	}
	interpretator.run(this, true);
	interpretator.run(this, true);
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	System.out.println(name);
	return "Yeah, baby";
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception ex) {
	ex.printStackTrace();
    }

}
