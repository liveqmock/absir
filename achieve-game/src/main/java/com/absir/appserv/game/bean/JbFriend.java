/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月21日 下午2:24:58
 */
package com.absir.appserv.game.bean;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbBeanLL;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author absir
 *
 */
@MappedSuperclass
public abstract class JbFriend extends JbBeanLL {

	// 主动添加
	private int accord;

	// 角色
	@JsonIgnore
	public abstract JbPlayer getPlayer();

	public abstract void setPlayer(JbPlayer player);

	// 角色对象
	@JsonIgnore
	public abstract JbPlayer getTarget();

	public abstract void setTarget(JbPlayer target);

	/**
	 * @return the accord
	 */
	public int getAccord() {
		return accord;
	}

	/**
	 * @param accord
	 *            the accord to set
	 */
	public void setAccord(int accord) {
		this.accord = accord;
	}
}
