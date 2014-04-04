/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-28 下午6:51:32
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.proxy.JiUpdate;

/**
 * @author absir
 * 
 */
@Entity
public class JUpdateXls extends JbBase implements JiUpdate {

	@EmbeddedId
	private JEmbedSS id;

	/** updateTime */
	private long updateTime;

	/** serialize */
	@Lob
	private byte[] serialize;

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
	public void setId(JEmbedSS id) {
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
	 * @return the serialize
	 */
	public byte[] getSerialize() {
		return serialize;
	}

	/**
	 * @param serialize
	 *            the serialize to set
	 */
	public void setSerialize(byte[] serialize) {
		this.serialize = serialize;
	}
}
