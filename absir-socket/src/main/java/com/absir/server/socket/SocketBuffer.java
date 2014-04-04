/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-31 下午12:04:51
 */
package com.absir.server.socket;

import java.io.Serializable;

/**
 * @author absir
 * 
 */
public class SocketBuffer {

	/** id */
	private Serializable id;

	/** length */
	private int length;

	/** lengthIndex */
	private int lengthIndex;

	/** buffer */
	private byte[] buff;

	/** buffLengthIndex */
	private int buffLengthIndex;

	/**
	 * @return the id
	 */
	public Serializable getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Serializable id) {
		this.id = id;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the lengthIndex
	 */
	public int getLengthIndex() {
		return lengthIndex;
	}

	/**
	 * @param lengthIndex
	 *            the lengthIndex to set
	 */
	public void setLengthIndex(int lengthIndex) {
		this.lengthIndex = lengthIndex;
	}

	/**
	 * @return the buff
	 */
	public byte[] getBuff() {
		return buff;
	}

	/**
	 * @param buff
	 *            the buff to set
	 */
	public void setBuff(byte[] buff) {
		this.buff = buff;
	}

	/**
	 * @return the buffLengthIndex
	 */
	public int getBuffLengthIndex() {
		return buffLengthIndex;
	}

	/**
	 * @param buffLengthIndex
	 *            the buffLengthIndex to set
	 */
	public void setBuffLengthIndex(int buffLengthIndex) {
		this.buffLengthIndex = buffLengthIndex;
	}
}
