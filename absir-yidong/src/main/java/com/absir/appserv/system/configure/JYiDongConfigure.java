/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午1:15:32
 */
package com.absir.appserv.system.configure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("接口配置") }, name = "移动")
public class JYiDongConfigure extends JConfigureBase {

	@JaLang("合作方ID")
	private String cpId;

	@JaLang("计费代码")
	private String cpServiceId;

	@JaLang("登录过期时间")
	private long sessionLifeTime = 24 * 3600000;

	/**
	 * @return the cpId
	 */
	public String getCpId() {
		return cpId;
	}

	/**
	 * @param cpId
	 *            the cpId to set
	 */
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	/**
	 * @return the cpServiceId
	 */
	public String getCpServiceId() {
		return cpServiceId;
	}

	/**
	 * @param cpServiceId
	 *            the cpServiceId to set
	 */
	public void setCpServiceId(String cpServiceId) {
		this.cpServiceId = cpServiceId;
	}

	/**
	 * @return the sessionLifeTime
	 */
	public long getSessionLifeTime() {
		return sessionLifeTime;
	}

	/**
	 * @param sessionLifeTime
	 *            the sessionLifeTime to set
	 */
	public void setSessionLifeTime(long sessionLifeTime) {
		this.sessionLifeTime = sessionLifeTime;
	}

}
