/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-28 下午4:49:18
 */
package com.absir.appserv.system.helper;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author absir
 * 
 */
public class HelperRandom {

	/** RANDOM */
	public static final Random RANDOM = new Random(new Date().getTime());

	/**
	 * @param max
	 * @return
	 */
	public static int nextInt(int max) {
		return RANDOM.nextInt(max);
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt(int min, int max) {
		max -= min;
		max = max > 0 ? RANDOM.nextInt(max) : 0;
		return min + max;
	}

	/**
	 * @param rnd
	 * @param began
	 * @param end
	 * @return
	 */
	public static int randInt(int rnd, int began, int end) {
		if (rnd < began) {
			rnd = began;

		} else if (rnd > end) {
			rnd = end;
		}

		return rnd;
	}

	/**
	 * @param min
	 * @param max
	 * @param began
	 * @param end
	 * @return
	 */
	public static int randInt(int min, int max, int began, int end) {
		int rnd = nextInt(min, max);
		return randInt(rnd, began, end);
	}

	/**
	 * @param collection
	 * @return
	 */
	public static <T> T randElement(Collection<? extends T> collection) {
		int size = collection.size();
		size = nextInt(size);
		if (collection instanceof List) {
			return ((List<? extends T>) collection).get(size);

		} else {
			for (T element : collection) {
				if (size-- == 0) {
					return element;
				}
			}
		}

		return null;
	}

	/**
	 * @param b
	 * @param e
	 * @return
	 */
	public static Color randColor(int b, int e) {
		return randColor(b, e, b);
	}

	/**
	 * @param b
	 * @param e
	 * @param a
	 * @return
	 */
	public static Color randColor(int b, int e, int a) {
		return randColor(b, e, b, e, b, e, a, e);
	}

	/**
	 * @param rb
	 * @param re
	 * @param gb
	 * @param ge
	 * @param bb
	 * @param be
	 * @param ab
	 * @param ae
	 * @return
	 */
	public static Color randColor(int rb, int re, int gb, int ge, int bb, int be, int ab, int ae) {
		rb = randInt(rb, re, 0, 255);
		gb = randInt(gb, ge, 0, 255);
		bb = randInt(bb, be, 0, 255);
		if (ab < 0) {
			return new Color(rb, gb, bb);

		} else {
			ab = randInt(ab, ae, 0, 255);
		}

		return new Color(rb, gb, bb, ab);
	}

	/**
	 * @param size
	 * @return
	 */
	public static String randChars(int size) {
		return randChars(size, 0);
	}

	/**
	 * 0x01 has number | no number 0x02 has char | no char 0x03 UPPER CHAR |
	 * lower char
	 * 
	 * @param size
	 * @param type
	 * @return
	 */
	public static String randChars(int size, int type) {
		int rb = (type & 0x00) == 0 ? 0 : 10;
		int re = (type & 0x01) == 0 ? 26 : 10;
		if (rb == 0) {
			re += 10;

		} else if (re == 10) {
			re = 26;
		}

		int lower = (type & 0x02) == 0 ? 65 : 96;
		StringBuilder buffer = new StringBuilder();
		for (; size > 0; size--) {
			int rnd = rb + RANDOM.nextInt(re);
			if (rnd < 10) {
				buffer.append(rnd);

			} else {
				buffer.append((char) (rnd - 10 + lower));
			}
		}

		return buffer.toString();
	}

	/** SECEND_SIZE */
	private static final int SECEND_SIZE = 3;

	/**
	 * @return
	 */
	public static String randSecendId() {
		return randSecendId(SECEND_SIZE);
	}

	/**
	 * @param size
	 * @return
	 */
	public static String randSecendId(int size) {
		return randSecendId(System.currentTimeMillis(), size);
	}

	/**
	 * @param time
	 * @param size
	 * @return
	 */
	public static String randSecendId(long time, int size) {
		return randSecendBuidler(time, size).toString();
	}

	/**
	 * @param time
	 * @param size
	 * @param id
	 * @return
	 */
	public static String randSecendId(long time, int size, int id) {
		return randSecendBuidler(time, size).append(randFormate(8, Integer.toHexString(id).toCharArray())).toString();
	}

	/**
	 * @param time
	 * @param size
	 * @return
	 */
	public static StringBuilder randSecendBuidler(long time, int size) {
		int max = size >= 8 ? Integer.MAX_VALUE : (1 << (size * 4)) - 1;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Long.toHexString(time));
		stringBuilder.append(randFormate(size, Integer.toHexString(RANDOM.nextInt(max)).toCharArray()));
		return stringBuilder;
	}

	/**
	 * @param i
	 * @return
	 */
	public static char[] randFormate(int size, char[] chars) {
		int length = chars.length;
		if (length >= size) {
			return chars;

		} else {
			char[] buff = new char[size];
			int start = size - length;
			Arrays.fill(buff, 0, start, '0');
			for (length = start; start < size; start++) {
				buff[start] = chars[start - length];
			}

			return buff;
		}
	}
}
