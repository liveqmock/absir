/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-30 下午12:25:03
 */
package com.absir.server.exception;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
public class ServerException extends RuntimeException {

	/** serverStatus */
	private ServerStatus serverStatus;

	/** exceptionData */
	private Object exceptionData;

	/**
	 * @param serverStatus
	 */
	public ServerException(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	/**
	 * @param serverStatus
	 * @param exceptionData
	 */
	public ServerException(ServerStatus serverStatus, Object exceptionData) {
		this(serverStatus);
		this.exceptionData = exceptionData;
	}

	/**
	 * @return the serverStatus
	 */
	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	/**
	 * @return the exceptionData
	 */
	public Object getExceptionData() {
		return exceptionData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		String s = getClass().getName();
		String message = getLocalizedMessage();
		return (message != null) ? (s + ": " + message + ":" + serverStatus) : (s + ":" + serverStatus);
	}
}
