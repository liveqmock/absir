/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月18日 下午12:50:45
 */
package com.absir.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author absir
 *
 */
@RunWith(value = JUnit4.class)
public class JSEngine {

	@Test
	public void test() throws Throwable {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");

		// engine.eval((String) null);
		// com.sun.script.javascript.

		// engine.getContext().setWriter(new PrintWriter(stringWriter));
		Compilable compilable = (Compilable) engine;
		CompiledScript compiledScript = compilable.compile("print('Hello, World');  test.dump(); number.toString();");

		// compiledScript.
		// SimpleBindings simpleBindings = new SimpleBindings();
		// simpleBindings.
		// compilable.equals(obj);
		StringWriter stringWriter = new StringWriter();
		SimpleScriptContext tempctxt = new SimpleScriptContext();
		tempctxt.setAttribute("number", null, ScriptContext.ENGINE_SCOPE);
		tempctxt.setAttribute("test", this, ScriptContext.ENGINE_SCOPE);
		tempctxt.setWriter(new PrintWriter(stringWriter));
		compiledScript.eval(tempctxt);

		// sun.org.mozilla.javascript.internal.continuations;

		// inv.invokeFunction("_tpl");
		// engine.eval("_tpl();");
		System.out.println("stringWriter:" + stringWriter.toString());
	}
}
