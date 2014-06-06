/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-6-3 下午3:34:42
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.system.bean.base.JbVerifier;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Entity
public class JYiDongSession extends JbVerifier {

	@JaLang("用户伪码")
	private String userId;

	@JaLang("渠道代码")
	private String channelId;

	@JaLang("透传参数")
	private String p;

	@JaLang("归属地")
	private String region;

	@JaLang("用户手机型号")
	private String Ua;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId
	 *            the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the p
	 */
	public String getP() {
		return p;
	}

	/**
	 * @param p
	 *            the p to set
	 */
	public void setP(String p) {
		this.p = p;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the ua
	 */
	public String getUa() {
		return Ua;
	}

	/**
	 * @param ua
	 *            the ua to set
	 */
	public void setUa(String ua) {
		Ua = ua;
	}
}
