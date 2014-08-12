/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:56:47
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.context.value.OFightBase.OCardBase;

/**
 * 答对时间
 * 
 * @author absir
 * 
 */
public class OTrigger_TIME_CA extends OTriggerBase {

	// 正确回答时间
	long correctAnswerTime;

	/**
	 * @param parameters
	 */
	public OTrigger_TIME_CA(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
		correctAnswerTime *= 1000;
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
		return self.currentFight().answerTime <= correctAnswerTime;
	}
}
