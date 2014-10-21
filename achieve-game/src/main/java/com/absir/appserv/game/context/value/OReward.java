/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 下午12:16:42
 */
package com.absir.appserv.game.context.value;

import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.system.context.value.ObjectParameters;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class OReward extends ObjectParameters {

	/**
	 * @param parameters
	 */
	public OReward(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
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
	public abstract Integer reward(JbPlayerContext playerContext, Integer recard);
}
