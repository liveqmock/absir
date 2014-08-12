/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:38:40
 */
package com.absir.appserv.client.context.value;

import java.util.Arrays;
import java.util.List;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.core.dyna.DynaBinder;

/**
 * 题目阵营指定变换
 * 
 * @author absir
 * 
 */
public class OEffect_CAM_C extends OEffectBase {

	// 变化数量
	int cateCount;

	// 阵营类型数量
	int campCount;

	// 变化阵营目标
	transient JeCamp[] camps;

	/**
	 * @param parameters
	 */
	public OEffect_CAM_C(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
		int length = parameters.length;
		if (length > 2) {
			camps = DynaBinder.to(Arrays.copyOfRange(parameters, 2, length), JeCamp[].class);
		}
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
		OCampCategory[] campCategories = self.currentFight().getCampQuestions();
		int length = campCategories.length;
		boolean[] cb = new boolean[length];
		for (int i = 0; i < campCount; i++) {
			int j = HelperRandom.nextInt(length);
			int last = j + length;
			for (; j < last; j++) {
				int k = j < length ? j : j - length;
				if (!cb[k]) {
					cb[k] = true;
					campCategories[k].camps = FightServiceUtils.getJeCamps(campCount, camps);
					break;
				}
			}
		}

		self.addReportDetail(null, getEffectName(), campCategories);
	}
}
