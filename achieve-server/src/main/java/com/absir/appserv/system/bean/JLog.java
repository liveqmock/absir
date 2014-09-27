/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月27日 下午12:18:52
 */
package com.absir.appserv.system.bean;

import java.util.Collection;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.service.BeanService;
import com.absir.context.core.ContextUtils;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 *
 */
@MaEntity(parent = { @MaMenu("系统管理") }, name = "日志")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JLog extends JbBean {

	@JaLang("名称")
	@JaColum(indexs = @Index(name = "name", columnList = "name"))
	private String name;

	@JaLang("动作")
	private String action;

	@JaLang("创建时间")
	private long createTime;

	@JaLang("IP地址")
	private String ip;

	@JaLang("用户名")
	private String username;

	@JaLang("成功")
	private boolean success;

	/**
	 * @param log
	 */
	public static void log(JLog log) {
		BeanService.ME.persist(log);
	}

	/**
	 * @param name
	 * @param action
	 * @param ip
	 * @param username
	 * @param success
	 * @return
	 */
	public static JLog log(String name, String action, String ip, String username, boolean success) {
		JLog log = new JLog(name, action, ip, username, success);
		log(log);
		return log;
	}

	/**
	 * @param logs
	 */
	public static void logs(Collection<JLog> logs) {
		BeanService.ME.persists(logs);
	}

	/**
	 * 
	 */
	public JLog() {
	}

	/**
	 * @param name
	 * @param action
	 * @param ip
	 * @param username
	 * @param success
	 */
	public JLog(String name, String action, String ip, String username, boolean success) {
		this.name = name;
		this.action = action;
		createTime = ContextUtils.getContextTime();
		this.ip = ip;
		this.username = username;
		this.success = success;
	}

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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
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
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
