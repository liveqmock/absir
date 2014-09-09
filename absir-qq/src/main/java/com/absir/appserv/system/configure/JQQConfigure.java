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
@MaEntity(parent = { @MaMenu("接口配置") }, name = "QQ")
public class JQQConfigure extends JConfigureBase {

	@JaLang("APPID")
	private String appid;

	@JaLang("Product Key")
	private String appkey;

	@JaLang("Product Secret")
	private String appSecret;

	@JaLang("沙盒测试")
	private boolean sandbox;

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid
	 *            the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * @return the appkey
	 */
	public String getAppkey() {
		return appkey;
	}

	/**
	 * @param appkey
	 *            the appkey to set
	 */
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	/**
	 * @return the appSecret
	 */
	public String getAppSecret() {
		return appSecret;
	}

	/**
	 * @param appSecret
	 *            the appSecret to set
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * @return the sandbox
	 */
	public boolean isSandbox() {
		return sandbox;
	}

	/**
	 * @param sandbox
	 *            the sandbox to set
	 */
	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}

}
