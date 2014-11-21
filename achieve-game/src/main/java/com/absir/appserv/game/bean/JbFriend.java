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

/**
 * @author absir
 *
 */
@MappedSuperclass
public abstract class JbFriend extends JbBeanLL {

	// 主动添加
	private boolean accord;

	// 角色
	public abstract JbPlayer getPlayer();

	public abstract void setPlayer(JbPlayer player);

	// 角色对象
	public abstract JbPlayer getTarget();

	public abstract void setTarget(JbPlayer target);

	/**
	 * @return the accord
	 */
	public boolean isAccord() {
		return accord;
	}

	/**
	 * @param accord
	 *            the accord to set
	 */
	public void setAccord(boolean accord) {
		this.accord = accord;
	}
}
