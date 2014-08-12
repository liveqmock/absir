/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午1:07:41
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IBuffDefence;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuffFromRound;

/**
 * @author absir
 * 
 */
public class OBuff_Defence extends OBuffFromRound<JeCamp, OCardBase> implements IBuffDefence<JeCamp> {

	// 伤害减少
	JeCamp camp;

	// 伤害减少
	float defenceP;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.IBuffDefence#defence(int,
	 * java.lang.Object, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public int defence(int atk, JeCamp damageFrom, IResult result) {
		// TODO Auto-generated method stub
		int round = getRound();
		if (round < 0) {
			if (++round == 0) {
				result.setDone(true);
			}
		}

		if (camp == damageFrom) {
			atk -= atk * defenceP;
		}

		return atk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void stepRound(OCardBase self, long time, int round, IResult buffResult) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffReverse#revert(com.absir.appserv.game
	 * .value.OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void revert(OCardBase self, IResult result) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuff#effect(com.absir.appserv.game.value
	 * .OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void effect(OCardBase self, IResult result) {
		// TODO Auto-generated method stub

	}

}
