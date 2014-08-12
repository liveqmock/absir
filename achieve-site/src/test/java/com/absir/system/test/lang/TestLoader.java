/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午4:13:43
 */
package com.absir.system.test.lang;

import org.junit.Test;

import com.absir.core.kernel.KernelClass;
import com.absir.system.test.AbstractTest;

/**
 * @author absir
 * 
 */
public class TestLoader extends AbstractTest {

	/**
	 * @return
	 */
	public static String getName() {
		return "123";
	}

	public static class T extends ATest implements ITest {
	}

	@Test
	public void test() {
		for (Class<?> cls : T.class.getInterfaces()) {
			KernelClass.forName(cls.getName());
		}

		// KernelClass.forName(ITest.class.getName());
		T t = new T();
		System.out.println(t);
		System.out.println(ATest.name);
		System.out.println(ITest.name);
	}
}
