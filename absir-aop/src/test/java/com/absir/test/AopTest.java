/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-25 上午11:13:23
 */
package com.absir.test;

import org.junit.Test;

import com.absir.aop.AopProxyUtils;

/**
 * @author absir
 * 
 */
public class AopTest {

	@Test
	public void test() {

		Object aopTest = AopProxyUtils.getProxy(new AopTest(), false, false);
		System.out.println(aopTest);
	}

}
