/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-27 上午11:23:11
 */
package com.absir.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.absir.core.kernel.KernelList;
import com.absir.core.kernel.KernelList.Orderable;

/**
 * @author absir
 * 
 */
@RunWith(JUnit4.class)
public class StringTest {

	class Wrapper {

		public String name;

		public Wrapper wrapper;

		public Wrapper(String name) {
			this(name, null);
		}

		public Wrapper(String name, Wrapper wrapper) {
			this.name = name;
			this.wrapper = wrapper;
		}

	}

	public static class TestOrder implements Orderable {

		private int order;

		/**
		 * @param order
		 */
		public TestOrder(int order) {
			this.order = order;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
		 */
		@Override
		public int getOrder() {
			// TODO Auto-generated method stub
			return order;
		}

	}

	@Test
	public void test() {

		List<Orderable> orderables = new ArrayList<Orderable>();

		orderables.add(new TestOrder(Integer.MAX_VALUE));
		orderables.add(new TestOrder(1));
		orderables.add(new TestOrder(3));
		orderables.add(new TestOrder(0));
		orderables.add(new TestOrder(4));
		orderables.add(new TestOrder(-1));

		for (Orderable orderable : orderables) {
			System.out.println(orderable.getOrder());
		}

		System.out.println("排序后...");

		KernelList.sortOrderable(orderables);

		for (Orderable orderable : orderables) {
			System.out.println(orderable.getOrder());
		}

		//
		// System.out.println(KernelString.camelUncapitalize("name_test__d"));
		// System.out.println(KernelString.camelUnderline("NAameTestD"));
		// System.out.println(KernelString.camelInvertUnderline("StoreActive"));
	}

}
