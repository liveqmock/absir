/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-28 下午6:52:41
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class JEmbedSS implements Serializable {

	/** eid */
	@JaLang("编号")
	private String eid;

	/** mid */
	@JaLang("关联")
	private String mid;

	/**
	 * @return the eid
	 */
	public String getEid() {
		return eid;
	}

	/**
	 * @param eid
	 *            the eid to set
	 */
	public void setEid(String eid) {
		this.eid = eid;
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return KernelObject.hashCode(eid) + KernelObject.hashCode(mid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof JEmbedSS) {
			JEmbedSS target = (JEmbedSS) obj;
			return KernelObject.equals(eid, target.eid) && KernelObject.equals(mid, target.mid);
		}

		return false;
	}
}
