/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-3 下午12:05:47
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;

import com.absir.appserv.system.bean.base.JbBeanLS;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Entity
public class JShared extends JbBeanLS {

	@JaLang("分享时间")
	private long createTime;

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
