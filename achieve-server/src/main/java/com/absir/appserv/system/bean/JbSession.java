/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-11 下午1:03:49
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.absir.appserv.system.bean.base.JbVerifier;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public abstract class JbSession extends JbVerifier {

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("用户名")
	private String username;

	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	@JaLang("最后登录")
	private long lastTime;

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("请求地址")
	private Long addrest;

	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaLang("请求路径")
	private String url;

	@JaLang("请求来源")
	private String agent;

	@JaLang("附加信息")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonMap")
	private Map<String, Serializable> metas;

	/**
	 * @return
	 */
	public abstract JiUserBase getUser();

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
	 * @return the addrest
	 */
	public Long getAddrest() {
		return addrest;
	}

	/**
	 * @param addrest
	 *            the addrest to set
	 */
	public void setAddrest(Long addrest) {
		this.addrest = addrest;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @param name
	 * @return
	 */
	public Serializable getMeta(String name) {
		return metas == null ? null : metas.get(name);
	}

	/**
	 * @param name
	 */
	public void removeMeta(String name) {
		if (metas != null) {
			metas.remove(name);
		}
	}

	/**
	 * @return the meta
	 */
	public void setMeta(String name, Serializable value) {
		if (metas == null) {
			synchronized (this) {
				metas = new HashMap<String, Serializable>();
			}
		}

		metas.put(name, value);
	}
}
