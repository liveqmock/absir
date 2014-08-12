/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:58:45
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.context.value.OFightBase.OCardBase;

/**
 * 答对总数
 * 
 * @author absir
 * 
 */
public class OTrigger_NUM_CA extends OTriggerBase {

	// 答对次数
	int correctCount;

	/**
	 * @param parameters
	 */
	public OTrigger_NUM_CA(String[] parameters) {
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
		return self.currentFight().correctCount > correctCount;
	}
}
