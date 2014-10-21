/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午5:27:16
 */
package com.absir.appserv.game.context.value;

import com.absir.appserv.game.context.JbPlayerContext;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OReward_SIGN extends OReward {

	/** day */
	private int day;

	/**
	 * @param parameters
	 */
	public OReward_SIGN(String[] parameters) {
		super(parameters);
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
	public Integer reward(JbPlayerContext playerContext, Integer recard) {
		// TODO Auto-generated method stub
		int signDay = recard == null ? 0 : recard;
		int onlineDay = playerContext.getPlayerA().getOnlineDay();
		if (signDay < onlineDay) {
			Integer sign = (onlineDay > signDay + 1) ? null : (Integer) playerContext.getPlayerA().getMetaRecards().get("sign");
			signDay = sign == null ? 0 : sign;
			if (signDay >= day) {
				playerContext.getPlayerA().getMetaRecards().put("sign", day + 1);
				return playerContext.getPlayerA().getOnlineDay();
			}
		}

		return null;
	}
}
