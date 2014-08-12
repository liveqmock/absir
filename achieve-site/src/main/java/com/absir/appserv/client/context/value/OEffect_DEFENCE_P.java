/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:39:23
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;

/**
 * 对阵营元素伤害减免
 * 
 * @author absir
 * 
 */
public class OEffect_DEFENCE_P extends OEffect_Fight {

	// 减少伤害
	float defenceP;

	// 提升回合数
	int round;

	/**
	 * @param parameters
	 */
	public OEffect_DEFENCE_P(String[] parameters) {
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
		OBuff_Defence buff = new OBuff_Defence();
		buff.setName(effectName);
		buff.camp = camp;
		buff.defenceP = defenceP;
		buff.setRound(round);
		target.addBuff(buff, result);
	}

}
