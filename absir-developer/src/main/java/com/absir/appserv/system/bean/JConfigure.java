/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-5 下午7:29:10
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.system.bean.base.JbBeanSS;

/**
 * @author absir
 * 
 */
@Entity
public class JConfigure extends JbBeanSS {

	/** value */
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
