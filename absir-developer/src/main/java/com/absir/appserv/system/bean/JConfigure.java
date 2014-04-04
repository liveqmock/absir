/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-5 下午7:29:10
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.absir.appserv.system.bean.base.JbBase;

/**
 * @author absir
 * 
 */
@Entity
public class JConfigure extends JbBase {

	/** id */
	@Id
	private String id;

	/** value */
	private String value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.base.JbBase#getId()
	 */
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
