/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.system.helper;

/**
 * @author absir
 * 
 */
public class HelperLong {

	/**
	 * IP地址转换数字
	 * 
	 * @param iPaddress
	 * @param iPcount
	 * @return
	 */
	private static long longIP(String iPaddress, int iPcount) {
		return longString(iPaddress, ".", iPcount);
	}

	/**
	 * 规则间隔转换数字
	 * 
	 * @param string
	 * @param glues
	 * @param count
	 * @return
	 */
	private static long longString(String string, String glues, int count) {
		count -= 1;
		int to = 0;
		long lg = 0;
		for (int from = 0; count >= 0; count--) {
			to = string.indexOf(glues, from);
			if (to < 0) {
				lg += HelperNumber.toLong(string.substring(from)) << (2 << count);
				break;
			}

			lg += HelperNumber.toLong(string.substring(from, to)) << (2 << count);
			from = to + 1;
		}

		return lg;
	}

	/**
	 * IPV4转换数字
	 * 
	 * @param iPAddress
	 * @return
	 */
	public static long longIPV4(String iPAddress) {
		return longIP(iPAddress, 4);
	}

	/**
	 * 数字转换IPV4
	 * 
	 * @param longIP
	 * @return
	 */
	public static String longIPV4(long longIP) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(longIP >>> 24));
		sb.append('.');
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append('.');
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append('.');
		sb.append(String.valueOf(longIP & 0x000000FF));
		sb.append('.');
		return sb.toString();
	}
}
