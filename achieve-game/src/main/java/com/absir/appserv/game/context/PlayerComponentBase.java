/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午4:21:15
 */
package com.absir.appserv.game.context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.configure.conf.ConfigureUtils;
import com.absir.appserv.configure.xls.XlsBase;
import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbDiamond;
import com.absir.appserv.game.bean.JbFriend;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.bean.JbPlayerA;
import com.absir.appserv.game.bean.JbPlayerMessage;
import com.absir.appserv.game.bean.JbPlayerReward;
import com.absir.appserv.game.bean.value.IArenaDefine;
import com.absir.appserv.game.bean.value.ICardDefine;
import com.absir.appserv.game.bean.value.IPlayerDefine;
import com.absir.appserv.game.bean.value.IPropDefine;
import com.absir.appserv.game.bean.value.IRewardDefine;
import com.absir.appserv.game.bean.value.ITaskDefine;
import com.absir.appserv.game.bean.value.IVipDefine;
import com.absir.appserv.game.confiure.JPlayerConfigure;
import com.absir.appserv.game.context.value.IPropEvolute;
import com.absir.appserv.game.context.value.IPropPlayer;
import com.absir.appserv.game.utils.GameUtils;
import com.absir.appserv.game.value.IExp;
import com.absir.appserv.game.value.LevelExpCxt;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextUtils;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelClass;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class PlayerComponentBase<C extends JbCard, P extends JbPlayer, PC extends JbPlayerContext<C, P, ?, ?, ?, ?, ?>, PG extends JPlayerConfigure, PD extends IPlayerDefine, CD extends ICardDefine, CE extends IExp, VD extends IVipDefine, PP extends IPropDefine, RD extends IRewardDefine, TD extends ITaskDefine, AD extends IArenaDefine> {

	// 角色上下文类
	public final Class<PC> PLAYER_CONTEXT_CLASS;

	// 全部在线角色
	public final Map<Long, PC> PLAYER_CONTEXT_MAP;

	// 文件配置
	public final PG PLAYER_CONFIGURE;

	// 卡牌定义类型
	public final Class<? extends ICardDefine> CARD_DEFINE_CLASS;

	// 道具定义类型
	public final Class<? extends IPropDefine> PROP_DEFINE_CLASS;

	// 角色类
	public final Class<? extends JbPlayer> PLAYER_CLASS;

	// 角色附加属性类
	public final Class<? extends JbPlayerA> PLAYERA_CLASS;

	// 好友类
	public final Class<? extends JbFriend> FRIEND_CLASS;

	// 角色等级定义
	protected List<PD> playerDefines;

	// 卡牌定义
	protected XlsDao<CD, Serializable> cardDefineDao;

	// 卡牌等级定义
	protected List<IExp> cardExps;

	// 卡牌等级经验
	protected List<Integer> cardLevelExps = new ArrayList<Integer>();

	// VIP定义
	protected List<VD> vipDefines;

	// 道具定义
	protected XlsDao<PP, Serializable> propDefineDao;

	// 奖励定义
	protected XlsDao<RD, Serializable> rewardDefineDao;

	// 任务定义
	protected XlsDao<TD, Serializable> taskDefineDao;

	// 竞技场定义
	protected XlsDao<AD, Serializable> arenaDefineDao;

	/**
	 * 初始化
	 */
	public PlayerComponentBase() {
		Class<?>[] componentClasses = KernelClass.componentClasses(getClass());
		PLAYER_CONTEXT_CLASS = (Class<PC>) componentClasses[2];
		PLAYER_CONTEXT_MAP = (Map<Long, PC>) (Object) ContextUtils.getContextFactory().getContextMap(PLAYER_CONTEXT_CLASS);
		PLAYER_CONFIGURE = (PG) JConfigureUtils.getConfigure((Class<? extends JConfigureBase>) componentClasses[3]);
		CARD_DEFINE_CLASS = (Class<? extends ICardDefine>) componentClasses[5];
		PROP_DEFINE_CLASS = (Class<? extends IPropDefine>) componentClasses[8];

		componentClasses = KernelClass.componentClasses(PLAYER_CONTEXT_CLASS);
		PLAYER_CLASS = (Class<? extends JbPlayer>) componentClasses[1];
		PLAYERA_CLASS = (Class<? extends JbPlayerA>) componentClasses[2];
		FRIEND_CLASS = (Class<? extends JbFriend>) componentClasses[5];
	}

	/**
	 * 初始化组件
	 */
	@Inject
	public void reloadComponent() {
		// 读取配置文件
		DynaBinder.INSTANCE.mapBind(ConfigureUtils.readPropertyMap(new File(BeanFactoryUtils.getBeanConfig().getClassPath() + "conf/PlayComponent.conf")), this);
		// 初始化配置对象
		Class<?>[] componentClasses = KernelClass.componentClasses(getClass());
		playerDefines = DynaBinder.to(XlsUtils.getXlsBeans((Class<? extends XlsBase>) componentClasses[4]), List.class);
		cardDefineDao = (XlsDao<CD, Serializable>) XlsUtils.getXlsDao((Class<? extends XlsBase>) CARD_DEFINE_CLASS);
		cardExps = DynaBinder.to(XlsUtils.getXlsBeans((Class<? extends XlsBase>) componentClasses[6]), List.class);
		if (cardExps != null) {
			int exp = 0;
			cardLevelExps.clear();
			for (IExp iExp : cardExps) {
				exp += iExp.getExp();
				cardLevelExps.add(exp);
			}
		}

		vipDefines = DynaBinder.to(XlsUtils.getXlsBeans((Class<? extends XlsBase>) componentClasses[7]), List.class);
		propDefineDao = (XlsDao<PP, Serializable>) XlsUtils.getXlsDao((Class<? extends XlsBase>) PROP_DEFINE_CLASS);
		rewardDefineDao = (XlsDao<RD, Serializable>) XlsUtils.getXlsDao((Class<? extends XlsBase>) componentClasses[9]);
		taskDefineDao = (XlsDao<TD, Serializable>) XlsUtils.getXlsDao((Class<? extends XlsBase>) componentClasses[10]);
		arenaDefineDao = (XlsDao<AD, Serializable>) XlsUtils.getXlsDao((Class<? extends XlsBase>) componentClasses[11]);
	}

	/**
	 * 保存所有玩家数据
	 */
	public void saveAllPlayerContext() {
		for (JbPlayerContext playerContext : PLAYER_CONTEXT_MAP.values()) {
			playerContext.uninitialize();
		}
	}

	/**
	 * @return the cardDefineDao
	 */
	public XlsDao<CD, Serializable> getCardDefineDao() {
		return cardDefineDao;
	}

	/**
	 * @return the propDefineDao
	 */
	public XlsDao<PP, Serializable> getPropDefineDao() {
		return propDefineDao;
	}

	/**
	 * @return the arenaDefineDao
	 */
	public XlsDao<AD, Serializable> getArenaDefineDao() {
		return arenaDefineDao;
	}

	/**
	 * 创建卡牌
	 * 
	 * @return
	 */
	public abstract C createCard();

	/**
	 * 创建角色
	 * 
	 * @return
	 */
	public abstract JbPlayer createPlayer();

	/**
	 * 创建角色附加
	 * 
	 * @return
	 */
	public abstract JbPlayerA<?> createPlayerA();

	/**
	 * 创建好友关系
	 * 
	 * @return
	 */
	public abstract JbFriend createFriend();

	/**
	 * 创建用户奖励
	 * 
	 * @return
	 */
	public abstract JbPlayerReward createPlayerReward();

	/**
	 * 创建用户消息
	 * 
	 * @return
	 */
	public abstract JbPlayerMessage createPlayerMessage();

	/**
	 * 获取宝石
	 * 
	 * @param diamondId
	 * @return
	 */
	public abstract JbDiamond getDiamond(Long diamondId);

	/**
	 * 获取在线玩家
	 * 
	 * @param playerId
	 * @return
	 */
	public PC find(Long playerId) {
		return PLAYER_CONTEXT_MAP.get(playerId);
	}

	/**
	 * 在线玩家数据
	 * 
	 * @param player
	 * @return
	 */
	public <T extends JbPlayer> T onlinePlayer(T player) {
		PC playerContext = find(player.getId());
		return playerContext == null ? player : (T) playerContext.getPlayer();
	}

	/**
	 * 在线玩家数据
	 * 
	 * @param players
	 */
	public <T extends JbPlayer> List<T> onlinePlayers(List<T> players) {
		int size = players.size();
		T player;
		T onlinePlayer;
		for (int i = 0; i < size; i++) {
			player = players.get(i);
			onlinePlayer = onlinePlayer(player);
			if (player != onlinePlayer) {
				players.set(i, onlinePlayer);
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
	public <T extends JbPlayer> List<T> onlinePlayers(Iterator<T> playerIterator) {
		List<T> players = new ArrayList<T>();
		while (playerIterator.hasNext()) {
			players.add(onlinePlayer(playerIterator.next()));
		}

		return players;
	}

	/**
	 * 最大等级
	 * 
	 * @param playerContext
	 * @return
	 */
	public int getPlayerMaxLevel(PC playerContext) {
		return PLAYER_CONFIGURE.getMaxLevel();
	}

	/**
	 * 经验列表
	 * 
	 * @return
	 */
	public List<PD> getPlayerExps() {
		return playerDefines;
	}

	/**
	 * 重置卡牌
	 * 
	 * @param card
	 */
	public void resetCard(C card) {
		card.setExp(0);
		modifyCardLevel(card, 1);
	}

	/**
	 * 获取卡牌定义
	 * 
	 * @param cardId
	 * @return
	 */
	public CD getCardDefine(Serializable cardId) {
		return cardDefineDao.get(cardId);
	}

	/**
	 * 获取VIP定义
	 * 
	 * @param vip
	 * @return
	 */
	public VD getVipDefine(int vip) {
		if (vipDefines == null || vipDefines.isEmpty()) {
			return null;
		}

		if (vip <= 0) {
			return vipDefines.get(0);

		} else {
			int size = vipDefines.size();
			return vip < size ? vipDefines.get(vip) : vipDefines.get(size - 1);
		}
	}

	/**
	 * 获取道具定义
	 * 
	 * @param propId
	 * @return
	 */
	public PP getPropDefine(Serializable propId) {
		return propDefineDao.get(propId);
	}

	/**
	 * 获取道具定义
	 * 
	 * @param rewardId
	 * @return
	 */
	public RD getRewardDefine(Serializable rewardId) {
		return rewardDefineDao.get(rewardId);
	}

	/**
	 * 获取任务定义
	 * 
	 * @param taskId
	 * @return
	 */
	public TD getTaskDefine(Serializable taskId) {
		return taskDefineDao.get(taskId);
	}

	/**
	 * 创建卡牌
	 * 
	 * @param cardDefine
	 * @param level
	 * @return
	 */
	public C generateCard(ICardDefine cardDefine) {
		C card = createCard();
		card.setCardDefine(cardDefine);
		resetCard(card);
		return card;
	}

	/**
	 * 获取卡牌最大等级
	 * 
	 * @param card
	 * @return
	 */
	public int getCardMaxLevel(C card) {
		return card.getCardDefine().getMaxLevel();
	}

	// 设置卡牌等级
	/** CARD_LEVEL_EXP_CXT */
	protected final LevelExpCxt<C> CARD_LEVEL_EXP_CXT = new LevelExpCxt<C>() {

		@Override
		public void setLevel(C card, int level) {
			modifyCardLevel(card, level);
		}
	};

	/**
	 * 更改卡牌经验
	 * 
	 * @param card
	 * @param exp
	 */
	public void modifyCardExp(C card, int exp) {
		synchronized (card.getPlayer() == null ? card : card.getPlayer()) {
			GameUtils.modifyExp(exp, card, CARD_LEVEL_EXP_CXT, cardExps, getCardMaxLevel(card));
		}
	}

	/**
	 * 更改卡牌等级
	 * 
	 * @param card
	 * @param level
	 */
	public void modifyCardLevel(C card, int level) {
		if (card.getLevel() == level || level < 1 || level > getCardMaxLevel(card)) {
			return;
		}

		synchronized (card.getPlayer() == null ? card : card.getPlayer()) {
			doModifyCardLevel(card, level);
		}
	}

	/**
	 * 更改卡牌等级
	 * 
	 * @param card
	 * @param level
	 */
	protected void doModifyCardLevel(C card, int level) {
		card.setLevel(level);
	}

	/**
	 * 获取升级经验
	 * 
	 * @param target
	 * @param card
	 * @return
	 */
	public int getFeedExp(C target, C card) {
		return (cardLevelExps.get(card.getLevel()) + card.getExp());
	}

	/**
	 * 获取升级金钱
	 * 
	 * @param target
	 * @param allExp
	 * @return
	 */
	public int getFeedMoney(C target, int allExp, List<C> cards) {
		return target.getCardDefine().getFeedPrice() + allExp * 10;
	}

	/**
	 * 获取卡牌进化几率
	 * 
	 * @param target
	 * @return
	 */
	public float getEvoluteOdds(C target) {
		return 1;
	}

	/**
	 * 获取道具执行对象
	 * 
	 * @param propType
	 * @return
	 */
	public Object getPropInvoke(String propType) {
		Class<?> propClass = KernelClass.forName(IPropEvolute.class.getPackage().getName() + ".OProp_" + propType);
		return KernelClass.newInstance(propClass, this);
	}

	/**
	 * 获取奖励执行对象
	 * 
	 * @param rewardType
	 * @return
	 */
	public Object getRewardInvoke(String rewardType) {
		Class<?> rewardClass = KernelClass.forName(IPropPlayer.class.getPackage().getName() + ".OReward_" + rewardType);
		return KernelClass.newInstance(rewardClass, this);
	}

	/**
	 * 消费宝石
	 * 
	 * @param player
	 * @param diamond
	 */
	public void consumeDiamond(P player, int diamond) {
		player.setDiamond(player.getDiamond() + diamond);
		diamond += player.getConsume();
		player.setConsume(diamond);
		if (vipDefines != null) {
			int size = vipDefines.size();
			int current = player.getVip();
			int vip = current + 1;
			if (vip < 1) {
				vip = 1;
			}

			for (; vip < size && diamond >= vipDefines.get(vip).getConsume(); vip++) {

			}

			if (current != --vip) {
				VD currentVipDefine = getVipDefine(current);
				VD vipDefine = getVipDefine(vip);
				if (currentVipDefine == null || vipDefine == null || currentVipDefine == vipDefine) {
					player.setVip(vip);
					setPlayerVipDefine(player, currentVipDefine, vipDefine);
				}
			}
		}
	}

	/**
	 * 设置VIP等级
	 * 
	 * @param player
	 * @param currentVipDefine
	 * @param vipDefine
	 */
	protected void setPlayerVipDefine(P player, VD currentVipDefine, VD vipDefine) {
		player.setEpTimes(player.getEpTimes() + vipDefine.getEpTimes() - currentVipDefine.getEpTimes());
		player.setMoneyTimes(player.getMoneyTimes() + vipDefine.getMoneyTimes() - currentVipDefine.getMoneyTimes());
	}

	/**
	 * 刷新玩家数据
	 * 
	 * @param player
	 * @param playerContext
	 * @param online
	 * @param vipDefine
	 */
	protected void doUpdatePlayerDay(P player, PC playerContext, int online, IVipDefine vipDefine) {
		player.setEpTimes(vipDefine.getEpTimes());
		player.setMoneyTimes(vipDefine.getMoneyTimes());
	}

	/**
	 * 购买行动力
	 * 
	 * @param player
	 */
	protected void doEp(PC playerContext) {
		P player = playerContext.getPlayer();
		int epConsume = player.getEpConsume() + 1;
		playerContext.modifyDiamond(JbPlayerContext.CONFIGURE.getEpDiamond() * epConsume, false);
		player.setEpConsume(epConsume);
		playerContext.modifyEp(JbPlayerContext.CONFIGURE.getEpAdd(), true);
	}

	/**
	 * 购买金钱
	 * 
	 * @param player
	 */
	protected void doMoney(PC playerContext) {
		P player = playerContext.getPlayer();
		int moneyConsume = player.getMoneyConsume() + 1;
		playerContext.modifyDiamond(JbPlayerContext.CONFIGURE.getMoneyDiamond() * moneyConsume, false);
		player.setMoneyConsume(moneyConsume);
		VD vipDefine = getVipDefine(playerContext.getPlayer().getVip());
		playerContext.modifyMoney(vipDefine.getMoneyCrop(), true);
	}

}
