/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午4:13:43
 */
package com.absir.system.test.bean;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Test;

import com.absir.appserv.system.helper.HelperRandom;
import com.absir.core.base.IBase;
import com.absir.system.test.AbstractTest;

/**
 * @author absir
 * 
 */
public class TestTreeMap extends AbstractTest {

	public static class TreeKey implements IBase<Serializable> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.system.bean.proxy.JiBase#getId()
		 */
		@Override
		public Serializable getId() {
			// TODO Auto-generated method stub
			return HelperRandom.nextInt(0, 10);
		}
	}

	@Test
	public void main() throws IOException {

	}

}
