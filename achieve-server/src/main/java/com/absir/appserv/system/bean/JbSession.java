/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-11 下午1:03:49
 */
package com.absir.appserv.system.bean;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbVerifier;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public abstract class JbSession extends JbVerifier {

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("用户ID")
	private Long userId;

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("用户名")
	private String username;

	@JaEdit(types = "ip", groups = JaEdit.GROUP_LIST)
	@JaLang("地址")
	private long address;

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("设备")
	private String agent;

	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	@JaLang("最后登录")
	private long lastTime;

	@JaLang("附加信息")
	@Lob
	private byte[] metas;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the address
	 */
	public long getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(long address) {
		this.address = address;
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return the lastTime
	 */
	public long getLastTime() {
		return lastTime;
	}

	/**
	 * @param lastTime
	 *            the lastTime to set
	 */
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * @return the metas
	 */
	public byte[] getMetas() {
		return metas;
	}

	/**
	 * @param metas
	 *            the metas to set
	 */
	public void setMetas(byte[] metas) {
		this.metas = metas;
	}
}
