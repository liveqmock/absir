/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-3 下午1:22:17
 */
package com.absir.appserv.system.helper;

import javax.crypto.spec.SecretKeySpec;

import com.absir.core.kernel.KernelCharset;

/**
 * @author absir
 * 
 */
public class HelperCommon {

	/** SECRET_KEY_SPEC */
	public static final SecretKeySpec SECRET_KEY_SPEC = HelperEncrypt.getSecretKeySpec("AES", "appserv-frame");

	/**
	 * @param content
	 * @return
	 */
	public static String encrypt(String content) {
		byte[] buffer = HelperEncrypt.aesEncrypt(SECRET_KEY_SPEC, content.getBytes(KernelCharset.DEFAULT));
		return buffer == null ? null : new String(buffer, KernelCharset.DEFAULT);
	}

	/**
	 * @param content
	 * @return
	 */
	public static String decrypt(String content) {
		byte[] buffer = HelperEncrypt.aesDecrypt(SECRET_KEY_SPEC, content.getBytes(KernelCharset.DEFAULT));
		return buffer == null ? null : new String(buffer, KernelCharset.DEFAULT);
	}
}
