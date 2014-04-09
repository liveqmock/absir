/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-30 下午7:27:41
 */
package com.absir.server.exception;

/**
 * @author absir
 * 
 */
public enum ServerStatus {

	/** IN_FAILED */
	IN_FAILED(0),

	/** ON_SUCCESS */
	ON_SUCCESS(200),

	/** ON_FAIL */
	ON_FAIL(201),

	/** NO_LOGIN */
	NO_LOGIN(202),

	/** NO_USER */
	NO_USER(203),

	/** NO_VERIFY */
	NO_VERIFY(204),

	/** ON_TIMEOUT */
	ON_TIMEOUT(301),

	/** ON_DELETED */
	ON_DELETED(302),

	/** ON_FORBIDDEN */
	ON_FORBIDDEN(303),

	/** ON_DENIED */
	ON_DENIED(304),

	/** NO_PARAM */
	NO_PARAM(400),

	/** ON_ERROR */
	ON_ERROR(401),

	/** IN_404 */
	IN_404(404),

	/** IN_405 */
	IN_405(405),

	;

	/** code */
	private int code;

	/**
	 * @param code
	 */
	ServerStatus(int code) {
		this.code = code;
	}

	/**
	 * @return
	 */
	public int getCode() {
		return code;
	}
}
