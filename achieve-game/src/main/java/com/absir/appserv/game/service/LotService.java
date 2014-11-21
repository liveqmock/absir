/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午3:05:10
 */
package com.absir.appserv.game.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbLotCard;
import com.absir.appserv.game.bean.JbPlayerA;
import com.absir.appserv.game.bean.value.ICardDefine;
import com.absir.appserv.game.confiure.JLotConfigure;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.system.bean.dto.IBeanCollectionSerializer;
import com.absir.appserv.system.domain.DActiver;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.async.value.Async;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextService;
import com.absir.context.core.ContextUtils;
import com.absir.orm.hibernate.boost.IEntityMerge;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Base
@Bean
public class LotService extends ContextService implements IEntityMerge<JbLotCard> {

	/** 对象单例访问 */
	public static final LotService ME = BeanFactoryUtils.get(LotService.class);

	/** 抽奖池参数 */
	public static final JLotConfigure LOT_CONFIGURE = JConfigureUtils.getConfigure(JLotConfigure.class);

	/** 抽奖池 */
	private DActiver<JbLotCard> lotCardActitity;

	/** 抽奖池卡组IDS */
	private Set<Long> lotCardIds;

	/** 品质卡牌抽奖组 */
	@JsonSerialize(contentUsing = IBeanCollectionSerializer.class)
	private Map<Integer, List<ICardDefine>> rareMapCard;

	/**
	 * 初始化服务
	 */
	@Inject
	protected void initService() {
		lotCardActitity = new DActiver<JbLotCard>("JLotCard");
		lotCardIds = new HashSet<Long>();
		rareMapCard = new HashMap<Integer, List<ICardDefine>>();
	}

	/**
	 * 获取抽奖池所有卡牌
	 */
	public Map<Integer, List<ICardDefine>> getRareMapCard() {
		return rareMapCard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextService#step(long)
	 */
	@Override
	public void step(long contextTime) {
		// TODO Auto-generated method stub
		if (lotCardActitity.stepNext(contextTime)) {
			ME.reload(contextTime);
		}
	}

	/**
	 * @param contextTime
	 */
	@Async(notifier = true)
	@Transaction(readOnly = true)
	public void reload(long contextTime) {
		lotCardIds.clear();
		Map<Integer, List<ICardDefine>> rareMapCard = new HashMap<Integer, List<ICardDefine>>();
		XlsDao<ICardDefine, Serializable> cardDefineDao = JbPlayerContext.COMPONENT.getCardDefineDao();
		for (JbLotCard lotCard : lotCardActitity.reloadActives(contextTime)) {
			lotCardIds.add(lotCard.getId());
			if (lotCard.getCardIds() != null) {
				for (int cardId : lotCard.getCardIds()) {
					ICardDefine cardDefine = cardDefineDao.get(cardId);
					if (cardDefine != null) {
						List<ICardDefine> cardDefines = rareMapCard.get(cardDefine.getRare());
						if (cardDefines == null) {
							cardDefines = new ArrayList<ICardDefine>();
							rareMapCard.put(cardDefine.getRare(), cardDefines);
						}

						if (!cardDefines.contains(cardDefine)) {
							cardDefines.add(cardDefine);
						}
					}
				}
			}
		}

		this.rareMapCard = rareMapCard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.hibernate.boost.IEntityMerge#merge(java.lang.String,
	 * java.lang.Object, com.absir.orm.hibernate.boost.IEntityMerge.MergeType,
	 * java.lang.Object)
	 */
	@Override
	public void merge(String entityName, JbLotCard entity, MergeType mergeType, Object mergeEvent) {
		// TODO Auto-generated method stub
		lotCardActitity.merge(entity, mergeType, mergeEvent);
	}

	/**
	 * 友情抽奖
	 * 
	 * @param playerContext
	 * @return
	 */
	public JbCard lotFriendShip(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.IN_FAILED, "cardNumber");
		}

		synchronized (playerContext) {
			long contextTime = ContextUtils.getContextTime();
			JbPlayerA playerA = playerContext.getPlayerA();
			if (playerA.getFriendLotFree() + LOT_CONFIGURE.getFriendFreeTime() < contextTime) {
				playerA.setFriendLotFree(contextTime);

			} else {
				playerContext.modifyFriendShipNumber(-LOT_CONFIGURE.getFriendshipNumber(), false);
			}

			JbCard card = playerContext.gainCard(lotCardDefine(LOT_CONFIGURE.getFriendProbabilities()), 1);
			playerContext.countCardNumber();
			return card;
		}
	}

	/**
	 * 概率抽卡
	 * 
	 * @param probabilities
	 * @return
	 */
	private ICardDefine lotCardDefine(float[] probabilities) {
		float rnd = HelperRandom.RANDOM.nextFloat();
		int length = probabilities.length;
		boolean empty = true;
		for (int i = 0; i < length; i++) {
			if ((rnd -= probabilities[i]) <= 0) {
				List<ICardDefine> cardDefines = rareMapCard.get(i);
				if (cardDefines == null) {
					if (empty) {
						empty = false;
						i = -1;
					}

				} else {
					return cardDefines.get(HelperRandom.nextInt(cardDefines.size()));
				}
			}
		}

		throw new ServerException(ServerStatus.ON_FAIL);
	}

	/**
	 * 抽一次
	 * 
	 * @param playerContext
	 * @return
	 */
	public JbCard lotDiamond(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		long contextTime = ContextUtils.getContextTime();
		JbPlayerA playerA = playerContext.getPlayerA();
		if (playerA.getDiamondLotFree() + LOT_CONFIGURE.getDiamondFreeTime() < contextTime) {
			playerA.setDiamondLotFree(contextTime);

		} else {
			playerContext.modifyDiamond(-LOT_CONFIGURE.getDiamondNumber(), false);
		}

		JbCard card = playerContext.gainCard(lotCardDefine(LOT_CONFIGURE.getDiamondProbabilities()), 1);
		playerContext.countCardNumber();
		return card;
	}

	/**
	 * 抽十次送一次
	 * 
	 * @param playerContext
	 * @return
	 */
	public List<JbCard> lotDiamonds(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		playerContext.modifyDiamond(-LOT_CONFIGURE.getTenDiamondNumber(), false);
		List<JbCard> cards = new ArrayList<JbCard>();
		boolean isRare = false;
		for (int i = 0; i < 8; i++) {
			ICardDefine cardDefine = lotCardDefine(LOT_CONFIGURE.getDiamondProbabilities());
			if (!isRare && cardDefine.getRare() >= 5) {
				isRare = true;
			}

			cards.add(playerContext.gainCard(cardDefine, 1));
		}

		if (isRare) {
			cards.add(playerContext.gainCard(lotCardDefine(LOT_CONFIGURE.getDiamondProbabilities()), 1));

		} else {
			List<ICardDefine> cardDefines = rareMapCard.get(LOT_CONFIGURE.getTenCardDefineRare());
			if (cardDefines == null) {
				cards.add(playerContext.gainCard(lotCardDefine(LOT_CONFIGURE.getDiamondProbabilities()), 10));

			} else {
				cards.add(playerContext.gainCard(cardDefines.get(HelperRandom.nextInt(cardDefines.size())), 10));
			}
		}

		playerContext.countCardNumber();
		return cards;
	}
}
