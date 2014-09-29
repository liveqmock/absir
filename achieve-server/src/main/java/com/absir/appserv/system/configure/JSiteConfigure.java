/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年8月28日 下午1:19:17
 */
package com.absir.appserv.system.configure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 *
 */
@MaEntity(parent = @MaMenu("网站设置"), name = "全局")
public class JSiteConfigure extends JConfigureBase {

	@JaLang("网站名称")
	private String sitename = "achieve server";

	@JaLang("允许注册")
	private boolean allowRegister = true;

	@JaLang("验证时间")
	private long verifyTime = 3600000;

	@JaLang("注册未激活")
	private boolean registerDisable;

	@JaLang("关键字")
	private String[] keywords = new String[] { "achieve", "server", "java", "web", "cms", "framework" };

	@JaLang("描述")
	private String discription = "achieve server is a java stack type web development framework, make as blog, bussiness, cms, game server";

	/**
	 * @return the sitename
	 */
	@Langs
	public String getSitename() {
		return sitename;
	}

	/**
	 * @param sitename
	 *            the sitename to set
	 */
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * @return the allowRegister
	 */
	public boolean isAllowRegister() {
		return allowRegister;
	}

	/**
	 * @param allowRegister
	 *            the allowRegister to set
	 */
	public void setAllowRegister(boolean allowRegister) {
		this.allowRegister = allowRegister;
	}

	/**
	 * @return the verifyTime
	 */
	public long getVerifyTime() {
		return verifyTime;
	}

	/**
	 * @param verifyTime
	 *            the verifyTime to set
	 */
	public void setVerifyTime(long verifyTime) {
		this.verifyTime = verifyTime;
	}

	/**
	 * @return the registerDisable
	 */
	public boolean isRegisterDisable() {
		return registerDisable;
	}

	/**
	 * @param registerDisable
	 *            the registerDisable to set
	 */
	public void setRegisterDisable(boolean registerDisable) {
		this.registerDisable = registerDisable;
	}

	/**
	 * @return the keywords
	 */
	@Langs
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the discription
	 */
	@Langs
	public String getDiscription() {
		return discription;
	}

	/**
	 * @param discription
	 *            the discription to set
	 */
	public void setDiscription(String discription) {
		this.discription = discription;
	}
}
