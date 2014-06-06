/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-17 下午4:05:06
 */
package com.absir.appserv.system.bean;

import javax.persistence.Embeddable;

import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Embeddable
public class JUserCard {

	@JaLang("昵称")
	private String nickname;

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
