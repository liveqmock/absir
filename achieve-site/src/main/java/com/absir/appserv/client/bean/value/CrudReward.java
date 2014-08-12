/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-23 下午6:26:11
 */
package com.absir.appserv.client.bean.value;

import com.absir.appserv.client.bean.JbRewardBean;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.crud.bean.CrudBean;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("内容管理"), @MaMenu("游戏管理") }, name = "奖励")
public class CrudReward extends JbRewardBean implements CrudBean {

	@JaLang("角色IDS")
	private long[] playerIds;

	@JaLang("服务区IDS")
	private long[] serverIds;

	@JaLang("奖励名称")
	private String name;

	@JaLang("奖励类型")
	private JeType type;

	@JaLang("奖励参数")
	private String data;

	/**
	 * @return the playerIds
	 */
	public long[] getPlayerIds() {
		return playerIds;
	}

	/**
	 * @param playerIds
	 *            the playerIds to set
	 */
	public void setPlayerIds(long[] playerIds) {
		this.playerIds = playerIds;
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
	 * @return the type
	 */
	public JeType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(JeType type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.bean.CrudBean#merge()
	 */
	@Override
	public void merge() {
		// TODO Auto-generated method stub
		PlayerService.ME.addPlayerReward(playerIds, serverIds, this, name, type, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.bean.CrudBean#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

}
