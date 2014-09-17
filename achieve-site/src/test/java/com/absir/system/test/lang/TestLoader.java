/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午4:13:43
 */
package com.absir.system.test.lang;

import org.junit.Test;

import com.absir.aop.AopProxyUtils;
import com.absir.appserv.system.bean.JUser;
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
		System.out.println(AopProxyUtils.getProxy(new JUser(), false, false).getClass());
		System.out.println(AopProxyUtils.getProxy(new JUser(), false, false).getClass());
		System.out.println(AopProxyUtils.getProxy(new JUser(), true, false).getClass());
		System.out.println(AopProxyUtils.getProxy(new JUser(), true, false).getClass());
	}
}
