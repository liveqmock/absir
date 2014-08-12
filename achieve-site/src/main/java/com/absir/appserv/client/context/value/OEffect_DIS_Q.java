/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:41:53
 */
package com.absir.appserv.client.context.value;

import java.util.List;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * 消除错误答案
 * 
 * @author absir
 * 
 */
public class OEffect_DIS_Q extends OEffectBase {

	// 消除数量
	int disCount;

	/**
	 * @param parameters
	 */
	public OEffect_DIS_Q(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.OEffectBase#effect(com.absir.appserv
	 * .client.bean.value.JeCamp,
	 * com.absir.appserv.client.context.value.OFightBase.OCardBase,
	 * java.util.List, boolean, int, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void effect(JeCamp camp, OCardBase self, List<OCardBase> targetCards, boolean targetType, int targetCount, IResult result) {
		// TODO Auto-generated method stub
		OFightBase fightBase = self.currentFight();
		if (fightBase.question == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		int size = 4;
		for (int i = 0; i < disCount; i++) {
			int j = HelperRandom.nextInt(size);
			int last = j + size;
			for (; j < last; j++) {
				int k = j < size ? j : j - size;
				if (!(k == fightBase.question.getCorrect() || fightBase.disIndexs.contains(k))) {
					fightBase.disIndexs.add(k);
					break;
				}
			}
		}

		self.addReportDetail(null, getEffectName(), fightBase.disIndexs);
	}
}
