/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-3 下午1:43:26
 */
package com.absir.appserv.client.configure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏配置") }, name = "社交")
public class JSocialConfigure extends JConfigureBase {

	@JaLang("应援奖励")
	private int encourageFriendShip = 5;

	@JaLang("分享奖励")
	private int shareDiamondNumber = 6;

	/**
	 * @return the encourageFriendShip
	 */
	public int getEncourageFriendShip() {
		return encourageFriendShip;
	}

	/**
	 * @param encourageFriendShip
	 *            the encourageFriendShip to set
	 */
	public void setEncourageFriendShip(int encourageFriendShip) {
		this.encourageFriendShip = encourageFriendShip;
	}

	/**
	 * @return the shareDiamondNumber
	 */
	public int getShareDiamondNumber() {
		return shareDiamondNumber;
	}

	/**
	 * @param shareDiamondNumber
	 *            the shareDiamondNumber to set
	 */
	public void setShareDiamondNumber(int shareDiamondNumber) {
		this.shareDiamondNumber = shareDiamondNumber;
	}
}
