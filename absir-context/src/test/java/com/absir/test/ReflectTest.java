/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-9 下午6:44:33
 */
package com.absir.test;

import java.util.Random;

import org.junit.Test;

import com.absir.context.core.ContextAtom;
import com.absir.core.util.UtilAtom;

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

	private void testAtom() {
		final UtilAtom contextAtom = new ContextAtom(8);
		// new ContextAtom(8);
		final Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 32; i++) {
			contextAtom.increment();
			final int index = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					System.out.println("线程开始[" + index + "]");
					try {
						Thread.sleep(random.nextInt(1000));

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("线程结束[" + index + "]");
					contextAtom.decrement();
				}
			}).start();
		}

		System.out.println("等待线程...");
		contextAtom.await();
		System.out.println("所有线程执行完毕");
	}

	@Test
	public void test() {
		testAtom();
		testAtom();
	}

}
