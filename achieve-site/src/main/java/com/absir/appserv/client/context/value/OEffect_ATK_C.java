/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:37:04
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.IResult;

/**
 * 连续攻击
 * 
 * @author absir
 * 
 */
public class OEffect_ATK_C extends OEffect_Fight {

	// 攻击伤害比例
	float atkP;

	// 连续攻击次数
	int atkC;

	/**
	 * @param parameters
	 */
	public OEffect_ATK_C(String[] parameters) {
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
		int atk = (int) (self.getAtk() * atkP);
		for (int i = 0; i < atkC; i++) {
			self.atk(target, FightServiceUtils.aioiGramsAtk(camp, target, atk), camp, result);
			if (target.getHp() <= 0) {
				break;
			}
		}

		result.setDone(true);
	}

}
