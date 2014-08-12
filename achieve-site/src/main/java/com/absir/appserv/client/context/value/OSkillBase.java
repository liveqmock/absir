/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-18 上午10:14:57
 */
package com.absir.appserv.client.context.value;

import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OSkill;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class OSkillBase extends OSkill<OCardBase> {

	/** xSkillDefine */
	XSkillDefine skillDefine;

	/** oEffectBase */
	OEffectBase effectBase;

	/** OEFFECT_BASE_NAME */
	public static final String OEFFECT_BASE_NAME = KernelString.rightSubString(OEffectBase.class.getName(), 4) + "_";

	/**
	 * @param skillDefine
	 */
	@SuppressWarnings("unchecked")
	public OSkillBase(XSkillDefine skillDefine) {
		this.skillDefine = skillDefine;
		effectBase = KernelClass.newInstance(KernelClass.forName(OEFFECT_BASE_NAME + skillDefine.getEffectType()), null, new Object[] { skillDefine.getEffectParameters() });
		if (effectBase != null) {
			effectBase.setEffectName(String.format("Skill%03d", skillDefine.getId()));
		}
	}

	/**
	 * @param self
	 * @param result
	 * @return
	 */
	public boolean isTrigger(OCardBase self, IResult result) {
		return super.isTrigger(self, result) && effectBase != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OSkill#effect(com.absir.appserv.game.value
	 * .OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void effect(OCardBase self, IResult result) {
		// TODO Auto-generated method stub
		boolean targetType = skillDefine.getTargetType() == 0;
		OCardBase[] cardBases = self.inTarget() ^ targetType ? self.currentFight().getTargetCards() : self.currentFight().getCards();
		List<OCardBase> targetCards;
		if (skillDefine.getTargetCamps() == null) {
			targetCards = KernelArray.toList(cardBases);

		} else {
			targetCards = new ArrayList<OCardBase>();
			for (OCardBase cardBase : cardBases) {
				if (KernelArray.contain(skillDefine.getTargetCamps(), cardBase.getCard().getCardDefine().getCamp())) {
					targetCards.add(cardBase);
				}
			}
		}

		effectBase.effect(skillDefine.getCamp(), self, targetCards, targetType, skillDefine.getTargetCount(), result);
	}
}
