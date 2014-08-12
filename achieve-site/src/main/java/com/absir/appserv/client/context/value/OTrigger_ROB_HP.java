/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:57:48
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.context.value.OFightBase.OCardBase;

/**
 * 敌人血量
 * 
 * @author absir
 * 
 */
public class OTrigger_ROB_HP extends OTriggerBase {

	// 最小血量
	float hpP;

	/**
	 * @param parameters
	 */
	public OTrigger_ROB_HP(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OTrigger#isTrigger(com.absir.appserv.game
	 * .value.OObject)
	 */
	@Override
	public boolean isTrigger(OCardBase self) {
		// TODO Auto-generated method stub
		OCardBase target = self.getTarget();
		return target.getHp() <= target.getMaxHp() * hpP;
	}

}
