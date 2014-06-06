/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-11 下午1:03:49
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("在线管理") }, name = "会话")
@Entity
public class JSession extends JbSession {

	@JaLang("用户参数")
	private String userParameter;

	/**
	 * @return the userParameter
	 */
	public String getUserParameter() {
		return userParameter;
	}

	/**
	 * @param userParameter
	 *            the userParameter to set
	 */
	public void setUserParameter(String userParameter) {
		this.userParameter = userParameter;
	}
}
