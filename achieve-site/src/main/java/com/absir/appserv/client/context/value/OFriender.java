/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-8 上午10:39:02
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.JFollow;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
public class OFriender {

	@JaLang("好友状态")
	private JFollow follow;

	/** player */
	@JaLang("好友角色")
	private JPlayer player;

	/**
	 * @param follow
	 */
	public OFriender(JFollow follow) {
		this.follow = follow;
	}

	/**
	 * @return the follow
	 */
	public JFollow getFollow() {
		return follow;
	}

	/**
	 * @param follow
	 *            the follow to set
	 */
	public void setFollow(JFollow follow) {
		this.follow = follow;
	}

	/**
	 * @return the player
	 */
	public JPlayer getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(JPlayer player) {
		this.player = player;
	}
}
