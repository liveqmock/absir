/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-15 下午1:25:58
 */
package com.absir.appserv.client.bean;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Index;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.client.bean.value.JeType;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.proxy.JpCreate;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaClasses;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏管理") }, name = "奖励")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class JPlayerReward extends JbBean implements JpCreate {

	@JaLang("关联用户")
	@JaClasses(JPlayer.class)
	@JaColum(indexs = @Index(name = "playerId", columnList = "playerId"))
	private long playerId;

	@JaLang("奖励实体")
	@Embedded
	private JbRewardBean rewardBean;

	@JaLang("奖励说明")
	private String name;

	@JaLang("奖励类型")
	private JeType type;

	@JaLang("奖励参数")
	private String data;

	/**
	 * @return the playerId
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the rewardBean
	 */
	public JbRewardBean getRewardBean() {
		return rewardBean;
	}

	/**
	 * @param rewardBean
	 *            the rewardBean to set
	 */
	public void setRewardBean(JbRewardBean rewardBean) {
		this.rewardBean = rewardBean;
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
}
