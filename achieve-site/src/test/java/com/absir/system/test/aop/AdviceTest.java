/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月11日 上午10:12:34
 */
package com.absir.system.test.aop;

import org.junit.Test;

import com.absir.bean.inject.value.Bean;
import com.absir.system.test.AbstractTestInject;

/**
 * @author absir
 *
 */
public class AdviceTest extends AbstractTestInject {

	@Bean
	public static class AdviceBean {

		public static final AdviceBean ME = null;

	}

	@Test
	public void test() throws Throwable {
		
	}

}
