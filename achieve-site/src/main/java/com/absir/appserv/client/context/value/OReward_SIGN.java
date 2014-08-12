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
public class OReward_SIGN extends OReward {

	/** day */
	private int day;

	/**
	 * @param rewardDefine
	 */
	public OReward_SIGN(XRewardDefine rewardDefine) {
		super(rewardDefine);
		// TODO Auto-generated constructor stub
		day--;
	}

	/**
	 * @return
	 */
	public String getRewardId() {
		return "signDay";
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
		int signDay = recard == null ? 0 : recard;
		int onlineDay = playerContext.getPlayerA().getOnlineDay();
		if (signDay < onlineDay) {
			Integer sign = (onlineDay > signDay + 1) ? null : playerContext.getPlayerA().getMetaRecards().get("sign");
			signDay = sign == null ? 0 : sign;
			if (signDay >= day) {
				playerContext.getPlayerA().getMetaRecards().put("sign", day + 1);
				return playerContext.getPlayerA().getOnlineDay();
			}
		}

		return null;
	}
}
