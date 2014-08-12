/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-18 下午1:10:26
 */
package com.absir.appserv.system.bean;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.proxy.JpUser;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Entity
public class JUserGroup extends JbBean implements JpUser {

	@JaLang("群组名称")
	private String name;

	@JaLang("成员名片")
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, JUserCard> userCards = new HashMap<String, JUserCard>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the userCards
	 */
	public Map<String, JUserCard> getUserCards() {
		return userCards;
	}

	/**
	 * @param userCards
	 *            the userCards to set
	 */
	public void setUserCards(Map<String, JUserCard> userCards) {
		this.userCards = userCards;
	}
}
