/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-28 上午10:38:29
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_ATK_R;

/**
 * @author absir
 * 
 */
public class OEffect_ATK_REBOUND extends OEffect_Fight {

	// 反弹伤害比例
	float atkR;

	int round;

	/**
	 * @param parameters
	 */
	public OEffect_ATK_REBOUND(String[] parameters) {
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
	 * com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void effect(JeCamp camp, OCardBase self, OCardBase target, IResult result) {
		// TODO Auto-generated method stub
		OBuff_ATK_R atk_R = new OBuff_ATK_R();
		atk_R.setName(effectName);
		atk_R.setAtkR(atkR);
		atk_R.setRound(round);
		target.addBuff(atk_R, result);
	}

}
