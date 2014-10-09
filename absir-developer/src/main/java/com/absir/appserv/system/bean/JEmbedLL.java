/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-1 下午3:36:28
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class JEmbedLL implements Serializable {

	/** eid */
	@JaEdit(groups = { JaEdit.GROUP_SUG, JaEdit.GROUP_SUGGEST })
	@JaLang("编号")
	private Long eid;

	/** mid */
	@JaEdit(groups = { JaEdit.GROUP_SUG, JaEdit.GROUP_SUGGEST })
	@JaLang("关联")
	private Long mid;

	/**
	 * 
	 */
	public JEmbedLL() {

	}

	/**
	 * @param eid
	 * @param mid
	 */
	public JEmbedLL(Long eid, long mid) {
		this.eid = eid;
		this.mid = mid;
	}

	/**
	 * @return the eid
	 */
	public Long getEid() {
		return eid;
	}

	/**
	 * @param eid
	 *            the eid to set
	 */
	public void setEid(Long eid) {
		this.eid = eid;
	}

	/**
	 * @return the mid
	 */
	public Long getMid() {
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(Long mid) {
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
		if (obj != null && obj instanceof JEmbedLL) {
			JEmbedLL target = (JEmbedLL) obj;
			return mid == target.mid && KernelObject.equals(eid, target.eid);
		}

		return false;
	}
}
