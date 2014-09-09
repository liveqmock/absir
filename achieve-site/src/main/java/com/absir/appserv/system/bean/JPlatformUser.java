/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-10 下午11:30:34
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Index;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.base.JbUserRole;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.proxy.JiUserRole;
import com.absir.appserv.system.bean.proxy.JpMeta;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "serial" })
@MaEntity(parent = { @MaMenu("游戏管理") }, name = "用户")
@Entity
public class JPlatformUser extends JbBean implements JiUserBase, Serializable, JpMeta {

	@JaLang("平台名称")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	@JaColum(indexs = @Index(columnList = "platform,username", unique = true))
	private String platform;

	@JaLang("用户名")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private String username;

	@JaLang("禁用")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private boolean disabled;

	@JaLang("服务区")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private long serverId;

	@JaLang("角色ID")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private Long playerId;

	@JaLang("服务区纪录")
	private long[] serverIds;

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
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the serverId
	 */
	public long getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the playerId
	 */
	public Long getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the serverIds
	 */
	public long[] getServerIds() {
		return serverIds;
	}

	/**
	 * @param serverIds
	 *            the serverIds to set
	 */
	public void setServerIds(long[] serverIds) {
		this.serverIds = serverIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.Proxies.JpUserBase#getUserId()
	 */
	@Override
	public Long getUserId() {
		// TODO Auto-generated method stub
		return getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiUserBase#isDeveloper()
	 */
	@Override
	public boolean isDeveloper() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiUserBase#isActivation()
	 */
	@Override
	public boolean isActivation() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiUserBase#getUserRoleLevel()
	 */
	@Override
	public int getUserRoleLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiUserBase#userRoles()
	 */
	@Override
	public Collection<? extends JiUserRole> userRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiUserBase#getUserRoles()
	 */
	@Override
	public Collection<? extends JbUserRole> getUserRoles() {
		// TODO Auto-generated method stub
		return null;
	}
}
