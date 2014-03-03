/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import java.io.*;
import java.util.Random;
import java.util.Stack;
import org.whiteoak.parsing.interpretating.ast.*;
import org.whiteoak.parsing.interpretating.exceptions.BadScriptException;

/**
 *
 * @author LPzhelud
 */
public class Runner {

    private final Constants constants;
    private final ExceptionHandler exhandler;
    private final Functions functions;
    private final Script script;
    private final ValueHolders vh;
    private final DefinedHolders dh;
    private IAcceptable iAcceptable;
    private ByteArrayOutputStream baos;
    private int tabs = 0;
    private StringBuffer tabsstr = new StringBuffer();
    private boolean log;
    private long start;
    private final Stack call = new Stack();
    private Additional additional;

    public Runner(Constants constants, ExceptionHandler exhandler, Functions functions, Script script) {
	this.constants = constants;
	this.exhandler = exhandler;
	this.functions = functions;
	this.script = script;
	//
	vh = new ValueHolders(constants, new Variables());
	dh = new DefinedHolders(vh, functions);
	dh.setRunner(this);
	//
	addF();
	addC();
    }

    void run(IAcceptable acceptable, boolean log) {
	this.log = log;
	iAcceptable = acceptable;
	start = System.currentTimeMillis();
	if (log) {
	    baos = new ByteArrayOutputStream();
	    printToStream("Running script " + script.getName(), baos);
	}
	try {
	    walkThroughDefinitionBlock(script);
	} catch (Exception ex) {
	    exhandler.acceptException(ex);
	}
	if (log) {
	    System.out.println("Running took " + (System.currentTimeMillis() - start) + " ms");
	}
    }

    private void addF() {
	functions.addFunction(new Function("println", 1, true));
	functions.addFunction(new Function("exit", 0, true));
	functions.addFunction(new Function("destroy", 1, true));
	functions.addFunction(new Function("rand", 0, true));
	functions.addFunction(new Function("rand", 1, true));
	functions.addFunction(new Function("time", 0, true));
	functions.addFunction(new Function("sleep", 1, true));
	functions.addFunction(new Function("pause", 0, true));
    }

    private void addC() {
	constants.addConstant(new Constant("version", "1"));
    }

    private void printToStream(String text, OutputStream os) {
	if (baos == null) {
	    return;
	}
	try {
	    os.write((tabsstr.toString() + text.concat("\n")).getBytes("UTF-8"));
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    public void walkThroughDefinitionBlock(DefinitionExpression de) throws Exception {
	call.push(de);
	if (log) {
	    tabs++;
	    tabsstr = new StringBuffer();
	    for (int i = 0; i < tabs; i++) {
		tabsstr.append("\t");
	    }
	}
	for (int i = 0; i < de.size(); i++) {
	    AbstractExpression abst = de.getLine(i);

	    if (de.isBreaked()) {
		break;
	    }

	    if (abst instanceof CallFunction) {
		CallFunction cf = ((CallFunction) abst);
		Function f = (Function) cf.getDefinitiveValue(dh);
		printToStream("Calling function named " + f.getName(), baos);
		//
		f.runTry(dh, cf.getValues());
	    } else if (abst instanceof VariableAssigning) {
		printToStream("Assigning variable", baos);
		((VariableAssigning) abst).assign(dh);
	    } else if (abst instanceof BreakExpression) {
		printToStream("Breaking cycle", baos);
		DefinitionExpression temp = (DefinitionExpression) call.pop();
		while (!(temp instanceof Cycle)) {
		    temp.breakIt();
		    if (call.isEmpty()) {
			throw new BadScriptException("Breaking out of cycles");
		    }
		    temp = (DefinitionExpression) call.pop();
		}
		temp.breakIt();
		break;
	    } else if (abst instanceof DefinitionExpression) {
		if (abst instanceof IfCall) {
		    final IfCall ifCall = (IfCall) abst;
		    if (ifCall.testTruth(dh)) {
			printToStream("If statement returned truth", baos);
		    } else {
			abst = ifCall.getElseBlock();
			if (abst == null) {
			    continue;
			}
			printToStream("If statement returned false", baos);
		    }
		} else if (abst instanceof Cycle) {
		    Cycle cycle = (Cycle) abst;
		    printToStream("Starting cycle", baos);
		    while (cycle.testBooleanExpression(dh)) {
			walkThroughDefinitionBlock(cycle);
		    }
		    continue;
		} else {
		    printToStream("Entering block of lines", baos);
		}
		walkThroughDefinitionBlock((DefinitionExpression) abst);
	    }
	}
	if (log) {
	    tabs--;
	    tabsstr = new StringBuffer();
	    for (int i = 0; i < tabs; i++) {
		tabsstr.append("\t");
	    }
	    if (!de.isBreaked()) {
		if (de instanceof Cycle) {
		    printToStream("Next iteration of cycle", baos);
		} else {
		    printToStream("Going out of block of lines", baos);
		}
	    } else {
		printToStream("Broken", baos);
	    }
	}
	if (call.peek() == de) {
	    call.pop();
	}
    }

    public void callNativeFunction(String name, Value[] values) {
	//
	String ret = null;
	switch (name) {
	    case "println":
		if (log) {
		    System.out.println("Output: " + values[0].getValue());
		}
		printToStream("Output: " + values[0].getValue(), baos);
		break;
	    case "destroy":
		vh.getVariables().removeVariable(values[0].getName() == null ? "return" : values[0].getName());
		break;
	    case "rand":
		if (values == null) {
		    ret = String.valueOf(new Random().nextInt());
		} else {
		    ret = String.valueOf(new Random().nextInt(Integer.parseInt(values[0].getValue())));
		}
		break;
	    case "time":
		ret = String.valueOf(System.currentTimeMillis() - start);
		break;
	    case "sleep":
		try {
		    if (log) {
//		    System.out.println("Sleeping");
		    }
		    Thread.sleep(Long.parseLong(values[0].getValue()));
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
		break;
	    case "pause":
		iAcceptable.paused();
		additional = new Additional();
		additional.start();
		try {
		    additional.join();
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
		break;
	    case "concat":
		ret = values[0].getValue().concat(values[1].getValue());
		break;
	    default:
		ret = iAcceptable.callNativeFunction(name, values);
		break;
	}
	if (ret != null) {
	    constants.setReturn(ret);
	}
    }

    String getLog() throws IOException {
	baos.flush();
	return "\nLog:\n" + new String(baos.toByteArray(), "UTF-8");
    }

    private class Additional extends Thread {

	public boolean paused = true;

	@Override
	public void run() {
	    while (paused) {
		try {
		    Thread.sleep(50L);
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
	    }
	}
    }

    void unpause() {
	additional.paused = false;
    }
}
