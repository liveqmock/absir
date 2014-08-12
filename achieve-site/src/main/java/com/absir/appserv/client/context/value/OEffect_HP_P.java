/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:38:05
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.IResult;

/**
 * 血量回复比例
 * 
 * @author absir
 * 
 */
public class OEffect_HP_P extends OEffect_Fight {

	// 恢复比例
	float hpP;

	/**
	 * @param parameters
	 */
	public OEffect_HP_P(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param targetCard
	 * @return
	 */
	public boolean scope(OCardBase targetCard) {
		return targetCard.getHp() < targetCard.getMaxHp();
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
		self.treat(target, FightServiceUtils.aioiGramsHp(camp, target, (int) (target.getMaxHp() * hpP)), camp, result);
	}

}
