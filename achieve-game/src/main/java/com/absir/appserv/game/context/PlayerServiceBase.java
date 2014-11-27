/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:11:40
 */
package com.absir.appserv.game.context;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.bean.JbReward;
import com.absir.appserv.game.bean.value.IRewardBean;
import com.absir.appserv.game.service.SocketService;
import com.absir.appserv.system.bean.JPlatformUser;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.bean.basis.Base;
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
import com.absir.server.socket.SocketServerContext;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Base
@Bean
public abstract class PlayerServiceBase {

	/** ME */
	public static final PlayerServiceBase ME = BeanFactoryUtils.get(PlayerServiceBase.class);

	/**
	 * 查询平台用户
	 * 
	 * @param platform
	 * @param username
	 * @return
	 */
	@DataQuery("SELECT o FROM JPlatformUser o WHERE o.platform = ? AND o.username = ?")
	public abstract JPlatformUser findByPlatformUsername(String platform, String username);

	/**
	 * @param 查找关联平台账号
	 * @return
	 */
	@Transaction(readOnly = true)
	protected JPlatformUser getPlatformUser(JiUserBase userBase) {
		return ME.findByPlatformUsername(",", userBase.getUsername());
	}

	/**
	 * 创建平台账号
	 * 
	 * @param userBase
	 * @return
	 */
	@Transaction
	protected JPlatformUser createPlatformUser(JiUserBase userBase) {
		JPlatformUser platformUser = new JPlatformUser();
		platformUser.setPlatform(",");
		platformUser.setUsername(userBase.getUsername());
		BeanDao.getSession().persist(platformUser);
		return platformUser;
	}

	/**
	 * 选择平台服务区
	 * 
	 * @param platformUser
	 * @param serverId
	 */
	@Transaction
	protected void selectServerId(JPlatformUser platformUser, long serverId) {
		platformUser.setServerId(serverId);
		platformUser.setPlayerId(ME.getPlayerId(null, serverId, platformUser.getUserId()));
		BeanDao.getSession().merge(platformUser);
	}

	/**
	 * 获取用户角色ID
	 * 
	 * @param userBase
	 * @return
	 */
	public Long getPlayerId(Long serverId, JiUserBase userBase) {
		JPlatformUser platformUser = userBase instanceof JPlatformUser ? (JPlatformUser) userBase : getPlatformUser(userBase);
		if (platformUser == null) {
			return null;
		}

		if (serverId != null && !(serverId.equals(platformUser.getServerId()))) {
			if (SocketServerContext.ME.getServerContext(serverId) == null) {
				throw new ServerException(ServerStatus.ON_DENIED);
			}

			ME.selectServerId(platformUser, serverId);
		}

		return platformUser.getPlayerId();
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
	 * 过滤字段
	 * 
	 * @param name
	 * @return
	 */
	@Transaction(readOnly = true)
	public boolean findFilter(String name) {
		return QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o.id FROM JFilter o WHERE ? LIKE o.id", '%' + name + '%').iterate().hasNext();
	}

	/**
	 * 角色列表
	 * 
	 * @param userBase
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JbPlayer> players(Long serverId, JiUserBase userBase) {
		JPlatformUser platformUser = ((JPlatformUser) userBase);
		boolean userDirty = false;
		Session session = BeanDao.getSession();
		if (serverId == null) {
			serverId = platformUser.getServerId();

		} else if (!serverId.equals(platformUser.getServerId())) {
			userDirty = true;
			platformUser.setServerId(serverId);
		}

		List<JbPlayer> players = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JPlayer o WHERE o.serverId = ? AND o.userId = ?", serverId, platformUser.getId()).list();
		Long playerId = platformUser.getPlayerId();
		if (playerId != null && !players.isEmpty()) {
			Long matchId = null;
			for (JbPlayer player : players) {
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
	public JbPlayer create(Long serverId, JiUserBase userBase, String name, int gender) {
		JPlatformUser platformUser = null;
		if (userBase instanceof JPlatformUser) {
			platformUser = ((JPlatformUser) userBase);

		} else {
			platformUser = ME.getPlatformUser(userBase);
			if (platformUser == null) {
				platformUser = createPlatformUser(userBase);
			}
		}

		if (serverId == null) {
			serverId = platformUser.getServerId();

		} else {
			platformUser.setServerId(serverId);
		}

		JbPlayer player = JbPlayerContext.COMPONENT.createPlayer();
		player.setServerId(serverId);
		player.setUserId(userBase.getUserId());
		player.setName(name);
		player.setGender(gender);
		Session session = BeanDao.getSession();
		session.persist(player);
		platformUser.setPlayerId(player.getId());
		session.merge(platformUser);
		return player;
	}

	/**
	 * 载入玩家
	 * 
	 * @param playerContext
	 * @return
	 */
	@Transaction(readOnly = true)
	public void loadPlayerContext(JbPlayerContext playerContext) {
		playerContext.load();
	}

	/**
	 * 保存玩家
	 * 
	 * @param playerContext
	 */
	@Transaction
	public void savePlayerContext(JbPlayerContext playerContext) {
		playerContext.save();
	}

	/**
	 * 获取玩家
	 * 
	 * @param playerId
	 * @return
	 */
	public JbPlayer findPlayer(Long playerId) {
		JbPlayerContext playerContext = JbPlayerContext.COMPONENT.find(playerId);
		return playerContext == null ? getPlayer(playerId) : playerContext.getPlayer();
	}

	/**
	 * @param playerId
	 * @return
	 */
	@Transaction(readOnly = true)
	protected JbPlayer getPlayer(Long playerId) {
		return BeanDao.get(BeanDao.getSession(), JbPlayerContext.COMPONENT.PLAYER_CLASS, playerId);
	}

	/**
	 * 获取玩家列表
	 * 
	 * @param playerIds
	 * @return
	 */
	public List<JbPlayer> findPlayers(List<Long> playerIds) {
		List<JbPlayer> players = new ArrayList<JbPlayer>(playerIds.size());
		Map<Long, Integer> unfinds = null;
		JbPlayerContext playerContext;
		JbPlayer player;
		int i = 0;
		for (Long playerId : playerIds) {
			if (playerId == null) {
				continue;
			}

			playerContext = JbPlayerContext.COMPONENT.find(playerId);
			if (playerContext == null) {
				player = null;
				if (unfinds == null) {
					unfinds = new HashMap<Long, Integer>();
				}

				unfinds.put(playerId, i);

			} else {
				player = playerContext.getPlayer();
			}

			players.add(player);
			i++;
		}

		if (unfinds != null) {
			for (JbPlayer getPlayer : getPlayers(unfinds.keySet())) {
				Integer index = unfinds.get(getPlayer.getId());
				if (index != null) {
					players.set(index, getPlayer);
				}
			}
		}

		return players;
	}

	/**
	 * 获取玩家列表
	 * 
	 * @param playerIds
	 * @return
	 */
	@Transaction(readOnly = true)
	protected List<JbPlayer> getPlayers(Collection<Long> playerIds) {
		return QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JPlayer o WHERE o.id in (:ids)").setParameterList("ids", playerIds).list();
	}

	/**
	 * 添加玩家卡牌
	 * 
	 * @param player
	 * @param card
	 */
	@Transaction
	public <C extends JbCard> C addPlayerCard(JbPlayer player, C card) {
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
	public void removePlayerCard(JbPlayer player, JbCard card) {
		Session session = BeanDao.getSession();
		session.delete(session.merge(card));
	}

	/**
	 * 领取奖励
	 * 
	 * @param playerId
	 * @param rewardId
	 * @return
	 */
	@Transaction
	public JbReward getPlayerReward(Long playerId, Long rewardId) {
		Session session = BeanDao.getSession();
		Iterator<Object> iterator = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JPlayerReward o WHERE o.id = ? AND o.playerId = ?", rewardId, playerId).iterate();
		if (iterator.hasNext()) {
			IRewardBean rewardBean = (IRewardBean) iterator.next();
			session.delete(rewardBean);
			return rewardBean.getReward();
		}

		return null;
	}

	/**
	 * 奖励列表数量
	 * 
	 * @return
	 */
	protected int getRewardPageSize() {
		return 10;
	}

	/**
	 * 奖励列表
	 * 
	 * @param playerId
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<IRewardBean> getPlayerRewards(Long playerId, int pageIndex) {
		if (--pageIndex < 0) {
			pageIndex = 0;
		}

		Query query = QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JPlayerReward o WHERE o.playerId = ?", playerId);
		return query.setFirstResult(pageIndex * getRewardPageSize()).setMaxResults(getRewardPageSize()).setCacheable(true).list();
	}

	/**
	 * 保存玩家修改
	 * 
	 * @param playerId
	 * @return
	 */
	@Transaction(rollback = Throwable.class)
	protected void mergePlayer(Long playerId, CallbackTemplate<JbPlayer> playerModifier) {
		Session session = BeanDao.getSession();
		JbPlayer player = BeanDao.get(session, JbPlayerContext.COMPONENT.PLAYER_CLASS, playerId);
		playerModifier.doWith(player);
		session.save(player);
	}

	/**
	 * 修改玩家属性
	 * 
	 * @param playerId
	 * @param player
	 */
	public void modifyPlayer(Long playerId, CallbackTemplate<JbPlayer> playerModifier) {
		String tokenId = UtilAbsir.getId(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, playerId);
		ContextFactory contextFactory = ContextUtils.getContextFactory();
		try {
			synchronized (contextFactory.getToken(tokenId)) {
				JbPlayerContext playerContext = JbPlayerContext.COMPONENT.find(playerId);
				if (playerContext == null) {
					mergePlayer(playerId, playerModifier);

				} else {
					JbPlayer player = playerContext.getPlayer();
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
	@Schedule(cron = "0 24 * * * *")
	protected static void updateOnlines() {
		long contextTime = ContextUtils.getContextTime();
		for (JbPlayerContext playerContext : (Collection<JbPlayerContext>) JbPlayerContext.COMPONENT.PLAYER_CONTEXT_MAP.values()) {
			playerContext.updateOnlineTime(contextTime);
		}
	}
}
