/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:11:40
 */
package com.absir.appserv.client.context;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.bean.JPlayerA;
import com.absir.appserv.client.bean.JPlayerReward;
import com.absir.appserv.client.bean.JbRewardBean;
import com.absir.appserv.client.bean.value.JeType;
import com.absir.appserv.client.service.SocketService;
import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.system.bean.JPlatformUser;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.JUserDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.async.value.Async;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextFactory;
import com.absir.context.core.ContextMap;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.core.util.UtilAbsir;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked" })
@Bean
public abstract class PlayerService {

	/** ME */
	public static final PlayerService ME = BeanFactoryUtils.get(PlayerService.class);

	/**
	 * 角色列表
	 * 
	 * @param userBase
	 * @return
	 */
	public List<JPlayer> players(Long serverId, JiUserBase userBase) {
		JPlatformUser platformUser = ((JPlatformUser) userBase);
		boolean userDirty = false;
		Session session = BeanDao.getSession();
		if (serverId == null) {
			serverId = platformUser.getServerId();

		} else if (!serverId.equals(platformUser.getServerId())) {
			userDirty = true;
			platformUser.setServerId(serverId);
		}

		List<JPlayer> players = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JPlayer o WHERE o.serverId = ? AND o.userId = ?", serverId, platformUser.getId()).list();
		Long playerId = platformUser.getPlayerId();
		if (playerId != null && !players.isEmpty()) {
			Long matchId = null;
			for (JPlayer player : players) {
				if (player.getId().equals(playerId)) {
					matchId = playerId;
					break;
				}
			}

			if (matchId == null) {
				matchId = players.get(0).getId();
				userDirty = true;
				platformUser.setPlayerId(matchId);
			}
		}

		if (userDirty) {
			session.merge(platformUser);
		}

		return players;
	}

	/**
	 * 创建角色
	 * 
	 * @param serverId
	 * @param userBase
	 * @param name
	 * @param sex
	 * @return
	 */
	@Transaction(rollback = Exception.class)
	public JPlayer create(Long serverId, JiUserBase userBase, String name, boolean sex) {
		JPlatformUser platformUser = null;
		if (userBase instanceof JPlatformUser) {
			platformUser = ((JPlatformUser) userBase);

		} else {
			platformUser = JUserDao.ME.findByPlatformUsername(null, userBase.getUsername());
			if (platformUser == null) {
				platformUser = new JPlatformUser();
				platformUser.setPlatform(",");
				platformUser.setUsername(userBase.getUsername());
				BeanDao.getSession().persist(platformUser);
			}
		}

		if (serverId == null) {
			serverId = platformUser.getServerId();

		} else {
			platformUser.setServerId(serverId);
		}

		JPlayer player = new JPlayer();
		player.setServerId(serverId);
		player.setUserId(userBase.getUserId());
		player.setName(name);
		player.setSex(sex);
		Session session = BeanDao.getSession();
		session.persist(player);
		platformUser.setPlayerId(player.getId());
		session.merge(platformUser);
		return player;
	}

	/**
	 * 获取用户角色ID
	 * 
	 * @param userBase
	 * @return
	 */
	public Long getPlayerId(Long serverId, JiUserBase userBase) {
		if (userBase instanceof JPlatformUser) {
			return ((JPlatformUser) userBase).getPlayerId();
		}

		JPlatformUser platformUser = getPlatformUser(userBase);
		return platformUser == null ? null : platformUser.getPlayerId();
	}

	/**
	 * @param 查找关联平台账号
	 * @return
	 */
	@Transaction(readOnly = true)
	protected JPlatformUser getPlatformUser(JiUserBase userBase) {
		return JUserDao.ME.findByPlatformUsername(",", userBase.getUsername());
	}

	/**
	 * 查找用户角色ID
	 * 
	 * @param serverId
	 * @param userId
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o.id FROM JPlayer o WHERE o.serverId = ? AND o.userId = ?")
	public abstract Long playerId(Long serverId, Long userId);

	/**
	 * 查找用户角色ID
	 * 
	 * @param id
	 * @param serverId
	 * @param userId
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o.id FROM JPlayer o WHERE o.id = ? AND o.serverId = ? AND o.userId = ?")
	public abstract Long playerId(Long id, Long serverId, Long userId);

	/**
	 * 查找用户角色ID
	 * 
	 * @param playerId
	 * @param serverId
	 * @param userId
	 * @return
	 */
	public Long getPlayerId(Long playerId, Long serverId, Long userId) {
		return playerId == null ? ME.playerId(serverId, userId) : ME.playerId(playerId, serverId, userId);
	}

	/**
	 * 载入玩家
	 * 
	 * @param id
	 * @return
	 */
	@Transaction(readOnly = true)
	public void loadPlayerContext(PlayerContext playerContext) {
		Long id = playerContext.getId();
		Session session = BeanDao.getSession();
		JPlayer player = BeanDao.get(session, JPlayer.class, id);
		if (player == null) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		playerContext.player = player;
		Map<Long, JCard> playerCards = playerContext.playerCards;
		Iterator<JCard> cardIterator = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JCard o WHERE o.player = ?", player).iterate();
		while (cardIterator.hasNext()) {
			JCard card = cardIterator.next();
			if (card.getCardDefine() != null) {
				playerCards.put(card.getId(), card);
			}
		}

		JPlayerA playerA = BeanDao.get(session, JPlayerA.class, id);
		if (playerA == null) {
			playerA = new JPlayerA();
			playerA.setId(id);

		} else {
			Iterator<Entry<Integer, Integer>> iterator = playerA.getPropNumbers().entrySet().iterator();
			while (iterator.hasNext()) {
				if (PlayerContext.PROP_DEFINE_XLS_DAO.get(iterator.next().getKey()) == null) {
					iterator.remove();
				}
			}
		}

		playerA.setRewardNumber(QueryDaoUtils.firstTo(QueryDaoUtils.createQueryArray(session, "SELECT COUNT(o) FROM JPlayerReward o WHERE o.playerId = ?", id), int.class));
		playerA.setMessageNumber(QueryDaoUtils.firstTo(
				QueryDaoUtils.createQueryArray(session, "SELECT SUM(o.messageNumber) FROM JFollow o WHERE o.id.eid = ? AND o.following = TRUE AND o.follower = TRUE", id), int.class));
		playerContext.playerA = playerA;
	}

	/**
	 * 领取奖励
	 * 
	 * @param playerId
	 * @param rewardId
	 * @return
	 */
	@Transaction
	public JPlayerReward getPlayerReward(Long playerId, Long rewardId) {
		Session session = BeanDao.getSession();
		Iterator<Object> iterator = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JPlayerReward o WHERE o.id = ? AND o.playerId = ?", rewardId, playerId).iterate();
		if (iterator.hasNext()) {
			JPlayerReward playerReward = (JPlayerReward) iterator.next();
			session.delete(playerReward);
			return playerReward;
		}

		return null;
	}

	/**
	 * 添加奖励
	 * 
	 * @param playerId
	 * @param rewardBean
	 * @param name
	 * @param type
	 * @param data
	 */
	@Transaction
	public void addPlayerReward(Long playerId, JbRewardBean rewardBean, String name, JeType type, String data) {
		JPlayerReward playerReward = new JPlayerReward();
		playerReward.setPlayerId(playerId);
		playerReward.setCreateTime(ContextUtils.getContextTime());
		playerReward.setRewardBean(rewardBean);
		playerReward.setName(name);
		playerReward.setType(type);
		playerReward.setData(data);
		BeanDao.getSession().persist(playerReward);
		// 通知在线用户
		PlayerContext playerContext = PlayerContext.find(playerId);
		if (playerContext != null) {
			JPlayerA playerA = playerContext.getPlayerA();
			int rewardNumber;
			synchronized (playerA) {
				rewardNumber = playerA.getRewardNumber();
				playerA.setRewardNumber(++rewardNumber);
			}

			SocketService.writeByteObject(playerContext.getSocketChannel(), SocketService.CALLBACK_REWARD, String.valueOf(rewardNumber));
		}
	}

	/**
	 * 获取用户IDS
	 * 
	 * @param playerIds
	 * @return
	 */
	@DataQuery("SELECT o.id FROM JPlayer o WHERE o.id IN :p0")
	protected abstract Long[] getPlayerIds(long[] playerIds);

	/**
	 * 获取用户IDS
	 * 
	 * @param playerIds
	 * @return
	 */
	@DataQuery("SELECT o.id FROM JPlayer o WHERE o.serverId IN :p0")
	protected abstract Long[] getPlayerIdsFromServerIds(long[] serverIds);

	/**
	 * 添加奖励
	 * 
	 * @param playerId
	 * @param rewardBean
	 * @param name
	 * @param type
	 * @param data
	 */
	@Async
	@Transaction
	public void addPlayerReward(long[] playerIds, long[] serverIds, JbRewardBean rewardBean, String name, JeType type, String data) {
		if (playerIds != null && playerIds.length > 0) {
			for (Long playerId : getPlayerIds(playerIds)) {
				addPlayerReward(playerId, rewardBean, name, type, data);
			}

		} else if (serverIds != null && serverIds.length > 0) {
			for (Long playerId : getPlayerIdsFromServerIds(serverIds)) {
				addPlayerReward(playerId, rewardBean, name, type, data);
			}

		} else {
			for (Long playerId : (List<Long>) QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o.id FROM JPlayer o").list()) {
				addPlayerReward(playerId, rewardBean, name, type, data);
			}
		}
	}

	/** REWARD_PAGE_SIZE */
	private static final int REWARD_PAGE_SIZE = 10;

	/**
	 * 奖励列表
	 * 
	 * @param playerId
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JPlayerReward> getPlayerRewards(Long playerId, int pageIndex) {
		if (--pageIndex < 0) {
			pageIndex = 0;
		}

		Query query = QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JPlayerReward o WHERE o.playerId = ?", playerId);
		return query.setFirstResult(pageIndex * REWARD_PAGE_SIZE).setMaxResults(REWARD_PAGE_SIZE).setCacheable(true).list();
	}

	/**
	 * 保存玩家
	 * 
	 * @param player
	 */
	@Transaction
	public void savePlayerContext(PlayerContext playerContext) {
		Session session = BeanDao.getSession();
		JPlayer player = playerContext.getPlayer();
		session.merge(player);
		session.merge(playerContext.getPlayerA());
		for (JCard card : playerContext.getAllCards()) {
			session.merge(card);
		}
	}

	/**
	 * 添加玩家卡牌
	 * 
	 * @param player
	 * @param card
	 */
	@Transaction
	public JCard addPlayerCard(JPlayer player, JCard card) {
		card.setId(null);
		card.setPlayer(player);
		BeanDao.getSession().persist(card);
		return card;
	}

	/**
	 * 删除玩家卡牌
	 * 
	 * @param player
	 * @param card
	 */
	@Transaction
	public void removePlayerCard(JPlayer player, JCard card) {
		Session session = BeanDao.getSession();
		session.delete(session.merge(card));
	}

	/**
	 * 查询玩家
	 * 
	 * @param playerId
	 * @return
	 */
	@Transaction(readOnly = true)
	protected JPlayer findPlayer(Long playerId) {
		return BeanDao.get(BeanDao.getSession(), JPlayer.class, playerId);
	}

	/**
	 * 获取玩家
	 * 
	 * @param playerId
	 * @return
	 */
	public JPlayer getPlayer(Long playerId) {
		PlayerContext playerContext = PlayerContext.find(playerId);
		return playerContext == null ? ME.findPlayer(playerId) : playerContext.getPlayer();
	}

	/**
	 * 保存玩家修改
	 * 
	 * @param playerId
	 * @return
	 */
	@Transaction(rollback = Throwable.class)
	protected void mergePlayer(Long playerId, CallbackTemplate<JPlayer> playerModifier) {
		Session session = BeanDao.getSession();
		JPlayer player = BeanDao.get(session, JPlayer.class, playerId);
		playerModifier.doWith(player);
		session.save(player);
	}

	/**
	 * 修改玩家属性
	 * 
	 * @param playerId
	 * @param player
	 */
	public void modifyPlayer(Long playerId, CallbackTemplate<JPlayer> playerModifier) {
		String tokenId = UtilAbsir.getId(PlayerContext.class, playerId);
		ContextFactory contextFactory = ContextUtils.getContextFactory();
		try {
			synchronized (contextFactory.getToken(tokenId)) {
				PlayerContext playerContext = PlayerContext.find(playerId);
				if (playerContext == null) {
					mergePlayer(playerId, playerModifier);

				} else {
					JPlayer player = playerContext.getPlayer();
					ContextMap contextMap = new ContextMap(player);
					synchronized (playerContext) {
						playerModifier.doWith(playerContext.getPlayer());
					}

					if (playerContext.isExpiration()) {
						playerContext.uninitialize();
					}

					SocketChannel socketChannel = playerContext.getSocketChannel();
					if (socketChannel != null) {
						SocketService.writeByteObject(socketChannel, SocketService.CALLBACK_MODIFY, contextMap.comparedMap());
					}
				}
			}

		} finally {
			contextFactory.clearToken(tokenId);
		}
	}

	/**
	 * 更新玩家在线天数
	 */
	@Schedule(cron = "0 0 0 * * *")
	protected static void updateOnlines() {
		long contextTime = ContextUtils.getContextTime();
		for (PlayerContext playerContext : PlayerContext.PLAYER_CONTEXT_MAP.values()) {
			playerContext.updateOnlineDay(contextTime);
		}
	}
}
