/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-25 上午11:46:27
 */
package com.absir.appserv.system.bean;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.proxy.JiUpdate;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbResource extends JbBase implements JiUpdate {

	@Id
	String id;

	@JsonIgnore
	private long updateTime;

	@JsonIgnore
	private boolean scanned;

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
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the scanned
	 */
	public boolean isScanned() {
		return scanned;
	}

	/**
	 * @param scanned
	 *            the scanned to set
	 */
	public void setScanned(boolean scanned) {
		this.scanned = scanned;
	}
}
