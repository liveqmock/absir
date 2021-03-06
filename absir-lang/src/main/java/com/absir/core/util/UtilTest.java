/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-12 上午9:44:01
 */
package com.absir.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.absir.core.kernel.KernelLang;

/**
 * @author absir
 * 
 */
public class UtilTest {

	/** Tag_Map_Times */
	private static Map<Object, Stack<Long>> Tag_Map_Times = new HashMap<Object, Stack<Long>>();

	/**
	 * 
	 */
	public static void spanStart() {
		spanStart(KernelLang.NULL_OBJECT);
	}

	/**
	 * @param tag
	 */
	public static void spanStart(Object tag) {
		Stack<Long> times = Tag_Map_Times.get(tag);
		if (times == null) {
			times = new Stack<Long>();
			Tag_Map_Times.put(tag, times);
		}

		times.push(System.currentTimeMillis());
	}

	/**
	 * @return
	 */
	public static long spanEnd() {
		return spanEnd(KernelLang.NULL_OBJECT);
	}

	/**
	 * @param tag
	 * @return
	 */
	public static long spanEnd(Object tag) {
		Stack<Long> times = Tag_Map_Times.get(tag);
		if (times == null || times.isEmpty()) {
			return 0;
		}

		return times.pop();
	}

}
