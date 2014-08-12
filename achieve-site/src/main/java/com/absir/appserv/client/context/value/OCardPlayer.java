/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-6 下午12:34:14
 */
package com.absir.appserv.client.context.value;

import java.io.Serializable;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.client.service.SocialService;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OBuff_Invincible;
import com.absir.core.kernel.KernelArray;

/**
 * @author absir
 * 
 */
public class OCardPlayer extends OCardBase {

	/** ssEnable */
	boolean ssEnable;

	/** ss */
	// OSkillSs skillSs;

	/** ENCOURAGE */
	private static final String ENCOURAGE = "E";

	/**
	 * @param oFightBase
	 * @param id
	 * @param card
	 */
	public OCardPlayer(OFightBase oFightBase, Serializable id, JCard card) {
		oFightBase.super(id, card);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OObject#die(com.absir.appserv.game.value
	 * .IResult)
	 */
	@Override
	public void die(IResult result) {
		OFightBase fightBase = currentFight();
		if (!fightBase.encourage) {
			// 获取应援
			fightBase.encourage = true;
			JCard card = SocialService.ME.getEncourageCard(fightBase.playerContext.getPlayer());
			if (card != null) {
				Integer id = (Integer) getId();
				OCardPlayer cardPlayer = new OCardPlayer(fightBase, id, card);
				addReportDetail(null, ENCOURAGE, cardPlayer);
				currentFight().getCards()[id] = cardPlayer;
				cardPlayer.ssEnable = true;
				// 设置无敌一个回合
				OBuff_Invincible buff_Invincible = new OBuff_Invincible();
				buff_Invincible.setName("INV");
				buff_Invincible.setRound(1);
				cardPlayer.addBuff(buff_Invincible, result);
				// cardPlayer.ss(result);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OCard#inTarget()
	 */
	@Override
	public boolean inTarget() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OCard#atk()
	 */
	@Override
	public boolean atk() {
		OFightBase fightBase = currentFight();
		return fightBase.getReportResult().answer == fightBase.getReportResult().correct && KernelArray.contain(fightBase.campCategory.camps, getCard().getCardDefine().getCamp()) && super.atk();
	}

	/**
	 * @param correct
	 */
	public void ssCorrect(int ssCorrect) {
		// if (!ssEnable && skillSs != null) {
		// if (ssCorrect >= skillSs.getSsCorrect()) {
		// ssEnable = true;
		// }
		// }
	}

	/**
	 * 释放SS技能
	 * 
	 * @param result
	 */
	public void ss(IResult result) {
		// if (skillSs != null) {
		// if (ssEnable && skillSs.isTrigger(this, result)) {
		// ssEnable = false;
		// skillSs.effect(this, result);
		// }
		// }
	}
}
