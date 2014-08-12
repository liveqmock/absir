/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午4:45:36
 */
package com.absir.appserv.client.service;

import java.util.List;
import java.util.Map;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JbSkill;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.IPropCard;
import com.absir.appserv.client.context.value.IPropPlayer;
import com.absir.appserv.client.context.value.IPropSkill;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@Bean
public class PropService {

	/** ME */
	public static final PropService ME = BeanFactoryUtils.get(PropService.class);

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param diamond
	 * @param size
	 * @return
	 */
	public int buyProp(PlayerContext playerContext, XPropDefine propDefine, boolean diamond, int size) {
		if (diamond) {
			if (propDefine.getDiamond() <= 0) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			playerContext.modifyDiamond(-propDefine.getDiamond() * size);

		} else {
			if (propDefine.getPrice() <= 0) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			playerContext.modifyMoney(-propDefine.getPrice() * size);
		}

		return addProp(playerContext, propDefine, size);
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param size
	 * @return
	 */
	public int sellProp(PlayerContext playerContext, XPropDefine propDefine, int size) {
		Map<Integer, Integer> propNumbers = playerContext.getPlayerA().getPropNumbers();
		int number;
		synchronized (propNumbers) {
			Integer propNumber = propNumbers.get(propDefine.getId());
			if (propNumber == null || propNumber < 1) {
				throw new ServerException(ServerStatus.ON_FAIL);
			}

			number = propNumber;
			if (size <= 0 || size > number) {
				size = number;
				number = 0;

			} else {
				number -= size;
			}

			if (number <= 0) {
				propNumbers.remove(propDefine.getId());

			} else {
				propNumbers.put(propDefine.getId(), number);
			}
		}

		if (propDefine.getPrice() > 0) {
			playerContext.modifyMoney((int) (propDefine.getPrice() * size * 0.8f));

		} else {
			playerContext.modifyDiamond((int) (propDefine.getDiamond() * size * 0.8f));
		}

		return number;
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param size
	 */
	public int addProp(PlayerContext playerContext, XPropDefine propDefine, int size) {
		Map<Integer, Integer> propNumbers = playerContext.getPlayerA().getPropNumbers();
		if (size < 0) {
			return propNumbers.get(propDefine.getId());
		}

		synchronized (propNumbers) {
			Integer number = propNumbers.get(propDefine);
			if (number != null) {
				size += number;
			}

			propNumbers.put(propDefine.getId(), size);
			return size;
		}
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param size
	 */
	public int useProp(PlayerContext playerContext, XPropDefine propDefine, int size) {
		Map<Integer, Integer> propNumbers = playerContext.getPlayerA().getPropNumbers();
		synchronized (propNumbers) {
			Integer number = propNumbers.get(propDefine.getId());
			if (number != null) {
				number -= size;
				if (number >= 0) {
					if (number == 0) {
						propNumbers.remove(propDefine.getId());

					} else {
						propNumbers.put(propDefine.getId(), number);
					}

					return number;
				}
			}
		}

		throw new ServerException(ServerStatus.IN_FAILED, "prop not enough");
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 */
	public void propPlayer(PlayerContext playerContext, XPropDefine propDefine) {
		Object propInvoker = propDefine.getPropInvoker();
		if (!(propInvoker instanceof IPropPlayer)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		useProp(playerContext, propDefine, 1);
		((IPropPlayer) propInvoker).prop(playerContext);
	}

	/**
	 * @param playerContext
	 * @param card
	 */
	public void openCard(PlayerContext playerContext, JCard card) {
		if (card.getSkillm() >= card.getCardDefine().getMaxSkill()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		playerContext.modifyMoney(5000 * (card.getCardDefine().getRare() + card.getSkillm() + 1));
		card.setSkillm(card.getSkillm() + 1);
	}

	/**
	 * @param playerContext
	 * @param card
	 * @param index
	 */
	public void lockCard(PlayerContext playerContext, JCard card, int index) {
		List<JbSkill> skills = card.getSkills();
		synchronized (skills) {
			if (index < 0 || index >= skills.size()) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			JbSkill skill = skills.get(index);
			if (skill.isLocked()) {
				skill.setLocked(false);

			} else {
				playerContext.modifyDiamond(-PlayerContext.PLAYER_CONFIGURE.getLockDiamond());
				skill.setLocked(true);
			}
		}
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param card
	 * @throws Throwable
	 */
	public void propCard(PlayerContext playerContext, XPropDefine propDefine, JCard card) throws Throwable {
		Object propInvoker = propDefine.getPropInvoker();
		if (!(propInvoker instanceof IPropCard)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		useProp(playerContext, propDefine, 1);
		try {
			((IPropCard) propInvoker).prop(card);

		} catch (Throwable e) {
			addProp(playerContext, propDefine, 1);
			throw e;
		}
	}

	/**
	 * @param playerContext
	 * @param propDefine
	 * @param card
	 * @param index
	 * @throws Throwable
	 */
	public void propSkill(PlayerContext playerContext, XPropDefine propDefine, JCard card, int index) throws Throwable {
		Object propInvoker = propDefine.getPropInvoker();
		if (!(propInvoker instanceof IPropSkill)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		useProp(playerContext, propDefine, 1);
		try {
			((IPropSkill) propInvoker).prop(card, index);

		} catch (Throwable e) {
			addProp(playerContext, propDefine, 1);
			throw e;
		}
	}
}
