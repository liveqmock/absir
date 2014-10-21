/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-10 下午11:30:34
 */
package com.absir.appserv.game.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Index;

import org.hibernate.annotations.Type;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.base.JbUserRole;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.proxy.JiUserRole;
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
public class JPlatformUser extends JbBean implements JiUserBase, Serializable {

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
	 * @author absir 扩展存储
	 */
	@JaLang("扩展纪录")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonMap")
	private Map<String, String> metaMap;

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

	/**
	 * @return the metaMap
	 */
	public Map<String, String> getMetaMap() {
		return metaMap;
	}

	/**
	 * @param metaMap
	 *            the metaMap to set
	 */
	public void setMetaMap(Map<String, String> metaMap) {
		this.metaMap = metaMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.bean.proxy.JiUserBase#getMetaMap(java.lang.String
	 * )
	 */
	@Override
	public Object getMetaMap(String key) {
		// TODO Auto-generated method stub
		return metaMap == null ? null : metaMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.bean.proxy.JiUserBase#setMetaMap(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public void setMetaMap(String key, String value) {
		// TODO Auto-generated method stub
		if (metaMap == null) {
			metaMap = new LinkedHashMap<String, String>();
		}

		metaMap.put(key, value);
	}
}
