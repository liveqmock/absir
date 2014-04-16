/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-16 下午3:15:59
 */
package com.absir.ab;

import com.absir.core.kernel.KernelDyna;

/**
 * @author absir
 * 
 */
public class ABTester {

	/** threadCount */
	protected static int threadCount = 10240;

	/** port */
	protected static int port = 18891;

	/** hostname */
	protected static String hostname;

	/**
	 * @param parameters
	 */
	public static void main(String... parameters) {
		int last = parameters.length - 1;
		if (last >= 2) {
			threadCount = KernelDyna.toInteger(parameters[last--], threadCount);
		}

		if (last >= 1) {
			port = KernelDyna.toInteger(parameters[last--], port);
		}

		if (last >= 0) {
			hostname = parameters[last--];
		}

		try {
			for (int i = 0; i < threadCount; i++) {
				ABTesterRunable testerRunable = new ABTesterRunable();
				new Thread(testerRunable).start();
			}

			while (true) {
				try {
					Thread.sleep(60000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
