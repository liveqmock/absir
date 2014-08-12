/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:38:54
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_PLA;

/**
 * 延迟攻击
 * 
 * @author absir
 * 
 */
public class OEffect_PLA extends OEffect_Fight {

	// 延迟回合
	int pla;

	/**
	 * @param parameters
	 */
	public OEffect_PLA(String[] parameters) {
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
		OBuff_PLA buff_PLA = new OBuff_PLA();
		buff_PLA.setName(effectName);
		buff_PLA.setRound(pla);
		target.addBuff(buff_PLA, result);
	}

}
