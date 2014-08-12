/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午4:13:43
 */
package com.absir.system.test.lang;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Test;

import com.absir.system.test.AbstractTest;

/**
 * @author absir
 * 
 */
public class TestMethod extends AbstractTest {

	public interface TI<T> {

		public T t(T t);

	}

	public class TS implements TI<String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.system.test.lang.TestMethod.TI#t(java.lang.Object)
		 */
		@Override
		public String t(String t) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public class TL implements TI<Long> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.system.test.lang.TestMethod.TI#t(java.lang.Object)
		 */
		@Override
		public Long t(Long t) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static interface TestProxy {

		public void emptyProxy();
	}

	public static abstract class TestProxyClass implements TestProxy {

		protected String getName() {
			return "dddd";
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static String unTransferred(String value) {
		value = value.trim();
		int length = value.length();
		if (length < 1) {
			return value;
		}

		StringBuilder stringBuilder = new StringBuilder();
		int quotation = 0;
		char chr = value.charAt(0);
		if (chr == '"') {
			quotation = 1;

		} else {
			stringBuilder.append(chr);
		}

		length--;
		boolean transferred = false;
		for (int i = 1; i < length; i++) {
			chr = value.charAt(i);
			if (transferred) {
				transferred = false;
				appendTransferred(stringBuilder, chr);

			} else if (chr == '\\') {
				transferred = true;

			} else {
				stringBuilder.append(chr);
			}
		}

		chr = value.charAt(length);
		if (transferred) {
			appendTransferred(stringBuilder, chr);

		} else {
			// "" quotation
			if (quotation == 1 && chr == '"') {
				quotation = 2;

			} else {
				stringBuilder.append(chr);
			}
		}

		return quotation == 1 ? '"' + stringBuilder.toString() : stringBuilder.toString();
	}

	/**
	 * @param stringBuilder
	 * @param chr
	 */
	public static void appendTransferred(StringBuilder stringBuilder, char chr) {
		switch (chr) {
		case 't':
			stringBuilder.append("\t");
			break;
		case 'r':
			stringBuilder.append("\r");
			break;
		case 'n':
			stringBuilder.append("\n");
			break;
		case '"':
			stringBuilder.append('"');
			break;
		case '\'':
			stringBuilder.append('\'');
			break;

		default:
			stringBuilder.append('\\');
			stringBuilder.append(chr);
			break;
		}
	}

	public void dump() {
		new Exception().printStackTrace();
	}

	@Test
	public void main() throws IOException, ScriptException, NoSuchMethodException {

		// System.out.println("tst:" + DynaBinder.to(new Object(),
		// String.class));
		//
		//
		//
		// for(int i = 0; i < 100; i++) {
		// System.out.println(HelperRandom.RANDOM.nextFloat());
		// }

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

		// long ip = HelperLong.longIP("154.64.12.64.23.1", -1);
		// System.out.println(ip);
		//
		// String address = HelperLong.stringLong(ip, '.', 6);
		// System.out.println(address);
		//
		// address = HelperLong.stringLong(ip, '.', 4);
		// System.out.println(address);

		// System.out.println(KernelString.unTransferred("\"    dsad\\当时的撒d\r\nddsds\""));

		// JdbcDriver driver = new JdbcDriver();
		// System.out.println(driver.updateString("TEST", new Object[] { "name",
		// true, "value", "dadsad" }, new Object[] { "value", 123213 }, new
		// ArrayList<Object>()));
		// System.out.println(Boolean.valueOf("sdsadsaasd"));
		//
		// System.out.println((int) 0.1f);
		// System.out.println((int) 0.8f);
		//
		// Accessor accessor = UtilAccessor.getAccessorObj(new JbRewardBean(),
		// "cardRewards");
		// System.out.println(accessor);

		// System.out.println(KernelString.implode(XlsBaseKeyDeserialize.class.getDeclaredConstructors(),
		// "\r\n"));
		//
		// JbReward reward = new JbReward();
		// LinkedHashMap<XCardDefine, Integer> cardDefineMap = new
		// LinkedHashMap<XCardDefine, Integer>();
		// XCardDefine cardDefine = new XCardDefine();
		// KernelObject.declaredSet(cardDefine, "id", 1L);
		// cardDefineMap.put(cardDefine, 1);
		// reward.setCardDefines(cardDefineMap);
		// String jsonValue = HelperJson.encode(reward);
		// System.out.println(jsonValue);
		//
		// System.out.print(HelperJson.decode(jsonValue, JbReward.class));

		// KernelReflect.doWithMethods(JUserRole.class, new
		// CallbackBreak<Method>() {
		//
		// @Override
		// public void doWith(Method template) throws BreakException {
		// // TODO Auto-generated method stub
		// System.out.println(template);
		// }
		// });

		// System.out.println("==================");
		// Method method = KernelReflect.assignableMethod(JUserRole.class,
		// Serializable.class, "getId", 0, true, true, false);
		// System.out.println(method);
		// Accessor accessor = UtilAccessor.getAccessor(JUserRole.class,
		// KernelReflect.declaredField(JUserRole.class, "id"));
		// System.out.println(accessor.getGetter());
		// System.out.println(accessor.getSetter());
		// method = KernelReflect.method(JUserRole.class, "getId");
		// System.out.println(method);

		// System.out.println(KernelClass.rawClass(JPlayer.class));
		// System.out.println(KernelClass.componentClass(JPlayer.class));
		// AopProxy aopProxy = AopProxyUtils.getProxy(null,
		// TestProxyClass.class, null, false, true);
		// System.out.println(aopProxy.getBeanObject());
		//
		// TestProxyClass testProxyClass = (TestProxyClass) aopProxy;
		// System.out.println(testProxyClass.getName());
		// aopProxy = AopProxyUtils.getProxy(null, JUserDao.class, null, true,
		// true);
		// System.out.println(aopProxy.getBeanObject());
		// ConcurrentSkipListMap<JPlayer, Boolean> map = new
		// ConcurrentSkipListMap<JPlayer, Boolean>(new Comparator<JPlayer>() {
		//
		// @Override
		// public int compare(JPlayer o1, JPlayer o2) {
		// // TODO Auto-generated method stub
		// return 0;
		// }
		//
		// });
		//
		// Set<JPlayer> players = Collections.newSetFromMap(map);
		// JPlayer player = new JPlayer();
		// player.setId(1L);
		// player.setName("123");
		// players.add(player);
		// System.out.println(HelperJson.encodeNull(players));
		//
		// player = new JPlayer();
		// player.setId(1L);
		// player.setName("ABC");
		// players.add(player);
		//
		// System.out.println(HelperJson.encodeNull(players));
		//
		// players.remove(2L);
		// System.out.println(HelperJson.encodeNull(players));

		// map.put(1L, "123");
		// map.put(2L, "ABC");
		// map.put(3L, "ABC");
		// map.put(4L, "ABC");
		// map.put(5L, "ABC");

	}
}
