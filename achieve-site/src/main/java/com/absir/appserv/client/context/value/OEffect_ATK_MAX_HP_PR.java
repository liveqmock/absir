/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:35:50
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_MAX_HP_TP;

/**
 * 最大血量比例攻击
 * 
 * @author absir
 * 
 */
public class OEffect_ATK_MAX_HP_PR extends OEffect_Fight {

	float maxHpP;

	int round;

	/**
	 * @param parameters
	 */
	public OEffect_ATK_MAX_HP_PR(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.OEffect_Fight#effect(com.absir
	 * .appserv.client.bean.value.JeCamp,
	 * com.absir.appserv.client.context.value.OFightBase.OCardBase,
	 * com.absir.appserv.client.context.value.OFightBase.OCardBase,
	 * com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void effect(JeCamp camp, OCardBase self, OCardBase target, IResult result) {
		// TODO Auto-generated method stub
		OBuff_MAX_HP_TP max_HP_TP = new OBuff_MAX_HP_TP();
		max_HP_TP.setName(effectName);
		max_HP_TP.setMaxHpP(maxHpP);
		max_HP_TP.setRound(round);
		target.addBuff(max_HP_TP, result);
	}
}
