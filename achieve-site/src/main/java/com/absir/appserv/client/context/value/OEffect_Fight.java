/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 下午6:48:37
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
 * @author absir
 * 
 */
public abstract class OEffect_Fight extends OEffectBase {

	/**
	 * @param parameters
	 */
	public OEffect_Fight(String[] parameters) {
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
		OCardBase targetCard = targetType ? self.getTarget() : self;
		if (targetCard == null || targetCard.died()) {
			self.targetResult(result, true);
			targetCard = self.getTarget();
		}

		int targetleft = targetCard == null || scope(targetCard) ? 0 : targetCards.indexOf(targetCard);
		int targetRight = targetleft + 1;
		int targetSize = targetCards.size();
		List<OCardBase> targets = new ArrayList<OCardBase>();
		while (true) {
			boolean complete = true;
			if (targetleft >= 0) {
				complete = false;
				targetCard = targetCards.get(targetleft);
				targetleft--;
				if (!targetCard.died() && scope(targetCard)) {
					targets.add(targetCard);
					if (--targetCount == 0) {
						break;
					}
				}
			}

			if (targetRight < targetSize) {
				complete = false;
				targetCard = targetCards.get(targetRight);
				targetRight++;
				if (!targetCard.died() && scope(targetCard)) {
					targets.add(targetCard);
					if (--targetCount == 0) {
						break;
					}
				}
			}

			if (complete) {
				break;
			}
		}

		// 添加战报
		List<List<OReportDetail>> allDetails = new ArrayList<List<OReportDetail>>();
		self.currentFight().addReportDetail(self.getId(), HelperBase.getBaseIds(targets), getEffectName(), allDetails);
		for (OCardBase target : targets) {
			self.currentFight().pushDetails(allDetails);
			effect(camp, self, target, result);
			self.currentFight().popDetails();
		}
	}

	/**
	 * @param targetCard
	 * @return
	 */
	public boolean scope(OCardBase targetCard) {
		return !targetCard.isInvincible();
	}

	/**
	 * @param camp
	 * @param self
	 * @param target
	 * @param result
	 */
	public abstract void effect(JeCamp camp, OCardBase self, OCardBase target, IResult result);

}
