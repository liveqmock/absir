/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午5:34:56
 */
package com.absir.appserv.client.bean;

import javax.persistence.Embeddable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.system.bean.dto.IBaseSerializer;

/**
 * @author absir
 * 
 */
@Embeddable
public class JbSkill {

	/** skillDefine */
	@JsonSerialize(using = IBaseSerializer.class)
	private XSkillDefine skillDefine;

	/** locked */
	private boolean locked;

	/**
	 * @return the skillDefine
	 */
	public XSkillDefine getSkillDefine() {
		return skillDefine;
	}

	/**
	 * @param skillDefine
	 *            the skillDefine to set
	 */
	public void setSkillDefine(XSkillDefine skillDefine) {
		this.skillDefine = skillDefine;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
