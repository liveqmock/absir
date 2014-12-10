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

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbLotCard;
import com.absir.appserv.game.bean.JbLotPool;
import com.absir.appserv.game.bean.JbPlayerA;
import com.absir.appserv.game.bean.value.ICardDefine;
import com.absir.appserv.game.bean.value.IPropDefine;
import com.absir.appserv.game.confiure.JLotConfigure;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.system.domain.DActiver;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.appserv.system.helper.HelperRandom.RandomPool;
import com.absir.async.value.Async;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextService;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
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

	/** definedCard */
	protected boolean definedCard;

	/** 抽奖卡牌 */
	private DActiver<JbLotCard> lotCardActitity;

	/** 抽奖池 */
	private DActiver<JbLotPool> lotPoolActitity;

	/** 品质卡牌抽奖组 */
	protected Map<Integer, RandomPool<ICardDefine>> rareMapCards;

	/** 特殊卡牌抽奖组 */
	protected RandomPool<ICardDefine> specialCards;

	/** 品质卡牌池抽奖组 */
	protected Map<Integer, RandomPool<ICardDefine>> rareMapPoolCards;

	/** 特殊卡牌抽奖组 */
	protected RandomPool<ICardDefine> specialPoolCards;

	/** 品质道具池抽奖组 */
	protected Map<Integer, RandomPool<IPropDefine>> rareMapPoolProps;

	/** 特殊卡牌抽奖组 */
	protected RandomPool<IPropDefine> specialPoolProps;

	/**
	 * @return the rareMapCards
	 */
	public Map<Integer, RandomPool<ICardDefine>> getRareMapCards() {
		return rareMapCards;
	}

	/**
	 * @return the specialCards
	 */
	public RandomPool<ICardDefine> getSpecialCards() {
		return specialCards;
	}

	/**
	 * @return the rareMapPoolCards
	 */
	public Map<Integer, RandomPool<ICardDefine>> getRareMapPoolCards() {
		return rareMapPoolCards;
	}

	/**
	 * @return the specialPoolCards
	 */
	public RandomPool<ICardDefine> getSpecialPoolCards() {
		return specialPoolCards;
	}

	/**
	 * @return the rareMapPoolProps
	 */
	public Map<Integer, RandomPool<IPropDefine>> getRareMapPoolProps() {
		return rareMapPoolProps;
	}

	/**
	 * @return the specialPoolProps
	 */
	public RandomPool<IPropDefine> getSpecialPoolProps() {
		return specialPoolProps;
	}

	/**
	 * 初始化服务
	 */
	@Inject
	protected void initService() {
		definedCard = JbPlayerContext.COMPONENT.getCardDefineDao() != null;
		lotCardActitity = new DActiver<JbLotCard>("JLotCard");
		lotPoolActitity = new DActiver<JbLotPool>("JLotPool");
		rareMapCards = new HashMap<Integer, RandomPool<ICardDefine>>();
		specialCards = new RandomPool<ICardDefine>();
		rareMapPoolCards = new HashMap<Integer, RandomPool<ICardDefine>>();
		specialPoolCards = new RandomPool<ICardDefine>();
		rareMapPoolProps = new HashMap<Integer, RandomPool<IPropDefine>>();
		specialPoolProps = new RandomPool<IPropDefine>();
		JbPlayerContext.COMPONENT.getCardDefineDao();
		XlsUtils.register(JbPlayerContext.COMPONENT.CARD_DEFINE_CLASS, new CallbackTemplate<Class<?>>() {

			@Override
			public void doWith(Class<?> template) {
				// TODO Auto-generated method stub
				ME.reloadCard();
				ME.reloadPool();
			}
		});

		XlsUtils.register(JbPlayerContext.COMPONENT.PROP_DEFINE_CLASS, new CallbackTemplate<Class<?>>() {

			@Override
			public void doWith(Class<?> template) {
				// TODO Auto-generated method stub
				ME.reloadPool();
			}
		});
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
			ME.reloadCard();
		}

		if (lotPoolActitity.stepNext(contextTime)) {
			ME.reloadPool();
		}
	}

	/**
	 * @param rareMap
	 * @param id
	 * @param rare
	 * @return
	 */
	protected float getRare(Map<? extends Serializable, Float> rareMap, Serializable id, float rare) {
		Float idRare = rareMap.get(id);
		return idRare == null ? rare : idRare;
	}

	/**
	 * @param rareMap
	 * @param rare
	 * @param element
	 * @param elementRare
	 */
	protected <T> void addRareMap(Map<Integer, RandomPool<T>> rareMap, Integer rare, T element, float elementRare) {
		RandomPool<T> pool = rareMap.get(rare);
		if (pool == null) {
			pool = new RandomPool<T>();
			rareMap.put(rare, pool);
		}

		pool.add(element, elementRare);
	}

	/**
	 * @param contextTime
	 */
	@Async(notifier = true)
	@Transaction(readOnly = true)
	public void reloadCard() {
		if (!definedCard) {
			return;
		}

		long contextTime = ContextUtils.getContextTime();
		Map<Integer, Float> cardRares = new HashMap<Integer, Float>();
		Set<Integer> cardSpecials = new HashSet<Integer>();
		try {
			for (JbLotCard lotCard : lotCardActitity.reloadActives(contextTime)) {
				if (lotCard.getCardRares() != null) {
					Integer cardId = null;
					for (float cardRare : lotCard.getCardRares()) {
						if (cardId == null) {
							cardId = (int) cardRare;

						} else {
							cardRares.put(cardId, cardRare);
							cardId = null;
						}
					}
				}

				if (lotCard.getCardSpecials() != null) {
					for (int cardSpecial : lotCard.getCardSpecials()) {
						cardSpecials.add(cardSpecial);
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		Map<Integer, RandomPool<ICardDefine>> newRareMapCards = new HashMap<Integer, RandomPool<ICardDefine>>();
		RandomPool<ICardDefine> newSpecialCards = new RandomPool<ICardDefine>();
		XlsDao<ICardDefine, Serializable> cardDefineDao = JbPlayerContext.COMPONENT.getCardDefineDao();
		for (ICardDefine cardDefine : cardDefineDao.getAll()) {
			float rare = getRare(cardRares, cardDefine.getId(), cardDefine.getLotRare());
			if (rare > 0) {
				addRareMap(newRareMapCards, cardDefine.getRare(), cardDefine, rare);
			}

			if (cardDefine.getLotSpecial() > 0) {
				newSpecialCards.add(cardDefine, cardDefine.getLotSpecial());

			} else if (rare > 0 && cardSpecials.contains(cardDefine.getId())) {
				newSpecialCards.add(cardDefine, rare);
			}
		}

		rareMapCards = newRareMapCards;
		specialCards = newSpecialCards;
	}

	/**
	 * @param contextTime
	 */
	@Async(notifier = true)
	@Transaction(readOnly = true)
	public void reloadPool() {
		long contextTime = ContextUtils.getContextTime();
		Map<Integer, Float> cardRares = new HashMap<Integer, Float>();
		Set<Integer> cardSpecials = new HashSet<Integer>();
		Map<Serializable, Float> propRares = new HashMap<Serializable, Float>();
		Set<Serializable> propSpecials = new HashSet<Serializable>();
		Class<? extends Serializable> propIdType = JbPlayerContext.COMPONENT.getPropDefineDao().getIdType();
		try {
			for (JbLotPool lotPool : lotPoolActitity.reloadActives(contextTime)) {
				if (lotPool.getCardRares() != null) {
					Integer cardId = null;
					for (float cardRare : lotPool.getCardRares()) {
						if (cardId == null) {
							cardId = (int) cardRare;

						} else {
							cardRares.put(cardId, cardRare);
							cardId = null;
						}
					}
				}

				if (lotPool.getCardSpecials() != null) {
					for (int cardSpecial : lotPool.getCardSpecials()) {
						cardSpecials.add(cardSpecial);
					}
				}

				if (lotPool.getPropRares() != null) {
					Serializable propId = null;
					for (Object propRare : lotPool.getPropRares()) {
						if (propId == null) {
							propId = KernelDyna.to(propRare, propIdType);

						} else {
							propRares.put(propId, KernelDyna.to(propRare, float.class));
							propId = null;
						}
					}
				}

				if (lotPool.getPropSpecials() != null) {
					for (Object propSpecial : lotPool.getPropSpecials()) {
						propSpecials.add(KernelDyna.to(propSpecial, propIdType));
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (definedCard) {
			Map<Integer, RandomPool<ICardDefine>> newRareMapPoolCards = new HashMap<Integer, RandomPool<ICardDefine>>();
			RandomPool<ICardDefine> newSpecialPoolCards = new RandomPool<ICardDefine>();
			XlsDao<ICardDefine, Serializable> cardDefineDao = JbPlayerContext.COMPONENT.getCardDefineDao();
			for (ICardDefine cardDefine : cardDefineDao.getAll()) {
				float rare = getRare(cardRares, cardDefine.getId(), cardDefine.getLotPoolRare());
				if (rare > 0) {
					addRareMap(newRareMapPoolCards, cardDefine.getRare(), cardDefine, rare);
				}

				if (cardDefine.getLotPoolSpecial() > 0) {
					newSpecialPoolCards.add(cardDefine, cardDefine.getLotPoolSpecial());

				} else if (rare > 0 && cardSpecials.contains(cardDefine.getId())) {
					newSpecialPoolCards.add(cardDefine, rare);
				}
			}

			rareMapPoolCards = newRareMapPoolCards;
			specialPoolCards = newSpecialPoolCards;
		}

		Map<Integer, RandomPool<IPropDefine>> newRareMapPoolProps = new HashMap<Integer, RandomPool<IPropDefine>>();
		RandomPool<IPropDefine> newSpecialPoolProps = new RandomPool<IPropDefine>();
		XlsDao<IPropDefine, Serializable> propDefineDao = JbPlayerContext.COMPONENT.getPropDefineDao();
		for (IPropDefine propDefine : propDefineDao.getAll()) {
			float rare = getRare(propRares, propDefine.getId(), propDefine.getLotPoolRare());
			if (rare > 0) {
				addRareMap(newRareMapPoolProps, propDefine.getRare(), propDefine, rare);
			}

			if (propDefine.getLotPoolSpecial() > 0) {
				newSpecialPoolProps.add(propDefine, propDefine.getLotPoolSpecial());

			} else if (rare > 0 && cardSpecials.contains(propDefine.getId())) {
				newSpecialPoolProps.add(propDefine, rare);
			}
		}

		rareMapPoolProps = newRareMapPoolProps;
		specialPoolProps = newSpecialPoolProps;
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
		if (entity instanceof JbLotPool) {
			lotPoolActitity.merge((JbLotPool) entity, mergeType, mergeEvent);

		} else {
			lotCardActitity.merge(entity, mergeType, mergeEvent);
		}
	}

	/**
	 * 概率抽奖
	 * 
	 * @param probabilities
	 * @param rareMapPool
	 * @return
	 */
	protected <T> T lotPool(float[] probabilities, Map<Integer, RandomPool<T>> rareMapPool) {
		float rnd = HelperRandom.RANDOM.nextFloat();
		int length = probabilities.length;
		boolean empty = true;
		for (int i = 0; i < length; i++) {
			if ((rnd -= probabilities[i]) <= 0) {
				RandomPool<T> pool = rareMapPool.get(i);
				if (pool == null) {
					if (empty) {
						empty = false;
						i = -1;
					}

				} else {
					return pool.randElement();
				}
			}
		}

		throw new ServerException(ServerStatus.ON_FAIL);
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

			JbCard card = playerContext.gainCard(lotPool(LOT_CONFIGURE.getFriendProbabilities(), rareMapPoolCards), 1);
			playerContext.countCardNumber();
			return card;
		}
	}

	/**
	 * 抽一次
	 * 
	 * @param playerContext
	 * @return
	 */
	public JbCard lotCard(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		long contextTime = ContextUtils.getContextTime();
		JbPlayerA playerA = playerContext.getPlayerA();
		if (playerA.getDiamondLotFree() + LOT_CONFIGURE.getCardDiamondFreeTime() < contextTime) {
			playerA.setDiamondLotFree(contextTime);

		} else {
			playerContext.modifyDiamond(-LOT_CONFIGURE.getCardDiamondNumber(), false);
		}

		JbCard card = playerContext.gainCard(lotPool(LOT_CONFIGURE.getCardProbabilities(), rareMapPoolCards), 1);
		playerContext.countCardNumber();
		return card;
	}

	/**
	 * 抽多次
	 * 
	 * @param playerContext
	 * @return
	 */
	public List<JbCard> lotCards(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		playerContext.modifyDiamond(-LOT_CONFIGURE.getSpecialCardDiamondNumber(), false);
		List<JbCard> cards = new ArrayList<JbCard>();
		for (int i = 0; i < 9; i++) {
			cards.add(playerContext.gainCard(lotPool(LOT_CONFIGURE.getCardProbabilities(), rareMapPoolCards), 1));
		}

		if (specialCards.size() > 0) {
			cards.add(playerContext.gainCard(specialCards.randElement(), 1));

		} else {
			cards.add(playerContext.gainCard(lotPool(LOT_CONFIGURE.getCardProbabilities(), rareMapPoolCards), 1));
		}

		playerContext.countCardNumber();
		return cards;
	}

	/**
	 * 抽奖物品
	 * 
	 * @author absir
	 *
	 */
	public static class LotElement {

		public int type;

		public Serializable id;

		public Object element;

	}

	/**
	 * 抽奖池
	 * 
	 * @return
	 */
	protected LotElement lot(JbPlayerContext playerContext) {
		LotElement element = new LotElement();
		if (definedCard && HelperRandom.randIndex(LOT_CONFIGURE.getPoolProbabilities()) == 1) {
			element.type = 1;
			element.element = playerContext.gainCard(lotPool(LOT_CONFIGURE.getCardProbabilities(), rareMapPoolCards), 1);
			playerContext.countCardNumber();

		} else {
			IPropDefine propDefine = lotPool(LOT_CONFIGURE.getPropProbabilities(), rareMapPoolProps);
			element.id = propDefine.getId();
			element.element = playerContext.modifyProp(propDefine, 1, true);
		}

		return element;
	}

	/**
	 * 抽一次池
	 * 
	 * @param playerContext
	 * @return
	 */
	public LotElement lotPool(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		return lot(playerContext);
	}

	/**
	 * 特殊抽奖池
	 * 
	 * @param playerContext
	 * @return
	 */
	protected LotElement specialLot(JbPlayerContext playerContext) {
		if (definedCard && specialPoolCards.size() > 0 && HelperRandom.randIndex(LOT_CONFIGURE.getPoolProbabilities()) == 1) {
			LotElement element = new LotElement();
			element.type = 1;
			element.element = playerContext.gainCard(specialPoolCards.randElement(), 1);
			playerContext.countCardNumber();
			return element;

		} else {
			if (specialPoolProps.size() > 0) {
				LotElement element = new LotElement();
				IPropDefine propDefine = specialPoolProps.randElement();
				element.id = propDefine.getId();
				element.element = playerContext.modifyProp(propDefine, 1, true);
				return element;
			}
		}

		return null;
	}

	/**
	 * 抽多次池
	 * 
	 * @param playerContext
	 * @return
	 */
	public List<LotElement> lotPools(JbPlayerContext playerContext) {
		if (playerContext.getAllCards().size() >= playerContext.getPlayer().getMaxCardNumber()) {
			throw new ServerException(ServerStatus.ON_FAIL, "cardNumber");
		}

		List<LotElement> elements = new ArrayList<LotElement>();
		for (int i = 0; i < 9; i++) {
			elements.add(lot(playerContext));
		}

		LotElement element = specialLot(playerContext);
		elements.add(element == null ? lot(playerContext) : element);
		return elements;
	}
}
