/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.parsing.interpretating;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import org.whiteoak.parsing.interpretating.ast.*;
import org.whiteoak.parsing.interpretating.exceptions.*;

/**
 *
 * @author LPzhelud
 */
public class Interpretator {

    private Constants constants = null;
    private Constant[] constantsset;
    private ExceptionHandler exhandler;
    private Functions functions;
    private Function[] functionsset;
    private Script script;
    private Stack<DefinitionExpression> definitionExpressions;
    private ValueHolders vh;
    private DefinedHolders dh;
    private Runner runner;

    /**
     * Initializate the Interpretator instance.
     *
     * @param constantses - constants' array stores a list of pre-defined constants
     * @param function - functions' array stores a list of pre-defined functions
     * @param scriptname - scriptname. Used in exceptions only
     * @param exhandler - handler to hold exceptions
     */
    public Interpretator(Constant[] constantses, Function[] function, String scriptname, ExceptionHandler exhandler) {
	init(constantses, function, scriptname, exhandler);
    }
    //
    //
    //

    /**
     * currently undefined
     */
    public void check() {
    }

    /**
     * cureently undefined
     */
    public void optimize() {
    }

    /**
     * cureently undefined
     */
    public void getScript() {
    }

    public void run(IAcceptable acceptable, boolean log) {
//	dh.setRunner(runner);
	runner.run(acceptable, log);
	init(constantsset, functionsset, script.getName(), exhandler);
    }

    public String getLog() throws IOException {
	return runner.getLog();
    }

    public void unpause() {
	runner.unpause();
    }

    /**
     * Gets and parses the input stream. Does not close input stream. Does not execute the input script
     *
     * @param is input with the scipt
     * @param log to log or no
     * @throws IOException
     */
    public void parse(InputStream is, boolean log) throws IOException {
	if (is == null) {
	    throw new NullPointerException("Null Stream");
	}
	String s;//reading input
	try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	    int c;
	    while ((c = is.read()) != -1) {
		baos.write(c);
	    }//	is.close();
	    s = new String(baos.toByteArray(), "UTF-8");
	}
	try {
	    parse(s, log);//call to the overided method
	} catch (Exception ex) {
	    exhandler.acceptException(ex);
	}
    }

    public void parse(String s, boolean log) {
	if (s == null) {
	    throw new NullPointerException("Null Parameters");
	}
	long start = System.currentTimeMillis();
//	String[] lines = getLines(s);//getting list of all lines in the file
	String[] lines = s.split(System.lineSeparator());
	for (String line : lines) {
	    try {
		String string = line;
		parseLine(string);
	    } catch (InterpretatingException ex) {
		exhandler.acceptException(ex);
	    }
	}
	script.finishedParsing();
	if (log) {
	    System.out.println("Parsing took " + ((long) (System.currentTimeMillis() - start)) + " ms");
	}
    }

    private void init(Constant[] constantses, Function[] function, String scriptname, ExceptionHandler exhandler) {
	constants = new Constants();
	if (constantses != null) {
	    for (Constant constantse : constantses) {
		constants.addConstant(constantse);
	    }
	}
	constants.addConstant(new Constant("LINE_SEPARATOR", System.lineSeparator()));
	constantsset = constantses;
	//
	functions = new Functions();
	if (function != null) {
	    for (Function function1 : function) {
		functions.addFunction(function1);
	    }
	}
	functionsset = function;
	//
	vh = new ValueHolders(constants, new Variables());
	dh = new DefinedHolders(vh, functions);
	//
	definitionExpressions = new Stack<>();
	script = new Script(scriptname);
	definitionExpressions.push(script);
	//
	if (exhandler == null) {
	    throw new NullPointerException("Handler must not be a null pointer");
	}
	this.exhandler = exhandler;
	runner = new Runner(constants, exhandler, functions, script);
    }
    //
    //
    //

    private String[] getLines(String s) {
	ArrayList<String> v = new ArrayList<>();
	int previouscut = 0;
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (c == 13) {
		v.add(s.substring(previouscut, i));
		i++;
		previouscut = i + 1;
		continue;
	    }
	    if (c == 10) {
		v.add(s.substring(previouscut, i));
		previouscut = i + 1;
	    }
	}
	if (previouscut < s.length() && previouscut != -1) {
	    v.add(s.substring(previouscut));//Adding last line
	}
	String[] str = new String[v.size()];
	v.toArray(str);
	return str;
    }

    private void parseLine(String s) throws InterpretatingException {
	s = s.trim();
	if (s.length() == 0) {
	    return;//empty line
	}
	if (s.startsWith("//")) {
	    return;//commented line
	}
	//get String with no quotes
	String temp = removeQuoted(s);
	//
	//
	final int CONTAINER_START = 1, CONTAINER_END = 2, FUNCTION_CALL = 3, EXPRESSION = 4;
	final int IF_BLOCK = 1, FUNCTION_DEF = 2, FOR_CYCLE = 4, WHILE_CYCLE = 3, VARIABLE_ASSIGN = 1, BREAK_EXPRESSION = 2;
	//<editor-fold defaultstate="collapsed" desc="detection">
	int type;
	int subtype = 0;
	if (temp.endsWith("{")) {
	    //open-brace It is DefinitionExpression
	    type = CONTAINER_START;
	    //detected what's the type of it
	    if (temp.startsWith("if")) {
		subtype = IF_BLOCK;
	    } else if (temp.startsWith("while")) {
		subtype = WHILE_CYCLE;
	    } else if (temp.startsWith("for")) {
		subtype = FOR_CYCLE;
	    } else if (temp.startsWith("else")) {
		//Hehe =)
		//changing else branch to a new if
		subtype = IF_BLOCK;
		if (definitionExpressions.peek() instanceof IfCall) {
		    IfCall ifCall = (IfCall) definitionExpressions.peek();
		    definitionExpressions.push(ifCall.getInvertedInstance());
		    return;
		}
	    } else {
		subtype = FUNCTION_DEF;
	    }
	} else if (temp.equalsIgnoreCase("}")) {
	    //close-brace It is the end of DefinitionExpression
	    type = CONTAINER_END;
	} else if (!temp.contains("=") && temp.contains("{") && temp.contains("}")) {
	    //Function call
	    type = FUNCTION_CALL;
	} else {
	    //Nothing left)
	    type = EXPRESSION;
	    if (temp.contains("break")) {
		subtype = BREAK_EXPRESSION;
	    } else {
		subtype = VARIABLE_ASSIGN;
	    }
	}
	//</editor-fold>
	//
	//
	//<editor-fold defaultstate="collapsed" desc="main part - decoding">
	switch (type) {
	    case FUNCTION_CALL:
		((DefinitionExpression) (definitionExpressions.peek())).addElement(parseFunctionCall(s));
		break;
	    case CONTAINER_START:
		DefinitionExpression de = null;
		switch (subtype) {
		    case IF_BLOCK:
			de = (parseIfBlockStart(s));
			break;
		    case WHILE_CYCLE:
			de = parseWhileBlockStart(s);
			break;
		    case FOR_CYCLE:
			de = parseForBlockStart(s);
			break;
		}
		((DefinitionExpression) (definitionExpressions.peek())).addElement(de);
		definitionExpressions.push(de);

		break;
	    case CONTAINER_END:
		((DefinitionExpression) (definitionExpressions.pop())).finishedParsing();
		if (definitionExpressions.empty()) {
		    throw new BadScriptException("Wrong numbers of closing '}'");
		}
		break;
	    case EXPRESSION:
		Expression exp = null;
		switch (subtype) {
		    case VARIABLE_ASSIGN:
			exp = parseVariableAssign(s);
			break;
		    case BREAK_EXPRESSION:
			exp = new BreakExpression();
			break;
		}
		((DefinitionExpression) (definitionExpressions.peek())).addElement(exp);
		break;
	}
	//</editor-fold>
    }

    private String removeQuoted(String v) {
	if (!v.contains("\"")) {
	    return v;//if no quotes at all
	}
	StringBuilder sb = new StringBuilder();
	int quotes = 0;
	char temp;
	for (int i = 0; i < v.length(); i++) {
	    temp = v.charAt(i);
	    if (temp == '"') {
		sb.append('!');
		if (quotes % 2 == 0) {
		    quotes++;
		} else {
		    quotes--;
		}
	    } else if (quotes > 0) {
		sb.append('!');
	    } else {
		sb.append(temp);
	    }
	}
	return sb.toString();
    }

    private CallFunction parseFunctionCall(String line) {
	String name = line.substring(0, line.indexOf('{')).trim();
//	if (!functions.containsFunction(name)) {
//	    throw new InvalidCallExcepion("There's no function with name \"" + name + '"');
//	}
	String values = line.substring(line.indexOf('{') + 1, line.length() - 1).trim();
	return new CallFunction(name, parseValues(values));
    }

    private Values parseValues(String s) {
	ArrayList<String> v = new ArrayList<>();
	int previouscut = 0;
	String temp = removeQuoted(s);
	for (int i = 0; i < s.length(); i++) {
	    char c = temp.charAt(i);
	    if (c == ',') {
		v.add(s.substring(previouscut, i));
		previouscut = i + 1;
	    }
	}
	v.add(s.substring(previouscut));//Adding last value
	return ((String) v.get(0)).length() == 0 ? null : new Values(v);
    }

    private IfCall parseIfBlockStart(String line) throws InterpretatingException {
	String bexp = line.substring(line.indexOf('(') + 1, line.length() - 2);
	return new IfCall(parseBooleanExpression(bexp));
    }

    private BooleanExpression parseBooleanExpression(String s) throws InterpretatingException {
	//detection type
	BooleanExpression.Type type;
	final String[] str = {"==", "<", ">", "!="};
	if (s.indexOf(str[0]) != -1) {
	    type = BooleanExpression.Type.EQUALS;
	} else if (s.indexOf(str[1]) != -1) {
	    type = BooleanExpression.Type.LESS;
	} else if (s.indexOf(str[2]) != -1) {
	    type = BooleanExpression.Type.BIGGER;
	} else if (s.indexOf(str[3]) != -1) {
	    type = BooleanExpression.Type.NOT_EQUALS;
	} else {
	    throw new BadBooleanExpressionException(s + "is bad boolean expression");
	}
	String value1 = s.substring(0, s.indexOf(str[type.ordinal()]));
	String value2 = s.substring(value1.length() + str[type.ordinal()].length());
	return new BooleanExpression(getCallExpression(value1), type, getCallExpression(value2));
    }

    private CallExpression getCallExpression(String v) {
	v = v.trim();
	if (detectBrackets(v)) {
	    return getCallExpression(v.substring(1, v.length() - 1));
	} else if (detectedBigCalculable(v)) {
	    return parseCalculateable(v);
	} else if (v.indexOf('{') != -1) {
	    return parseFunctionCall(v);
	} else {
	    return new RuntimeCallExpression(v);
	}

    }

    private VariableAssigning parseVariableAssign(String s) throws InterpretatingException {
	String value = s.substring(s.indexOf('=') + 1).trim();
	String name = s.substring(0, s.indexOf('=')).trim();
	if (constants.containsConstant(name)) {
	    throw new AccessChangedException("Variable " + name + " hides constant with the same name");
	}
	CallExpression ce;
	ce = getCallExpression(value);
	if (ce instanceof CallFunction) {
	    ((DefinitionExpression) (definitionExpressions.peek())).addElement(parseFunctionCall(value));
	    ce = new CallVariable("return");
	}
	return new VariableAssigning(new CallVariable(name), ce);
    }

    private boolean detectedBigCalculable(String v) {
	return v.indexOf('+') != -1 || v.indexOf('-') != -1 || v.indexOf('/') != -1 || v.indexOf('*') != -1;
    }

    private CalculateableValue parseCalculateable(String v) {
	CallExpression v1, v2;
	CalculateableValue.Type type;
	String temp = removeBrackets(v);
	if (temp.indexOf('+') != -1) {
	    type = CalculateableValue.Type.ADD;
	    v1 = getCallExpression(v.substring(0, temp.indexOf('+')));
	    v2 = getCallExpression(v.substring(temp.indexOf('+') + 1));
	} else if (temp.indexOf('-') != -1) {
	    type = CalculateableValue.Type.SUBSTRACT;
	    v1 = getCallExpression(v.substring(0, temp.indexOf('-')));
	    v2 = getCallExpression(v.substring(temp.indexOf('-') + 1));
	} else if (temp.indexOf('*') != -1) {
	    type = CalculateableValue.Type.MULTIPLY;
	    v1 = getCallExpression(v.substring(0, temp.indexOf('*')));
	    v2 = getCallExpression(v.substring(temp.indexOf('*') + 1));
	} else /*if (v.indexOf('/') != -1)*/ {
	    type = CalculateableValue.Type.DIVIDE;
	    v1 = getCallExpression(v.substring(0, temp.indexOf('/')));
	    v2 = getCallExpression(v.substring(temp.indexOf('/') + 1));
	}
	return new CalculateableValue(v1, type, v2);
    }

    private boolean detectBrackets(String v) {
	return v.startsWith("(") && v.endsWith(")");
    }

    private String removeBrackets(String v) {
	if (v.indexOf('(') == -1) {
	    return v;//if no brackets at all
	}
	char temp;
	StringBuilder sb = new StringBuilder();
	int booleans = 0;
	for (int i = 0; i < v.length(); i++) {
	    temp = v.charAt(i);
	    if (temp == '(') {
		sb.append('!');
		booleans++;
	    } else if (temp == ')') {
		sb.append('!');
		booleans--;
	    } else if (booleans > 0) {
		sb.append('!');
	    } else {
		sb.append(temp);
	    }
	}
	return sb.toString();
    }

    private While parseWhileBlockStart(String line) throws InterpretatingException {
	String bexp = line.substring(line.indexOf('(') + 1, line.length() - 2);
	return new While(parseBooleanExpression(bexp));
    }

    private For parseForBlockStart(String line) throws InterpretatingException {
	String temp = line.substring(line.indexOf('(') + 1, line.length() - 2).trim();
	VariableAssigning exp = parseVariableAssign(temp.substring(0, temp.indexOf(';')));
	((DefinitionExpression) (definitionExpressions.peek())).addElement(exp);
	temp = temp.substring(temp.indexOf(';') + 1);
	return new For(parseBooleanExpression(temp.substring(0, temp.indexOf(';'))), parseVariableAssign(temp.substring(
		temp.indexOf(';') + 1)));
    }
}
