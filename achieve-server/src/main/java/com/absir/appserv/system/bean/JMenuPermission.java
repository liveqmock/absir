/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-29 下午3:32:06
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JMenuPermission extends JbBase {

	@JaLang("访问链接")
	@Id
	private String id;

	@JaLang("授权用户")
	private long allowUserIds[];

	@JaLang("禁用用户")
	private long forbidUserIds[];

	/**
	 * @return the id
	 */
	public String getId() {
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
	 * @return the allowUserIds
	 */
	public long[] getAllowUserIds() {
		return allowUserIds;
	}

	/**
	 * @param allowUserIds
	 *            the allowUserIds to set
	 */
	public void setAllowUserIds(long[] allowUserIds) {
		this.allowUserIds = allowUserIds;
	}

	/**
	 * @return the forbidUserIds
	 */
	public long[] getForbidUserIds() {
		return forbidUserIds;
	}

	/**
	 * @param forbidUserIds
	 *            the forbidUserIds to set
	 */
	public void setForbidUserIds(long[] forbidUserIds) {
		this.forbidUserIds = forbidUserIds;
	}
}
