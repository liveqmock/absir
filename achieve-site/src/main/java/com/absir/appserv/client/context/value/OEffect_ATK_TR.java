/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:36:48
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_ATK_T;

/**
 * 攻击力提升固定值
 * 
 * @author absir
 * 
 */
public class OEffect_ATK_TR extends OEffect_Fight {

	// 提升攻击力
	int atkT;

	// 提升回合数
	int round;

	/**
	 * @param parameters
	 */
	public OEffect_ATK_TR(String[] parameters) {
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
		OBuff_ATK_T atk_T = new OBuff_ATK_T();
		atk_T.setAtkT(atkT);
		atk_T.setRound(round);
		target.addBuff(atk_T, result);
	}
}
