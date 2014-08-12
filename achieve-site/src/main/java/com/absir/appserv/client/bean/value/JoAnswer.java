/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-15 下午6:56:59
 */
package com.absir.appserv.client.bean.value;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JbAnswer;

/**
 * @author absir
 * 
 */
public class JoAnswer extends JbAnswer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiBase#getId()
	 */
	@JsonIgnore
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
