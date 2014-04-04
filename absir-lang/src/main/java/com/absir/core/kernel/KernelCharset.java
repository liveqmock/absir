/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-10 下午11:01:58
 */
package com.absir.core.kernel;

import java.nio.charset.Charset;

/**
 * @author absir
 * 
 */
public class KernelCharset {

	/** UTF8 */
	public static final Charset UTF8 = Charset.forName("UTF-8");

	/** DEFAULT */
	public static final Charset DEFAULT = UTF8;

	/** DEFAULT_ENCODER */
	public static final Object DEFAULT_ENCODER = DEFAULT.newEncoder();

	/** DEFAULT_DECODER */
	public static final Object DEFAULT_DECODER = DEFAULT.newDecoder();
}
