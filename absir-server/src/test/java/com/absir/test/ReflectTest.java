/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-9 下午6:44:33
 */
package com.absir.test;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author absir
 * 
 */
public class ReflectTest {

	public static class TestA {

		public void testA() {

		}
	}

	public static class TestB extends TestA {

		public void testB() {

		}
	}

	public static Pattern PATH_PATTERN = Pattern.compile("^(.*?)/(.*?)-((.*?))$");

	@Test
	public void test() {
		String name = "12/{title}-{title2}";

		Matcher matcher = PATH_PATTERN.matcher(name);
		while (matcher.find()) {
			MatchResult matchResult = matcher.toMatchResult();
			int gc = matchResult.groupCount();
			System.out.println("matcher.find()......" + gc);
			for (int i = 0; i < gc; i++) {
				System.out.println(i + " : " + matchResult.group(i));
			}

		}

		// System.out.println(name);
		//
		// System.out.println(name.replace("${name}", "123"));
		//
		//
		// for (Method method : TestA.class.getMethods()) {
		// System.out.println(method);
		// }
		//
		// System.out.println("test....");
		//
		// for (Method method : TestB.class.getDeclaredMethods()) {
		// System.out.println(method);
		// }
	}

}
