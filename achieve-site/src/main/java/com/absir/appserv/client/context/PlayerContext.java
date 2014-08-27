/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 上午11:57:29
 */
package com.absir.appserv.client.context;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.bean.JPlayerA;
import com.absir.appserv.client.bean.JPlayerReward;
import com.absir.appserv.client.bean.JbAnswer;
import com.absir.appserv.client.bean.JbReward;
import com.absir.appserv.client.bean.value.JoAnswer;
import com.absir.appserv.client.configure.JPlayerConfigure;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XCardExpDefine;
import com.absir.appserv.client.configure.xls.XPlayerDefine;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.configure.xls.XRewardDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskDetail;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskPass;
import com.absir.appserv.client.context.value.IPropEvolute;
import com.absir.appserv.client.context.value.OFightBase;
import com.absir.appserv.client.context.value.OFightTask;
import com.absir.appserv.client.context.value.OReward;
import com.absir.appserv.client.service.PropService;
import com.absir.appserv.client.service.SocketService;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.game.utils.GameUtils;
import com.absir.appserv.game.value.IExp;
import com.absir.appserv.game.value.LevelExpCxt;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.appserv.system.helper.HelperArray;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.context.core.ContextBean;
import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@MaEntity(parent = { @MaMenu("内容管理"), @MaMenu("游戏管理") }, name = "在线")
public class PlayerContext extends ContextBean<Long> implements ICrudBean {

	// 玩家基本数据
	@Embedded
	JPlayer player;

	// 玩家全部卡牌
	@Transient
	Map<Long, JCard> playerCards = new LinkedHashMap<Long, JCard>();

	// 玩家更多数据
	@Embedded
	JPlayerA playerA;

	// 登录时间
	@JaEdit(editable = JeEditable.LOCKED)
	private long onlineTime;

	// 自动回复行动力
	@JaEdit(editable = JeEditable.LOCKED)
	private long epTaskTime;

	// SOCKET链接
	@JsonIgnore
	@JaEdit(editable = JeEditable.LOCKED)
	private SocketChannel socketChannel;

	// 玩家战斗
	@Transient
	@JaEdit(editable = JeEditable.LOCKED)
	private OFightBase fightBase;

	// 玩家游戏外部配置
	public static final JPlayerConfigure PLAYER_CONFIGURE = JConfigureUtils.getConfigure(JPlayerConfigure.class);

	// 玩家等级定义
	public static final XlsDao<XPlayerDefine, Serializable> PLAYER_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XPlayerDefine.class);

	// 卡牌基础定义
	public static final XlsDao<XCardDefine, Serializable> CARD_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XCardDefine.class);

	// 卡牌等级定义
	public static final XlsDao<XCardExpDefine, Serializable> CARD_EXP_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XCardExpDefine.class);

	// 任务定义
	public static final XlsDao<XTaskDefine, Serializable> TASK_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XTaskDefine.class);

	// 道具定义
	public static final XlsDao<XPropDefine, Serializable> PROP_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XPropDefine.class);

	// 奖励定义
	public static final XlsDao<XRewardDefine, Serializable> REWARD_DEFINE_XLS_DAO = XlsUtils.getXlsDao(XRewardDefine.class);

	// 卡牌等级经验
	public static final List<Integer> CARD_LEVEl_EXPS = new ArrayList<Integer>();

	static {
		// 兼容等级从1级开始
		// ((List<XPlayerDefine>) PLAYER_DEFINE_XLS_DAO.getAll()).add(0, new
		// XPlayerDefine());
		// ((List<XCardExpDefine>) CARD_EXP_DEFINE_XLS_DAO.getAll()).add(0, new
		// XCardExpDefine());
		int exp = 0;
		for (XCardExpDefine cardExpDefine : CARD_EXP_DEFINE_XLS_DAO.getAll()) {
			exp += cardExpDefine.getExp();
			CARD_LEVEl_EXPS.add(exp);
		}
	}

	/** PLAYER_CONTEXT_MAP */
	public static final Map<Long, PlayerContext> PLAYER_CONTEXT_MAP = (Map<Long, PlayerContext>) (Object) ContextUtils.getContextFactory().getContextMap(PlayerContext.class);

	/**
	 * @param playerId
	 * @return
	 */
	public static PlayerContext find(Long playerId) {
		return PLAYER_CONTEXT_MAP.get(playerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContext#initialize()
	 * 
	 * 初始化玩家
	 */
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		PlayerService.ME.loadPlayerContext(this);
		countCardNumber();

		onlineTime = ContextUtils.getContextTime();
		int lastOnlineDay = (int) (playerA.getLastOffline() / (24 * 3600000));
		int onlineDay = (int) (onlineTime / (24 * 3600000));
		playerA.setOnlineDay(onlineDay);
		if (lastOnlineDay < onlineDay) {
			if (playerA.getLastOffline() != 0 && lastOnlineDay < onlineDay - 1) {
				// 登录不清零
				// playerA.setOnline(0);

			} else {
				playerA.setOnline(playerA.getOnline() + 1);
			}
		}

		// 初始化自动回复
		if (player.getEp() < player.getMaxEp()) {
			int ep = (int) ((onlineTime - playerA.getLastOffline()) / getEpTaskInterval());
			modifyEp(ep < 0 ? 0 : ep);
		}
	}

	/**
	 * 更新在线天数
	 * 
	 * @param contextTime
	 */
	public void updateOnlineDay(long contextTime) {
		int contextDay = (int) (contextTime / (24 * 3600000));
		if (contextDay > playerA.getOnlineDay()) {
			playerA.setOnline(contextDay);
			playerA.setOnline(playerA.getOnline() + 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContextBean#stepDone(long)
	 * 
	 * 计算自动回复|答题倒计时和玩家存活时间
	 */
	@Override
	public boolean stepDone(long contextTime) {
		if (epTaskTime < contextTime) {
			int hp = 1;
			while ((epTaskTime += getEpTaskInterval()) < contextTime) {
				hp++;
			}

			modifyEp(hp);
		}

		OFightBase fight = fightBase;
		if (fight != null) {
			fight.steping(contextTime);
		}

		return super.stepDone(contextTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContext#uninitialize()
	 * 
	 * 保存玩家全部数据
	 */
	@Override
	public void uninitialize() {
		// TODO Auto-generated method stub
		setFightBase(null);
		playerA.setLastOffline(ContextUtils.getContextTime());
		playerA.setOnlineTime(playerA.getOnlineTime() + playerA.getLastOffline() - onlineTime);
		PlayerService.ME.savePlayerContext(this);
	}

	/**
	 * 行动力回复时间
	 * 
	 * @return
	 */
	private int getEpTaskInterval() {
		return 300000;
	}

	/**
	 * 获取玩家
	 * 
	 * @return the player
	 */
	public JPlayer getPlayer() {
		return player;
	}

	/**
	 * 获取玩家全部卡牌
	 * 
	 * @return
	 */
	public Collection<JCard> getAllCards() {
		return playerCards.values();
	}

	/**
	 * 获取玩家全部数据
	 * 
	 * @return the playerA
	 */
	public JPlayerA getPlayerA() {
		return playerA;
	}

	/**
	 * 获取Ep回复时间
	 * 
	 * @return
	 */
	public long getEpTaskTime() {
		return getEpTaskInterval() - (epTaskTime - ContextUtils.getContextTime());
	}

	/**
	 * 获取玩家当前连接
	 * 
	 * @return the socketChannel
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * 设置当前玩家连接和在线状态
	 * 
	 * @param socketChannel
	 *            the socketChannel to set
	 */
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		player.setOnline(socketChannel != null);
	}

	/**
	 * 获取玩家当前战斗
	 * 
	 * @return the fightBase
	 */
	public OFightBase getFightBase() {
		return fightBase;
	}

	/**
	 * 设置玩家战斗
	 * 
	 * @param fightBase
	 *            the fightBase to set
	 */
	public void setFightBase(OFightBase fight) {
		if (fightBase != fight) {
			if (fightBase != null) {
				fightBase.closed();
			}

			fightBase = fight;
		}
	}

	/**
	 * 获取玩家卡牌
	 * 
	 * @param cardId
	 * @return
	 */
	public JCard getCard(long cardId) {
		JCard card = playerCards.get(cardId);
		if (card == null) {
			throw new ServerException(ServerStatus.NO_PARAM, "Card");
		}

		return card;
	}

	/**
	 * 获取玩家全部卡牌
	 * 
	 * @param cardIds
	 * @return
	 */
	public List<JCard> getCards(long[] cardIds) {
		List<JCard> cards = new ArrayList<JCard>();
		for (long cardId : cardIds) {
			cards.add(getCard(cardId));
		}

		return cards;
	}

	/**
	 * 获取卡牌领导力消耗
	 * 
	 * @param cards
	 * @return
	 */
	public int getCardsCost(JCard[] cards) {
		int cost = 0;
		for (JCard card : cards) {
			if (card != null) {
				if ((cost += card.getCardDefine().getCost()) > player.getMaxCost()) {
					return cost;
				}
			}
		}

		return cost;
	}

	/**
	 * 更改玩家金钱
	 * 
	 * @param money
	 */
	public synchronized void modifyMoney(int money) {
		money += player.getMoney();
		if (money < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "Money");
		}

		player.setMoney(money);
	}

	/**
	 * 更改玩家行动力
	 * 
	 * @param ep
	 */
	public synchronized void modifyEp(int ep) {
		ep += player.getEp();
		if (ep < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "Ep");

		} else if (ep >= player.getMaxEp()) {
			ep = player.getMaxEp();
			epTaskTime = 0;

		} else {
			if (epTaskTime < ContextUtils.getContextTime()) {
				epTaskTime = ContextUtils.getContextTime() + getEpTaskInterval();
			}
		}

		player.setEp(ep);
	}

	/**
	 * 更改玩家经验
	 * 
	 * @param exp
	 */
	public synchronized void modifyExp(int exp) {
		GameUtils.modifyExp(exp, player, PLAYER_LEVEL_EXP_CXT, (List<IExp>) (Object) PLAYER_DEFINE_XLS_DAO.getAll(), PLAYER_CONFIGURE.getMaxLevel());
	}

	/**
	 * 更改玩家等级
	 * 
	 * @param level
	 */
	public synchronized void modifyLevel(int level) {
		if (player.getLevel() == level || level > PLAYER_CONFIGURE.getMaxLevel()) {
			return;
		}

		XPlayerDefine playerDefine = PLAYER_DEFINE_XLS_DAO.get(level);
		XPlayerDefine currentPlayerDefine = PLAYER_DEFINE_XLS_DAO.get(player.getLevel());
		player.setLevel(level);
		player.setMaxEp(player.getMaxEp() + playerDefine.getEp() - currentPlayerDefine.getEp());
		modifyEp(player.getMaxEp());
		player.setMaxCost(player.getMaxCost() + playerDefine.getCost() - currentPlayerDefine.getCost());
		player.setMaxCardNumber(player.getMaxCardNumber() + playerDefine.getCardNumber() - currentPlayerDefine.getCardNumber());
		player.setMaxFriendNumber(player.getMaxFriendNumber() + playerDefine.getFriendNumber() - currentPlayerDefine.getFriendNumber());
	}

	// 设置玩家等级
	/** PLAYER_LEVEL_EXP_CXT */
	final LevelExpCxt<JPlayer> PLAYER_LEVEL_EXP_CXT = new LevelExpCxt<JPlayer>() {

		@Override
		public void setLevel(JPlayer player, int level) {
			PlayerContext.this.modifyLevel(level);
		}
	};

	/**
	 * 更改玩家友情点数
	 * 
	 * @param friendShipNumber
	 */
	public synchronized void modifyFriendShipNumber(int friendShipNumber) {
		friendShipNumber += player.getFriendshipNumber();
		if (friendShipNumber < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "FriendShipNumber");
		}

		player.setFriendshipNumber(friendShipNumber);
	}

	/**
	 * 更改玩家宝石
	 * 
	 * @param diamond
	 */
	public synchronized void modifyDiamond(int diamond) {
		diamond += player.getDiamond();
		if (diamond < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "Diamond");
		}

		player.setDiamond(diamond);
	}

	/**
	 * 获取卡牌
	 * 
	 * @param cardId
	 * @param level
	 * @return
	 */
	public JCard gainCard(int cardId, int level) {
		XCardDefine cardDefine = CARD_DEFINE_XLS_DAO.get(cardId);
		if (cardDefine != null) {
			return gainCard(cardDefine, level);
		}

		return null;
	}

	/**
	 * 获取卡牌
	 * 
	 * @param cardDefine
	 * @param level
	 * @return
	 */
	public JCard gainCard(XCardDefine cardDefine, int level) {
		JCard card = FightServiceUtils.generate(cardDefine, this);
		card = gainCard(card);
		modifyCardLevel(card, level);
		return card;
	}

	/**
	 * 获取卡牌
	 * 
	 * @param card
	 * @return
	 */
	public JCard gainCard(JCard card) {
		FightServiceUtils.gain(card);
		card = PlayerService.ME.addPlayerCard(player, card);
		playerCards.put(card.getId(), card);
		return card;
	}

	/**
	 * 消耗卡牌
	 * 
	 * @param card
	 */
	private void consumeCard(JCard card) {
		if (player.containCard(card)) {
			throw new ServerException(ServerStatus.IN_FAILED, "Card");
		}

		playerCards.remove(card.getId());
		PlayerService.ME.removePlayerCard(player, card);
	}

	/**
	 * 消耗多张卡牌
	 * 
	 * @param cards
	 */
	private void consumeCards(List<JCard> cards) {
		for (JCard card : cards) {
			consumeCard(card);
		}
	}

	/**
	 * 计算卡牌总数
	 * 
	 */
	public void countCardNumber() {
		// int cardNumber = -5;
		// if (player.getCard1() == null) {
		// cardNumber = -1;
		//
		// } else if (player.getCard2() == null) {
		// cardNumber = -2;
		//
		// } else if (player.getCard3() == null) {
		// cardNumber = -3;
		//
		// } else if (player.getCard4() == null) {
		// cardNumber = -4;
		// }
		player.setCardNumber(playerCards.size());
	}

	/**
	 * 更改卡牌等级
	 * 
	 * @param card
	 * @param level
	 */
	public static void modifyCardLevel(JCard card, int level) {
		synchronized (card.getPlayer() == null ? card : card.getPlayer()) {
			if (card.getLevel() == level || level > card.getCardDefine().getMaxLevel()) {
				return;
			}

			int subLevel = level - card.getLevel();
			card.setLevel(level);
			card.setHp(card.getHp() + (int) (subLevel * card.getHpf()));
			card.setAtk(card.getAtk() + (int) (subLevel * card.getAtkf()));
			card.setWater(card.getWater() + (int) (subLevel * card.getWaterf()));
			card.setFire(card.getFire() + (int) (subLevel * card.getFiref()));
			card.setThunder(card.getThunder() + (int) (subLevel * card.getThunderf()));
		}
	}

	// 设置卡牌等级
	/** CARD_LEVEL_EXP_CXT */
	static final LevelExpCxt<JCard> CARD_LEVEL_EXP_CXT = new LevelExpCxt<JCard>() {

		@Override
		public void setLevel(JCard card, int level) {
			modifyCardLevel(card, level);
		}
	};

	/**
	 * 更改卡牌经验
	 * 
	 * @param card
	 * @param exp
	 */
	public static void modifyCardExp(JCard card, int exp) {
		synchronized (card.getPlayer() == null ? card : card.getPlayer()) {
			GameUtils.modifyExp(exp, card, CARD_LEVEL_EXP_CXT, (List<IExp>) (Object) CARD_EXP_DEFINE_XLS_DAO.getAll(), card.getCardDefine().getMaxLevel());
		}
	}

	/**
	 * 更改玩家签名
	 * 
	 * @param sign
	 */
	public void sign(String sign) {
		player.setSign(sign);
	}

	/**
	 * 阵型卡牌列表
	 * 
	 * @param cardIds
	 * @param cards
	 * @return
	 */
	public int formationCards(long[] cardIds, List<JCard> cards) {
		int formation = 0;
		for (long cardId : cardIds) {
			JCard card = playerCards.get(cardId);
			if (card != null) {
				formation++;
			}

			cards.add(card);
		}

		return formation;
	}

	/**
	 * 更改阵型
	 * 
	 * @param cardIds
	 */
	public synchronized void formation(long[] cardIds) {
		if (cardIds.length < 1 || cardIds.length > JPlayer.CARD_FORMATION_COUNT) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		int length = cardIds.length;
		for (int i = 0; i < length; i++) {
			long cardId = cardIds[i];
			for (int j = i + 1; j < length; j++) {
				if (cardId == cardIds[j]) {
					throw new ServerException(ServerStatus.NO_PARAM);
				}
			}
		}

		JCard[] cards = new JCard[length];
		int formation = 0;
		for (int i = 0; i < length; i++) {
			JCard card = playerCards.get(cardIds[i]);
			if (card != null) {
				formation++;
			}

			cards[i] = card;
		}

		if (formation == 0) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		int cost = getCardsCost(cards);
		if (cost > player.getMaxCost()) {
			throw new ServerException(ServerStatus.IN_FAILED, "Cost");
		}

		player.setCost(cost);
		for (int i = 0; i < length; i++) {
			player.setCard(cards[i], i);
		}

		for (; length < JPlayer.CARD_FORMATION_COUNT; length++) {
			player.setCard(null, length);
		}
	}

	/**
	 * @param cardId
	 * @param position
	 */
	public synchronized void formation(long cardId, int position) {
		JCard card = getCard(cardId);
		player.setCard(card, position);
	}

	/**
	 * 出售卡牌
	 * 
	 * @param cardId
	 * @return
	 */
	public synchronized void sell(long cardId) {
		if (playerCards.size() <= 1) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		JCard card = getCard(cardId);
		consumeCard(card);
		modifyMoney(card.getCardDefine().getPrice());
		countCardNumber();
	}

	/**
	 * 出售卡牌(多张)
	 * 
	 * @param cardIds
	 */
	public synchronized void sell(long[] cardIds) {
		if (playerCards.size() <= cardIds.length) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		List<JCard> cards = getCards(cardIds);
		int money = 0;
		for (JCard card : cards) {
			consumeCard(card);
			money += card.getCardDefine().getPrice();
		}

		modifyMoney(money);
		countCardNumber();
	}

	/**
	 * 升级卡牌
	 * 
	 * @param cardId
	 * @param cardIds
	 * @return
	 */
	public synchronized JCard feed(long cardId, long[] cardIds) {
		if (HelperArray.contains(cardIds, cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		JCard card = getCard(cardId);
		if (card.getLevel() >= card.getCardDefine().getMaxLevel()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		List<JCard> targetCards = getCards(cardIds);
		int allExp = 0;
		for (JCard targetCard : targetCards) {
			int exp = targetCard.getCardDefine().getExp() + (int) ((CARD_LEVEl_EXPS.get(targetCard.getLevel()) + targetCard.getExp()) * 0.8f);
			if (card.getCardDefine().getCamp() == targetCard.getCardDefine().getCamp()) {
				exp *= 1.25f;
			}

			allExp += exp;
		}

		modifyMoney(-(card.getCardDefine().getLevelPrice() + allExp * 10));
		consumeCards(targetCards);
		modifyCardExp(card, allExp);
		countCardNumber();
		return card;
	}

	/**
	 * 进化卡牌
	 * 
	 * @param cardId
	 * @param cardIds
	 * @return
	 */
	public synchronized JCard evolute(long cardId, long[] cardIds, IPropEvolute propEvolute) {
		if (HelperArray.contains(cardIds, cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		JCard card = getCard(cardId);
		List<JCard> targetCards = getCards(cardIds);
		int length = card.getCardDefine().getEvolutionRequires().length;
		if (length != targetCards.size()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		for (int i = 0; i < length; i++) {
			if (card.getCardDefine().getEvolutionRequires()[i] != targetCards.get(i).getCardDefine()) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}
		}

		modifyMoney(-card.getCardDefine().getEvolutionPrice());
		consumeCards(targetCards);
		float evolute = PLAYER_CONFIGURE.getEvoluteProb()[card.getCardDefine().getRare()];
		float rnd = HelperRandom.RANDOM.nextFloat();
		if (propEvolute == null) {
			if (rnd > evolute) {
				return null;
			}

		} else {
			if (rnd > evolute * propEvolute.getLuck(card)) {
				return null;
			}
		}

		int cost = card.getCardDefine().getCost();
		FightServiceUtils.reset(card, card.getCardDefine().getEvolution(), this);
		card.setExp(0);
		if (player.containCard(card)) {
			player.setCost(player.getCost() - cost + card.getCardDefine().getCost());
		}

		countCardNumber();
		return card;
	}

	/**
	 * 购买卡牌栏
	 * 
	 * @return
	 */
	public synchronized int item(int number) {
		modifyDiamond(-number);
		number *= PLAYER_CONFIGURE.getCardNumber();
		player.setMaxCardNumber(player.getMaxCardNumber() + number);
		return number;
	}

	/**
	 * 购买好友数量
	 * 
	 * @return
	 */
	public synchronized int friend(int number) {
		modifyDiamond(-number);
		number *= PLAYER_CONFIGURE.getFriendNumber();
		player.setMaxFriendNumber(player.getMaxFriendNumber() + number);
		return number;
	}

	/**
	 * 领取奖励
	 * 
	 * @param reward
	 */
	public synchronized Object reward(JbReward reward) {
		Map<String, Object> rewardData = new HashMap<String, Object>();
		modifyMoney(reward.getMoney());
		modifyDiamond(reward.getDiamond());
		if (reward.getCardDefines() != null) {
			List<JCard> cards = new ArrayList<JCard>();
			rewardData.put("cards", cards);
			for (Entry<Integer, Integer> entry : reward.getCardDefines().entrySet()) {
				XCardDefine cardDefine = CARD_DEFINE_XLS_DAO.get(entry.getKey());
				if (cardDefine != null) {
					for (int i = entry.getValue(); i > 0; i--) {
						cards.add(gainCard(cardDefine, 1));
					}
				}
			}
		}

		if (reward.getPropDefines() != null) {
			rewardData.put("props", reward.getPropDefines());
			for (Entry<Integer, Integer> entry : reward.getPropDefines().entrySet()) {
				XPropDefine propDefine = PROP_DEFINE_XLS_DAO.get(entry.getKey());
				if (propDefine != null) {
					for (int i = entry.getValue(); i > 0; i--) {
						entry.setValue(PropService.ME.addProp(this, propDefine, entry.getValue()));
					}
				}
			}
		}

		countCardNumber();
		return rewardData;
	}

	/**
	 * 领取奖励
	 * 
	 * @param rewardDefine
	 */
	public synchronized Object reward(long rewardId) {
		JPlayerReward playerReward = PlayerService.ME.getPlayerReward(getId(), rewardId);
		if (playerReward == null) {
			throw new ServerException(ServerStatus.ON_DELETED);
		}

		int rewardNumber = playerA.getRewardNumber();
		if (rewardNumber > 0) {
			playerA.setRewardNumber(--rewardNumber);
		}

		return reward(playerReward.getRewardBean());
	}

	/**
	 * 领取奖励
	 * 
	 * @param rewardDefine
	 */
	public synchronized Object xReward(XRewardDefine rewardDefine) {
		OReward rewardInvoker = rewardDefine.getRewardInvoker();
		String rewardId = rewardInvoker.getRewardId();
		if (rewardId == null) {
			rewardId = rewardDefine.getId();
		}

		Integer recard = rewardDefine.getRewardInvoker().reward(this, playerA.getMetaRecards().get(rewardId));
		if (recard == null) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		Object rewardData = reward(rewardDefine.getReward());
		playerA.getMetaRecards().put(rewardId, recard);
		return rewardData;
	}

	/**
	 * 答题属性
	 * 
	 * @param id
	 * @param correct
	 * @param answerTime
	 * @return
	 */
	public synchronized JbAnswer answer(long id, boolean correct, long answerTime) {
		Map<Long, JbAnswer> answers = player.getAnswers();
		if (answers == null) {
			answers = new HashMap<Long, JbAnswer>();
			player.setAnswers(answers);
		}

		JbAnswer answer = answers.get(id);
		if (answer == null) {
			answer = new JoAnswer();
		}

		answer.setAnswerCount(answer.getAnswerCount() + 1);
		if (correct) {
			answer.setAnswerCorrect(answer.getAnswerCorrect() + 1);
		}

		answer.setAnswerTime(answer.getAnswerTime() + answerTime);
		return answer;
	}

	/**
	 * 任务ID
	 * 
	 * @param scene
	 * @param pass
	 * @return
	 */
	public static String getTaskId(int scene, int pass) {
		return scene + "." + pass;
	}

	/**
	 * 进入任务
	 * 
	 * @return
	 */
	public synchronized OFightTask task(int scene, int pass, int detail) {
		if (playerCards.size() >= player.getMaxCardNumber()) {
			throw new ServerException(ServerStatus.IN_FAILED, "CardNumber");
		}

		XTaskDefine taskDefine = TASK_DEFINE_XLS_DAO.get(scene);
		if (taskDefine.getLevel() > player.getLevel()) {
			throw new ServerException(ServerStatus.IN_FAILED, "Level");
		}

		TaskPass taskPass = taskDefine.getTaskPasses()[pass];
		TaskDetail taskDetail = taskPass.getTaskDetails()[detail];
		String taskId = getTaskId(scene, pass);
		if (detail > 0) {
			Integer progress = playerA.getTaskProgresses().get(taskId);
			if (progress == null || progress < detail) {
				throw new ServerException(ServerStatus.IN_FAILED, "Progress");
			}

		} else {
			// 顺序开启条件
			int nScene = scene;
			int nPass = pass;
			if (pass == 0) {
				if (scene > 1) {
					nScene--;
				}

			} else {
				nPass--;
			}

			if (nScene != scene || nPass != pass) {
				String nTaskId = getTaskId(nScene, nPass);
				Integer nProgress = playerA.getTaskProgresses().get(nTaskId);
				if (nProgress == null) {
					throw new ServerException(ServerStatus.IN_FAILED, "Progress");
				}
			}
		}

		if (player.getEp() < taskDetail.getEp()) {
			throw new ServerException(ServerStatus.IN_FAILED, "Ep");
		}

		// modifyEp(-taskDetail.getEp());
		return new OFightTask(this, taskId, scene, pass, detail, taskDefine, taskPass, taskDetail);
	}

	/**
	 * 任务完成
	 * 
	 * @param taskId
	 * @param detail
	 */
	public synchronized void taskComplete(String taskId, int detail) {
		Map<String, Integer> taskProgresses = playerA.getTaskProgresses();
		Integer progress = taskProgresses.get(taskId);
		++detail;
		if (progress == null || progress < detail) {
			taskProgresses.put(taskId, detail);
		}
	}

	/**
	 * 在线玩家数据
	 * 
	 * @param players
	 */
	public static List<JPlayer> onlinePlayers(List<JPlayer> players) {
		int size = players.size();
		for (int i = 0; i < size; i++) {
			PlayerContext playerContext = PlayerContext.find(players.get(i).getId());
			if (playerContext != null) {
				players.set(i, playerContext.getPlayer());
			}
		}

		return players;
	}

	/**
	 * 在线玩家数据
	 * 
	 * @param Iterator
	 * @return
	 */
	public static List<JPlayer> onlinePlayers(Iterator<JPlayer> playerIterator) {
		List<JPlayer> players = new ArrayList<JPlayer>();
		while (playerIterator.hasNext()) {
			JPlayer player = playerIterator.next();
			PlayerContext playerContext = PlayerContext.find(player.getId());
			if (playerContext != null) {
				player = playerContext.getPlayer();
			}

			players.add(player);
		}

		return players;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.crud.value.ICrudBean#proccessCrud(com.absir.
	 * appserv.system.bean.value.JaCrud.Crud)
	 */
	@Override
	public synchronized void proccessCrud(Crud crud) {
		// TODO Auto-generated method stub
		if (crud == Crud.UPDATE) {
			SocketService.writeByteObject(this, SocketService.CALLBACK_MODIFY, "");
		}
	}
}
