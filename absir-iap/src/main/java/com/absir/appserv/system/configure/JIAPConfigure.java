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
@MaEntity(parent = { @MaMenu("接口配置") }, name = "IAP")
public class JIAPConfigure extends JConfigureBase {

	@JaLang("APPID")
	private String bunlderId = "";

	@JaLang("沙盒测试")
	private boolean sandbox;

	/**
	 * @return the bunlderId
	 */
	public String getBunlderId() {
		return bunlderId;
	}

	/**
	 * @param bunlderId
	 *            the bunlderId to set
	 */
	public void setBunlderId(String bunlderId) {
		this.bunlderId = bunlderId;
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
