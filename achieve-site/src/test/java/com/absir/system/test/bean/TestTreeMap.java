/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午4:13:43
 */
package com.absir.system.test.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.absir.core.base.IBase;
import com.absir.core.kernel.KernelClass;
import com.absir.system.test.AbstractTest;

/**
 * @author absir
 * 
 */
public class TestTreeMap extends AbstractTest {

	public static class TreeKey implements IBase<Serializable> {

		public void test() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.system.bean.proxy.JiBase#getId()
		 */
		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return "123";
		}
	}

	@Test
	public void main() throws IOException {
		for (Method method : TreeKey.class.getDeclaredMethods()) {
			System.out.println(method);
			System.out.println(method.isSynthetic());
			System.out.println(method.isVarArgs());
			System.out.println(method.isBridge());
		}

		Class<?> beanType = String.class;
		Method beanMethod = null;

		List<Method> bridgeMethods = null;
		if (bridgeMethods == null) {
			bridgeMethods = new ArrayList<Method>();
			for (Method method : beanType.getDeclaredMethods()) {
				if (method.isBridge()) {
					bridgeMethods.add(method);
				}
			}
		}

		Class<?>[] parameterTypes = beanMethod.getParameterTypes();
		for (Method method : bridgeMethods) {
			if (method.getName().equals(beanMethod.getName()) && KernelClass.isAssignableFrom(method.getParameterTypes(), parameterTypes)) {

			}
		}

	}

}
