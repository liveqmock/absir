/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-6-17 上午10:15:32
 */
package com.absir.appserv.cmd;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author absir
 * 
 */
public class CmdOptionials {

	/** keyMapOpitional */
	private Map<String, Opitional<?>> keyMapOpitional = new LinkedHashMap<String, Opitional<?>>();

	/**
	 * @author absir
	 * 
	 */
	public static class Opitional<T> {

		/** cls */
		private Class<? extends T> cls;

		/**
		 * @param cls
		 */
		private Opitional(Class<? extends T> cls) {
			this.cls = cls;
		}

	}
}
