/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.IAcceptable;
import org.whiteoak.parsing.interpretating.Interpretator;
import org.whiteoak.parsing.interpretating.ast.Constant;
import org.whiteoak.parsing.interpretating.ast.Function;
import org.whiteoak.parsing.interpretating.ast.NativeFunction;
import org.whiteoak.parsing.interpretating.ast.Value;

/**
 *
 * @author White Oak
 */
public class Main implements IAcceptable, ExceptionHandler {

    Function[] f = {
	new NativeFunction("test", 0)
    };
    Constant[] c = {};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	new Main().doThing();
    }

    public void doThing() {
	Interpretator interpretator = new Interpretator(c, f, "lol", this);
	interpretator.parse("test {}", true);
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
