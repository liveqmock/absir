/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-29 上午11:03:12
 */
package com.absir.appserv.configure.xls;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.system.bean.proxy.JiUpdate;

/**
 * @author absir
 * 
 */
public class XlsBeanUpdate<T extends Serializable> extends XlsBean<T> implements JiUpdate {

	/** updateTime */
	private transient long updateTime;

	/**
	 * @return the updateTime
	 */
	@JsonIgnore
	public long getUpdateTime() {
		return updateTime;
	}
}
