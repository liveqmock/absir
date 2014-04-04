/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-25 下午7:20:14
 */
package com.absir.core.kernel;

/**
 * @author absir
 * 
 */
public class KernelUtil {

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static int compare(byte[] from, byte[] to) {
		int len1 = from.length;
		int len2 = to.length;
		int compare = compareNo(from, to, len1, len2);
		if (compare == 0) {
			compare = len1 - len2;
		}

		return compare;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static int compareNo(byte[] from, byte[] to) {
		return compareNo(from, to, from.length, to.length);
	}

	/**
	 * @param from
	 * @param to
	 * @param len1
	 * @param len2
	 * @return
	 */
	public static int compareNo(byte[] from, byte[] to, int len1, int len2) {
		int len = len1 < len2 ? len1 : len2;
		int compare;
		for (int i = 0; i < len; i++) {
			compare = from[i] - to[i];
			if (compare != 0) {
				return compare;
			}
		}

		return len1 >= len2 ? 0 : len1 - len2;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static int compareNull(byte[] from, byte[] to) {
		if (from == null) {
			return to == null ? 0 : -1;
		}

		return to == null ? 1 : compare(from, to);
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static int compareEndNo(byte[] from, byte[] to) {
		return compareEndNo(from, to, from.length, to.length);
	}

	/**
	 * @param from
	 * @param to
	 * @param len1
	 * @param len2
	 * @return
	 */
	public static int compareEndNo(byte[] from, byte[] to, int len1, int len2) {
		int len = len1 < len2 ? len1 : len2;
		int compare;
		for (int i = 1; i <= len; i++) {
			compare = from[len1 - i] - to[len2 - i];
			if (compare != 0) {
				return compare;
			}
		}

		return len1 >= len2 ? 0 : len1 - len2;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static int compareEndNoNull(byte[] from, byte[] to) {
		if (to == null) {
			return 0;
		}

		if (from == null) {
			return -1;
		}

		return compareEndNo(from, to);
	}
}
