/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-14 下午5:22:44
 */
package com.absir.core.kernel;

/**
 * @author absir
 * 
 */
public class KernelByte {

	/**
	 * @param destination
	 * @param destionationIndex
	 * @return
	 */
	public static int getLength(byte[] destination, int destionationIndex) {
		int length = destination[destionationIndex];
		length += destination[++destionationIndex] << 8;
		length += destination[++destionationIndex] << 16;
		length += destination[++destionationIndex] << 24;
		return length;
	}

	/**
	 * @param destination
	 * @param destionationIndex
	 * @param length
	 */
	public static void setLength(byte[] destination, int destionationIndex, int length) {
		destination[destionationIndex] = (byte) (length);
		destination[++destionationIndex] = (byte) (length >> 8);
		destination[++destionationIndex] = (byte) (length >> 16);
		destination[++destionationIndex] = (byte) (length >> 24);
	}

	/**
	 * @param source
	 * @param destination
	 * @param sourceIndex
	 * @param destionationIndex
	 * @param length
	 */
	public static void copy(byte[] source, byte[] destination, int sourceIndex, int destionationIndex, int length) {
		for (; length > 0; length--) {
			destination[destionationIndex++] = source[sourceIndex++];
		}
	}
}
