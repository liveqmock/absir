/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-29 上午11:02:30
 */
package com.absir.appserv.configure.xls;

import com.absir.appserv.system.bean.proxy.JiUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author absir
 * 
 */
public class XlsBaseUpdate extends XlsBase implements JiUpdate {

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
