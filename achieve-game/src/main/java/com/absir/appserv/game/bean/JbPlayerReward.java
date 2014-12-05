/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月27日 上午9:51:09
 */
package com.absir.appserv.game.bean;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JaName;

/**
 * @author absir
 *
 */
@MappedSuperclass
public abstract class JbPlayerReward extends JbBean {

	@JaLang("角色编号")
	@JaName("JPlayer")
	private long playerId;

	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	@JaLang(value = "创建时间")
	private long createTime;

	@JaLang("名称")
	private String name;

	@JaLang("类型")
	private int type;

	@JaLang("数据")
	private String data;

	/**
	 * @return
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 */
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return
	 */
	public abstract JbReward getReward();

	/**
	 * @param reward
	 */
	public abstract void setReward(JbReward reward);

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
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
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
