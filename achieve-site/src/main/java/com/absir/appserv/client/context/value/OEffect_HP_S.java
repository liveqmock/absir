/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:39:06
 */
package com.absir.appserv.client.context.value;

import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OReportDetail;
import com.absir.appserv.system.helper.HelperBase;

/**
 * 复活并回复HP
 * 
 * @author absir
 * 
 */
public class OEffect_HP_S extends OEffectBase {

	// 复活血量
	float hpP;

	/**
	 * @param parameters
	 */
	public OEffect_HP_S(String[] parameters) {
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
		List<OCardBase> targetBases = new ArrayList<OCardBase>();
		for (OCardBase target : targetCards) {
			if (target.died()) {
				targetBases.add(target);
				if (--targetCount <= 0) {
					break;
				}
			}
		}

		List<List<OReportDetail>> allDetails = new ArrayList<List<OReportDetail>>();
		self.currentFight().addReportDetail(self.getId(), HelperBase.getBaseIds(targetBases), getEffectName(), allDetails);
		for (OCardBase target : targetBases) {
			self.currentFight().pushDetails(allDetails);
			self.treat(target, (int) (target.getMaxHp() * hpP), camp, result);
			self.currentFight().popDetails();
		}
	}
}
