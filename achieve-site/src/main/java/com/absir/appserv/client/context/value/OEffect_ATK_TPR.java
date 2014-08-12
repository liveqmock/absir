/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:37:37
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_ATK_TP;

/**
 * 攻击力提升比例值
 * 
 * @author absir
 * 
 */
public class OEffect_ATK_TPR extends OEffect_Fight {

	// 提升攻击力比例
	float atkTP;

	// 提升回合数
	int round;

	/**
	 * @param parameters
	 */
	public OEffect_ATK_TPR(String[] parameters) {
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
		OBuff_ATK_TP atk_TP = new OBuff_ATK_TP();
		atk_TP.setName(effectName);
		atk_TP.setAtkTP(atkTP);
		atk_TP.setRound(round);
		target.addBuff(atk_TP, result);
	}

}
