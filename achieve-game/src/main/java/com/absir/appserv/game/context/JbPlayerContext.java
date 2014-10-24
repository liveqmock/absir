/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午1:06:29
 */
package com.absir.appserv.game.context;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.bean.JbPlayerA;
import com.absir.appserv.game.bean.JbReward;
import com.absir.appserv.game.bean.value.ICardDefine;
import com.absir.appserv.game.bean.value.IPlayerDefine;
import com.absir.appserv.game.bean.value.IPropDefine;
import com.absir.appserv.game.bean.value.IRewardDefine;
import com.absir.appserv.game.bean.value.ITaskDefine;
import com.absir.appserv.game.bean.value.ITaskDefine.ITaskDetail;
import com.absir.appserv.game.bean.value.ITaskDefine.ITaskPass;
import com.absir.appserv.game.context.value.IFight;
import com.absir.appserv.game.context.value.IPropEvolute;
import com.absir.appserv.game.context.value.OReward;
import com.absir.appserv.game.service.SocketService;
import com.absir.appserv.game.utils.GameUtils;
import com.absir.appserv.game.value.LevelExpCxt;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.appserv.system.helper.HelperArray;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextBean;
import com.absir.context.core.ContextUtils;
import com.absir.core.util.UtilQueue;
import com.absir.core.util.UtilQueueBlock;
import com.absir.property.value.Allow;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Inject
public abstract class JbPlayerContext<C extends JbCard, P extends JbPlayer, A extends JbPlayerA, PD extends IPlayerDefine, R extends JbReward, F extends IFight> extends ContextBean<Long> {

	// 功能组件
	public static final PlayerComponentBase COMPONENT = BeanFactoryUtils.get(PlayerComponentBase.class);

	// 玩家全部卡牌
	@Transient
	protected Map<Long, C> playerCards = new LinkedHashMap<Long, C>();

	// 玩家基本数据
	@Embedded
	@Allow
	protected P player;

	// 玩家更多数据
	@Embedded
	@Allow
	protected A playerA;

	// 登录时间
	@JaEdit(editable = JeEditable.LOCKED)
	@Allow
	protected long onlineTime;

	// 自动回复行动力
	@JaEdit(editable = JeEditable.LOCKED)
	@Allow
	protected long epTaskTime;

	// SOCKET连接
	@JsonIgnore
	@JaEdit(editable = JeEditable.LOCKED)
	protected SocketChannel socketChannel;

	// 当前战斗
	@JsonIgnore
	@JaEdit(editable = JeEditable.LOCKED)
	protected F fight;

	// 全部通知
	@JsonIgnore
	@JaEdit(editable = JeEditable.DISABLE)
	protected List<Notifier> notifiers = new ArrayList<Notifier>();

	/**
	 * 通知
	 * 
	 * @author absir
	 *
	 */
	public abstract class Notifier implements Runnable {

		// 准备好了
		protected boolean posting;

		/**
		 * 初始化
		 */
		public Notifier() {
			notifiers.add(this);
		}

		/**
		 * 投递
		 */
		public void post() {
			posting = true;
			checkPosted();
		}

		/**
		 * 取消
		 */
		public void cancel() {
			posting = false;
		}

		/**
		 * 检查投递
		 */
		protected void checkPosted() {
			if (posting) {
				ContextUtils.getThreadPoolExecutor().execute(this);
			}
		}

		/**
		 * 执行
		 */
		public synchronized void run() {
			if (posting) {
				if (doPost()) {
					posting = false;
				}
			}
		}

		/**
		 * 执行投递
		 * 
		 * @return
		 */
		protected abstract boolean doPost();
	}

	// 更改通知
	@JsonIgnore
	private Notifier modifierNotifier = new Notifier() {

		@Override
		protected boolean doPost() {
			// TODO Auto-generated method stub
			return modifierNotifier();
		}
	};

	/**
	 * @return
	 */
	protected boolean modifierNotifier() {
		return SocketService.writeByteObject(socketChannel, SocketService.CALLBACK_MODIFY, "", true);
	}

	// 奖励通知
	@JsonIgnore
	private Notifier rewardNotifier = new Notifier() {

		@Override
		protected boolean doPost() {
			// TODO Auto-generated method stub
			return rewardNotifier();
		}
	};

	/**
	 * @return
	 */
	protected boolean rewardNotifier() {
		return SocketService.writeByteObject(socketChannel, SocketService.CALLBACK_REWARD, playerA.getRewardNumber(), true);
	}

	// 消息通知
	@JsonIgnore
	private Notifier messageNotifier = new Notifier() {

		@Override
		protected boolean doPost() {
			// TODO Auto-generated method stub
			return messageNotifier();
		}
	};

	/**
	 * @return
	 */
	protected boolean messageNotifier() {
		return SocketService.writeByteObject(socketChannel, SocketService.CALLBACK_MESSAGE, playerA.getMessageNumber(), true);
	}

	// 会话队列
	@JsonIgnore
	private UtilQueue<Object> chatQueue = createChatQueue();

	/**
	 * 创建会话队列
	 * 
	 * @return
	 */
	protected UtilQueue<Object> createChatQueue() {
		return new UtilQueueBlock<Object>(50);
	}

	/**
	 * @author absir
	 *
	 */
	protected abstract class NotifierQueue extends Notifier {

		/** runed */
		protected boolean runed;

		/** postObject */
		protected Object postObject;

		/**
		 * 清除通知
		 */
		public void clear() {
			cancel();
			postObject = null;
		}

		/**
		 * 检查投递
		 */
		protected synchronized void checkPosted() {
			if (posting && !runed) {
				runed = true;
				ContextUtils.getThreadPoolExecutor().execute(this);
			}
		}

		/**
		 * 执行
		 */
		public synchronized void run() {
			while (true) {
				if (postObject == null || SocketService.writeByteObject(socketChannel, getCallbackIndex(), postObject, true)) {
					postObject = getPostObject();
					if (!SocketService.writeByteObject(socketChannel, getCallbackIndex(), postObject, true)) {
						break;
					}

					if (!posting) {
						postObject = null;
						break;
					}
				}
			}

			runed = false;
		}

		/**
		 * 推送频道
		 * 
		 * @return
		 */
		protected abstract int getCallbackIndex();

		/**
		 * 推送频道
		 * 
		 * @return
		 */
		protected abstract Object getPostObject();

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.context.JbPlayerContext.Notifier#doPost()
		 */
		@Override
		protected boolean doPost() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	// 会话通知
	@JsonIgnore
	private Notifier chatNotifier = new NotifierQueue() {

		@Override
		protected Object getPostObject() {
			// TODO Auto-generated method stub
			return talkNotifier();
		}

		@Override
		protected int getCallbackIndex() {
			// TODO Auto-generated method stub
			return SocketService.CALLBACK_CHAT;
		}
	};

	/**
	 * @return
	 */
	protected Object talkNotifier() {
		return chatQueue.readElements(10);
	}

	/**
	 * @return the notifiers
	 */
	public List<Notifier> getNotifiers() {
		return notifiers;
	}

	/**
	 * @return the modifierNotifier
	 */
	public Notifier getModifierNotifier() {
		return modifierNotifier;
	}

	/**
	 * @return the rewardNotifier
	 */
	public Notifier getRewardNotifier() {
		return rewardNotifier;
	}

	/**
	 * @return the messageNotifier
	 */
	public Notifier getMessageNotifier() {
		return messageNotifier;
	}

	/**
	 * @return the chatQueue
	 */
	public UtilQueue<Object> getChatQueue() {
		return chatQueue;
	}

	/**
	 * @return the chatNotifier
	 */
	public Notifier getChatNotifier() {
		return chatNotifier;
	}

	/**
	 * 获取玩家全部卡牌
	 * 
	 * @return
	 */
	public Collection<C> getAllCards() {
		return playerCards.values();
	}

	/**
	 * 获取玩家基本信息
	 * 
	 * @return the player
	 */
	public P getPlayer() {
		return player;
	}

	/**
	 * 获取玩家全部数据
	 * 
	 * @return the playerA
	 */
	public A getPlayerA() {
		return playerA;
	}

	/**
	 * 获取登录时间
	 * 
	 * @return the onlineTime
	 */
	public long getOnlineTime() {
		return onlineTime;
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
		if (socketChannel == null) {
			player.setOnline(false);

		} else {
			player.setOnline(true);
			for (Notifier notifier : notifiers) {
				notifier.checkPosted();
			}
		}

		this.socketChannel = socketChannel;
	}

	/**
	 * @return the fight
	 */
	public F getFight() {
		return fight;
	}

	/**
	 * @param fight
	 *            the fight to set
	 */
	public void setFight(F fight) {
		if (this.fight != null) {
			this.fight.disconnect();
		}

		this.fight = fight;
	}

	/**
	 * 载入数据
	 */
	protected abstract void load();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.Context#initialize()
	 */
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		PlayerServiceBase.ME.loadPlayerContext(this);
		int lastOnlineDay = (int) (playerA.getLastOffline() / (24 * 3600000));
		onlineTime = ContextUtils.getContextTime();
		int onlineDay = (int) (onlineTime / (24 * 3600000));
		playerA.setOnlineDay(onlineDay);
		if (lastOnlineDay < onlineDay) {
			if (playerA.getLastOffline() == 0 || lastOnlineDay > onlineDay) {
				playerA.setOnline(playerA.getOnline() + 1);
			}
		}

		// 初始化自动回复
		if (player.getEp() < player.getMaxEp()) {
			int ep = (int) ((onlineTime - playerA.getLastOffline()) / getEpTaskInterval());
			modifyEp(ep < 0 ? 0 : ep);
		}

		// 计算卡牌数量
		countCardNumber();
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
	 * @see com.absir.context.core.ContextBean#stepDone(long)
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

		return super.stepDone(contextTime);
	}

	/**
	 * 保存数据
	 */
	protected abstract void save();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContext#uninitialize()
	 */
	@Override
	public void uninitialize() {
		// TODO Auto-generated method stub
		playerA.setLastOffline(ContextUtils.getContextTime());
		playerA.setOnlineTime(playerA.getOnlineTime() + playerA.getLastOffline() - onlineTime);
		PlayerServiceBase.ME.savePlayerContext(this);
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
	 * 更改玩家行动力
	 * 
	 * @param ep
	 */
	public synchronized void modifyEp(int ep) {
		ep += player.getEp();
		if (ep < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "ep");

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
	 * 更改玩家金钱
	 * 
	 * @param money
	 */
	public synchronized void modifyMoney(int money) {
		money += player.getMoney();
		if (money < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "money");
		}

		player.setMoney(money);
	}

	/**
	 * 更改玩家宝石
	 * 
	 * @param diamond
	 */
	public synchronized void modifyDiamond(int diamond) {
		diamond += player.getDiamond();
		if (diamond < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "diamond");
		}

		player.setDiamond(diamond);
	}

	/**
	 * 更改玩家友情点数
	 * 
	 * @param friendShipNumber
	 */
	public synchronized void modifyFriendShipNumber(int friendShipNumber) {
		friendShipNumber += player.getFriendshipNumber();
		if (friendShipNumber < 0) {
			throw new ServerException(ServerStatus.IN_FAILED, "friendShipNumber");
		}

		player.setFriendshipNumber(friendShipNumber);
	}

	/**
	 * 更改玩家签名
	 * 
	 * @param sign
	 */
	public void sign(String sign) {
		player.setSign(sign);
	}

	// 设置玩家等级
	public LevelExpCxt<P> playerExpCxt;

	/**
	 * 获取设置玩家等级
	 * 
	 * @return
	 */
	public LevelExpCxt<P> getPlayerExpCxt() {
		if (playerExpCxt == null) {
			playerExpCxt = new LevelExpCxt<P>() {

				@Override
				public void setLevel(P player, int level) {
					JbPlayerContext.this.modifyLevel(level);
				}
			};
		}

		return playerExpCxt;
	}

	/**
	 * 更改经验
	 * 
	 * @param exp
	 */
	public synchronized void modifyExp(int exp) {
		GameUtils.modifyExp(exp, player, getPlayerExpCxt(), COMPONENT.getPlayerExps(), COMPONENT.getPlayerMaxLevel(this));
	}

	/**
	 * 更改等级
	 * 
	 * @param level
	 */
	public synchronized void modifyLevel(int level) {
		if (player.getLevel() == level || level < 1 || level > COMPONENT.getPlayerMaxLevel(this)) {
			return;
		}

		PD playerDefine = (PD) COMPONENT.playerDefines.get(level);
		PD currentPlayerDefine = (PD) COMPONENT.playerDefines.get(player.getLevel());
		player.setMaxExp(playerDefine.getExp());
		player.setLevel(level);
		player.setMaxEp(player.getMaxEp() + playerDefine.getEp() - currentPlayerDefine.getEp());
		modifyEp(player.getMaxEp());
		player.setMaxCardNumber(player.getMaxCardNumber() + playerDefine.getCardNumber() - currentPlayerDefine.getCardNumber());
		player.setMaxFriendNumber(player.getMaxFriendNumber() + playerDefine.getFriendNumber() - currentPlayerDefine.getFriendNumber());
	}

	/**
	 * 更改等级
	 * 
	 * @param level
	 */
	protected void doModifyLevel(PD playerDefine, PD currentPlayerDefine) {
	}

	/**
	 * 计算卡牌总数
	 * 
	 */
	public void countCardNumber() {
		player.setCardNumber(playerCards.size());
	}

	/**
	 * 获取玩家卡牌
	 * 
	 * @param cardId
	 * @return
	 */
	public C getCard(long cardId) {
		C card = playerCards.get(cardId);
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
	public List<C> getCards(long[] cardIds) {
		List<C> cards = new ArrayList<C>();
		for (long cardId : cardIds) {
			cards.add(getCard(cardId));
		}

		return cards;
	}

	/**
	 * 生成卡牌
	 * 
	 * @param card
	 * @return
	 */
	protected C getResetCard(C card) {
		COMPONENT.resetCard(card);
		return card;
	}

	/**
	 * 获取卡牌
	 * 
	 * @param cardId
	 * @param level
	 * @return
	 */
	public C gainCard(int cardId, int level) {
		ICardDefine cardDefine = COMPONENT.getCardDefine(cardId);
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
	public C gainCard(ICardDefine cardDefine, int level) {
		C card = (C) COMPONENT.generateCard(cardDefine);
		COMPONENT.modifyCardLevel(card, level);
		return card;
	}

	/**
	 * 获取卡牌
	 * 
	 * @param card
	 * @return
	 */
	public C gainCard(C card) {
		card = PlayerServiceBase.ME.addPlayerCard(player, getResetCard(card));
		playerCards.put(card.getId(), card);
		return card;
	}

	/**
	 * 消耗卡牌
	 * 
	 * @param card
	 */
	protected void consumeCard(C card) {
		if (player.containCard(card)) {
			throw new ServerException(ServerStatus.IN_FAILED, "card");
		}

		playerCards.remove(card.getId());
		PlayerServiceBase.ME.removePlayerCard(player, card);
	}

	/**
	 * 消耗多张卡牌
	 * 
	 * @param cards
	 */
	protected void consumeCards(List<? extends C> cards) {
		for (C card : cards) {
			consumeCard(card);
		}
	}

	/**
	 * 阵型卡牌列表
	 * 
	 * @param cardIds
	 * @param cards
	 * @return
	 */
	public int formationCards(long[] cardIds, List<C> cards) {
		int formation = 0;
		for (long cardId : cardIds) {
			C card = playerCards.get(cardId);
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
		if (cardIds.length < 1 || cardIds.length > player.getMaxCardFormation()) {
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

		JbCard[] cards = new JbCard[length];
		int formation = 0;
		for (int i = 0; i < length; i++) {
			C card = playerCards.get(cardIds[i]);
			if (card != null) {
				formation++;
			}

			cards[i] = card;
		}

		if (formation == 0) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		String error = checkFormation(cards, false);
		if (error != null) {
			throw new ServerException(ServerStatus.IN_FAILED, error);
		}

		for (int i = 0; i < length; i++) {
			player.setCard(cards[i], i);
		}

		for (; length < player.getMaxCardFormation(); length++) {
			player.setCard(null, length);
		}
	}

	/**
	 * 检查阵型
	 * 
	 * @param cards
	 * @param force
	 * @return
	 */
	protected String checkFormation(JbCard[] cards, boolean force) {
		return null;
	}

	/**
	 * @param cardId
	 * @param position
	 */
	public synchronized void formation(long cardId, int position) {
		C card = getCard(cardId);
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

		C card = getCard(cardId);
		consumeCard(card);
		doSell(card);
		countCardNumber();
	}

	/**
	 * 出售卡牌
	 * 
	 * @param card
	 */
	protected abstract void doSell(C card);

	/**
	 * 出售卡牌(多张)
	 * 
	 * @param cardIds
	 */
	public synchronized void sell(long[] cardIds) {
		if (playerCards.size() <= cardIds.length) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		for (C card : getCards(cardIds)) {
			consumeCard(card);
			doSell(card);
		}

		countCardNumber();
	}

	/**
	 * 升级卡牌
	 * 
	 * @param cardId
	 * @param cardIds
	 * @return
	 */
	public synchronized C feed(long cardId, long[] cardIds) {
		if (HelperArray.contains(cardIds, cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		C target = getCard(cardId);
		if (target.getLevel() >= COMPONENT.getCardMaxLevel(target)) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		int allExp = 0;
		List<C> cards = getCards(cardIds);
		for (C card : cards) {
			allExp += COMPONENT.getFeedExp(target, card);
		}

		modifyMoney(COMPONENT.getFeedMoney(target, allExp, (List) cards));
		consumeCards(cards);
		COMPONENT.modifyCardExp(target, allExp);
		countCardNumber();
		return target;
	}

	/**
	 * 进化卡牌
	 * 
	 * @param cardId
	 * @param cardIds
	 * @return
	 */
	public synchronized C evolute(long cardId, long[] cardIds, IPropEvolute propEvolute) {
		if (HelperArray.contains(cardIds, cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		C target = getCard(cardId);
		List<C> cards = getCards(cardIds);
		int length = target.getCardDefine().getEvolutionRequires().length;
		if (length != cards.size()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		for (int i = 0; i < length; i++) {
			if (target.getCardDefine().getEvolutionRequires()[i] != cards.get(i).getCardDefine()) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}
		}

		modifyMoney(-target.getCardDefine().getEvolutionPrice());
		consumeCards(cards);
		countCardNumber();
		float evolute = COMPONENT.getEvoluteOdds(target);
		if (evolute < 1) {
			float odds = HelperRandom.RANDOM.nextFloat();
			if (propEvolute == null) {
				if (odds > evolute) {
					return null;
				}

			} else {
				if (odds > evolute * propEvolute.getLuck(target)) {
					return null;
				}
			}
		}

		COMPONENT.resetCard(target);
		if (player.containCard(target)) {
			checkFormation(player.getCards(), true);
		}

		return target;
	}

	/**
	 * 购买卡牌栏
	 * 
	 * @return
	 */
	public synchronized int card(int number) {
		modifyDiamond(-number * COMPONENT.PLAYER_CONFIGURE.getBuyCardNumber());
		number *= COMPONENT.PLAYER_CONFIGURE.getCardNumberAdd();
		player.setMaxCardNumber(player.getMaxCardNumber() + number);
		return number;
	}

	/**
	 * 购买好友数量
	 * 
	 * @return
	 */
	public synchronized int friend(int number) {
		modifyDiamond(-number * COMPONENT.PLAYER_CONFIGURE.getBuyFriendNumber());
		number *= COMPONENT.PLAYER_CONFIGURE.getFriendNumberAdd();
		player.setMaxFriendNumber(player.getMaxFriendNumber() + number);
		return number;
	}

	/**
	 * 修改道具
	 * 
	 * @param propDefine
	 * @param size
	 * @return
	 */
	public int modifyProp(IPropDefine propDefine, int size) {
		Map<Integer, Integer> propNumbers = playerA.getPropNumbers();
		synchronized (propNumbers) {
			Integer number = propNumbers.get(propDefine);
			if (number != null) {
				size += number;
			}

			if (size < 0) {
				throw new ServerException(ServerStatus.IN_FAILED, "prop not enough");
			}

			propNumbers.put(propDefine.getId(), size);
			return size;
		}
	}

	/**
	 * 购买道具
	 * 
	 * @param propDefine
	 * @param diamond
	 * @param size
	 * @return
	 */
	public int buyProp(IPropDefine propDefine, boolean diamond, int size) {
		if (diamond) {
			if (propDefine.getDiamond() <= 0) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			modifyDiamond(-propDefine.getDiamond() * size);

		} else {
			if (propDefine.getPrice() <= 0) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			modifyMoney(-propDefine.getPrice() * size);
		}

		return modifyProp(propDefine, size);
	}

	/**
	 * 出售道具
	 * 
	 * @param propDefine
	 * @param size
	 * @return
	 */
	public int sellProp(IPropDefine propDefine, int size) {
		Map<Integer, Integer> propNumbers = playerA.getPropNumbers();
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
			modifyMoney((int) (propDefine.getPrice() * size * 0.8f));

		} else {
			modifyDiamond((int) (propDefine.getDiamond() * size * 0.8f));
		}

		return number;
	}

	/**
	 * 领取奖励
	 * 
	 * @param reward
	 */
	public synchronized Object doReward(R reward) {
		Map<String, Object> rewardData = new HashMap<String, Object>();
		modifyMoney(reward.getMoney());
		modifyDiamond(reward.getDiamond());
		if (reward.getCardDefines() != null) {
			List<C> cards = new ArrayList<C>();
			rewardData.put("cards", cards);
			for (Entry<Integer, Integer> entry : reward.getCardDefines().entrySet()) {
				ICardDefine cardDefine = COMPONENT.getCardDefine(entry.getKey());
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
				IPropDefine propDefine = COMPONENT.getPropDefine(entry.getKey());
				if (propDefine != null) {
					for (int i = entry.getValue(); i > 0; i--) {
						entry.setValue(modifyProp(propDefine, entry.getValue()));
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
	public synchronized Object reward(IRewardDefine rewardDefine) {
		OReward rewardInvoker = rewardDefine.getRewardInvoker();
		String rewardId = rewardInvoker.getRewardId();
		if (rewardId == null) {
			rewardId = rewardDefine.getId();
		}

		Integer recard = rewardDefine.getRewardInvoker().reward(this, (Integer) playerA.getMetaRecards().get(rewardId));
		if (recard == null) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		Object rewardData = doReward((R) rewardDefine.getReward());
		playerA.getMetaRecards().put(rewardId, recard);
		return rewardData;
	}

	/**
	 * 领取奖励
	 * 
	 * @param rewardDefine
	 */
	public synchronized Object reward(long rewardId) {
		JbReward reward = PlayerServiceBase.ME.getPlayerReward(getId(), rewardId);
		if (reward == null) {
			throw new ServerException(ServerStatus.ON_DELETED);
		}

		int rewardNumber = playerA.getRewardNumber();
		if (rewardNumber > 0) {
			playerA.setRewardNumber(--rewardNumber);
		}

		return doReward((R) reward);
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
	public synchronized F task(int scene, int pass, int detail) {
		if (playerCards.size() >= player.getMaxCardNumber()) {
			throw new ServerException(ServerStatus.IN_FAILED, "CardNumber");
		}

		ITaskDefine taskDefine = COMPONENT.getTaskDefine(scene);
		if (taskDefine.getLevel() > player.getLevel()) {
			throw new ServerException(ServerStatus.IN_FAILED, "Level");
		}

		ITaskPass taskPass = taskDefine.getTaskPasses()[pass];
		ITaskDetail taskDetail = taskPass.getTaskDetails()[detail];
		String taskId = getTaskId(scene, pass);
		if (detail > 0) {
			Integer progress = (Integer) playerA.getTaskProgresses().get(taskId);
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
				Integer nProgress = (Integer) playerA.getTaskProgresses().get(nTaskId);
				if (nProgress == null) {
					throw new ServerException(ServerStatus.IN_FAILED, "Progress");
				}
			}
		}

		if (player.getEp() < taskDetail.getEp()) {
			throw new ServerException(ServerStatus.IN_FAILED, "Ep");
		}

		F fight = doTaskFight(taskId, scene, pass, detail, taskDefine, taskPass, taskDetail);
		setFight(fight);
		return fight;
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
	 * 获取任务战斗
	 * 
	 * @param taskId
	 * @param scene
	 * @param pass
	 * @param detail
	 * @param taskDefine
	 * @param taskPass
	 * @param taskDetail
	 * @return
	 */
	protected abstract F doTaskFight(String taskId, int scene, int pass, int detail, ITaskDefine taskDefine, ITaskPass taskPass, ITaskDetail taskDetail);
}
