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
@SuppressWarnings("rawtypes")
public class OReward_LEVEL extends OReward {

	/** level */
	private int level;

	/**
	 * @param parameters
	 */
	public OReward_LEVEL(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.context.value.OReward#reward(com.absir.appserv
	 * .game.context.JbPlayerContext, java.lang.Integer)
	 */
	@Override
	public Integer reward(JbPlayerContext playerContext, Integer recard) {
		// TODO Auto-generated method stub
		if (recard != null) {
			return null;
		}

		return playerContext.getPlayer().getLevel() < level ? null : 0;
	}
}
