/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月27日 上午9:51:09
 */
package com.absir.appserv.game.bean;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 *
 */
@MappedSuperclass
public abstract class JbPlayerReward {

	@JaLang("名称")
	private String name;

	@JaLang("数据")
	private String data;

	/**
	 * @return
	 */
	public abstract long getPlayerId();

	/**
	 * @param playerId
	 */
	public abstract void setPlayerId(long playerId);

	/**
	 * @return
	 */
	public abstract JbReward getReward();

	/**
	 * @param reward
	 */
	public abstract void setReward(JbReward reward);

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
