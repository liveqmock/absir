/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 下午12:16:42
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.configure.xls.XRewardDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.system.context.value.ObjectParameters;

/**
 * @author absir
 * 
 */
public abstract class OReward extends ObjectParameters {

	/** rewardDefine */
	protected transient XRewardDefine rewardDefine;

	/**
	 * @param rewardDefine
	 */
	public OReward(XRewardDefine rewardDefine) {
		super(rewardDefine.getRewardParams());
		this.rewardDefine = rewardDefine;
	}

	/**
	 * @return
	 */
	public String getRewardId() {
		return null;
	}

	/**
	 * 奖励条件
	 * 
	 * @param playerContext
	 * @param recard
	 * @return
	 */
	public abstract Integer reward(PlayerContext playerContext, Integer recard);
}
