/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午5:27:16
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.configure.xls.XRewardDefine;
import com.absir.appserv.client.context.PlayerContext;

/**
 * @author absir
 * 
 */
public class OReward_ONLINE extends OReward {

	/** level */
	private int online;

	/**
	 * @param rewardDefine
	 */
	public OReward_ONLINE(XRewardDefine rewardDefine) {
		super(rewardDefine);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.OReward#reward(com.absir.appserv
	 * .client.context.JPlayerContext)
	 */
	@Override
	public Integer reward(PlayerContext playerContext, Integer recard) {
		// TODO Auto-generated method stub
		if (recard != null) {
			return null;
		}

		return playerContext.getPlayerA().getOnline() < online ? null : playerContext.getPlayerA().getOnlineDay();
	}
}
