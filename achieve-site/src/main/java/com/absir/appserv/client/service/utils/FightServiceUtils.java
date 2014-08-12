/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 下午1:25:18
 */
package com.absir.appserv.client.service.utils;

import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JbSkill;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelCollection;

/**
 * @author absir
 * 
 */
public abstract class FightServiceUtils {

	/** jeCamps */
	private static final JeCamp[] JE_CAMPS = JeCamp.values();

	/**
	 * 生成卡牌
	 * 
	 * @param cardDefine
	 * @param playerContext
	 * @return
	 */
	public static JCard generate(XCardDefine cardDefine, PlayerContext playerContext) {
		JCard card = new JCard();
		card.setSkills(new ArrayList<JbSkill>());
		reset(card, cardDefine, playerContext);
		return card;
	}

	/**
	 * @param card
	 * @param cardDefine
	 */
	public static void reset(JCard card, XCardDefine cardDefine, PlayerContext playerContext) {
		card.setCardDefine(cardDefine);
		card.setHp(cardDefine.getHp());
		card.setAtk(cardDefine.getAtk());
		card.setWater(cardDefine.getWater());
		card.setFire(cardDefine.getFire());
		card.setThunder(cardDefine.getThunder());
		float levelf = (float) (cardDefine.getMaxLevel() - 1);
		card.setHpf((HelperRandom.nextInt(cardDefine.getMinHp(), cardDefine.getMaxHp()) - cardDefine.getHp()) / levelf);
		card.setAtkf((HelperRandom.nextInt(cardDefine.getMinAtk(), cardDefine.getMaxAtk()) - cardDefine.getAtk()) / levelf);
		card.setWaterf((HelperRandom.nextInt(cardDefine.getMinWater(), cardDefine.getMaxWater()) - cardDefine.getWater()) / levelf);
		card.setFiref((HelperRandom.nextInt(cardDefine.getMinFire(), cardDefine.getMaxFire()) - cardDefine.getFire()) / levelf);
		card.setThunderf((HelperRandom.nextInt(cardDefine.getMinThunder(), cardDefine.getMaxThunder()) - cardDefine.getThunder()) / levelf);
		card.setLevel(1);
		card.setSkillm(1);
	}

	/**
	 * 获取卡牌
	 * 
	 * @param card
	 * @return
	 */
	public static void gain(JCard card) {
		PlayerContext.modifyCardLevel(card, 1);
		card.setSkillm(1);
		synchronized (card.getSkills()) {
			card.getSkills().clear();
		}
	}

	/**
	 * 获取题目属性
	 * 
	 * @param campCount
	 * @return
	 */
	public static JeCamp[] getJeCamps(int campCount) {
		return getJeCamps(campCount, null);
	}

	/**
	 * 获取题目属性
	 * 
	 * @param campCount
	 * @param camps
	 * @return
	 */
	public static JeCamp[] getJeCamps(int campCount, JeCamp[] camps) {
		if (campCount < 1) {
			campCount = 1;
		}

		if (camps != null && campCount == camps.length) {
			return camps;
		}

		int length = JE_CAMPS.length;
		List<JeCamp> jeCamps = camps == null ? new ArrayList<JeCamp>(campCount) : KernelArray.toList(camps);
		for (int i = jeCamps.size(); i < campCount; i++) {
			int j = HelperRandom.nextInt(length);
			int last = j + length;
			for (; j < last; j++) {
				int k = j < length ? j : j - length;
				JeCamp camp = JE_CAMPS[k];
				if (!jeCamps.contains(camp)) {
					jeCamps.add(camp);
					break;
				}
			}
		}

		return KernelCollection.toArray(jeCamps, JeCamp.class);
	}

	/**
	 * 属性相克
	 * 
	 * @param camp
	 * @param target
	 * @return
	 */
	public static boolean aioiGrams(JeCamp camp, OCardBase target) {
		JeCamp targetCamp = target.getCard().getCardDefine().getCamp();
		return camp != targetCamp && camp.aioiGramsCamp(targetCamp) > 1.0f;
	}

	/**
	 * 相生治疗
	 * 
	 * @param camp
	 * @param target
	 * @param hp
	 * @return
	 */
	public static int aioiGramsHp(JeCamp camp, OCardBase target, int hp) {
		if (camp == target.getCard().getCardDefine().getCamp()) {
			hp *= 1.5f;
		}

		return hp;
	}

	/**
	 * @param hp
	 * @param camp
	 * @return
	 */
	public static int aioiGramsHp(OCardBase self, OCardBase target, int hp) {
		return aioiGramsHp(self.getCard().getCardDefine().getCamp(), target, hp);
	}

	/**
	 * 相生、相克攻击
	 * 
	 * @param camp
	 * @param target
	 * @param atk
	 * @return
	 */
	public static int aioiGramsAtk(JeCamp camp, OCardBase target, int atk) {
		JeCamp targetCamp = target.getCard().getCardDefine().getCamp();
		atk = atk * (100 - targetCamp.getDefence(target.getCard())) / 100;
		if (camp != targetCamp) {
			atk *= camp.aioiGramsCamp(camp);
		}

		return atk;
	}

	/**
	 * @param atk
	 * @param camp
	 * @return
	 */
	public static int aioiGramsAtk(OCardBase self, OCardBase target, int atk) {
		return aioiGramsAtk(self.getCard().getCardDefine().getCamp(), target, atk);
	}
}
