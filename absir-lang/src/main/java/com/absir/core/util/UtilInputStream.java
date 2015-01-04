/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年12月29日 下午9:33:15
 */
package com.absir.core.util;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author absir
 *
 */
public class UtilInputStream extends InputStream {

	/** dataInput */
	DataInput dataInput;

	/**
	 * @param dataInput
	 */
	public UtilInputStream(DataInput dataInput) {
		// TODO Auto-generated constructor stub
		this.dataInput = dataInput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		try {
			return dataInput.readByte() & 0xff;

		} catch (EOFException e) {
			// TODO: handle exception
			return -1;
		}
	}

}
