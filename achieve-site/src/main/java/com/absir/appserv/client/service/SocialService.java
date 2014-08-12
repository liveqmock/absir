/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-6 下午4:15:04
 */
package com.absir.appserv.client.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JFollow;
import com.absir.appserv.client.bean.JFollowMessage;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.bean.JPlayerA;
import com.absir.appserv.client.bean.JShared;
import com.absir.appserv.client.configure.JSocialConfigure;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.client.context.value.OFriender;
import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.bean.JEmbedLS;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.helper.HelperCommon;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextMap;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelDyna;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
public abstract class SocialService {

	/** ME */
	public static final SocialService ME = BeanFactoryUtils.get(SocialService.class);

	/** socialConfigure */
	JSocialConfigure socialConfigure = JConfigureUtils.getConfigure(JSocialConfigure.class);

	/**
	 * 添加(取消)好友
	 * 
	 * @param playerContext
	 * @param friend
	 * @return
	 */
	@Transaction(rollback = Exception.class)
	public boolean follow(PlayerContext playerContext, JPlayer friend) {
		if (playerContext.getId().equals(friend.getId())) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		synchronized (playerContext) {
			JEmbedLL embedLL = new JEmbedLL();
			embedLL.setEid(playerContext.getId());
			embedLL.setMid(friend.getId());
			Session session = BeanDao.getSession();
			JFollow follow = BeanDao.get(session, JFollow.class, embedLL);
			boolean created = false;
			if (follow == null) {
				// 创建关系
				follow = new JFollow();
				follow.setId(embedLL);
				try {
					session.persist(follow);

				} catch (Throwable e) {
					// TODO: handle exception
				}

			} else {
				created = true;
				session.evict(follow);
			}

			// 关系更改条件
			int friendNumber = playerContext.getPlayer().getFriendNumber();
			if (follow.isFollowing()) {
				if (follow.isEncouraging()) {
					throw new ServerException(ServerStatus.ON_FAIL);
				}

				follow.setFollowing(false);

			} else {
				if (friendNumber >= playerContext.getPlayer().getMaxFriendNumber()) {
					throw new ServerException(ServerStatus.ON_FAIL);
				}

				follow.setFollowing(true);
			}

			// 创建对方关系
			embedLL = new JEmbedLL();
			embedLL.setEid(follow.getId().getMid());
			embedLL.setMid(follow.getId().getEid());
			JFollow eFollow = BeanDao.get(session, JFollow.class, embedLL);
			if (eFollow == null) {
				try {
					// 创建关系
					eFollow = new JFollow();
					eFollow.setId(embedLL);
					session.persist(eFollow);

				} catch (Throwable e) {
					// TODO: handle exception
				}
			}

			// 保证数据提前提交
			session.flush();

			// 更改相互关注状态
			if (created) {
				QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.following = ? WHERE o.id.eid = ? AND o.id.mid = ?", follow.isFollowing(), embedLL.getMid(), embedLL.getEid())
						.executeUpdate();
			}

			QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.follower = ? WHERE o.id.eid = ? AND o.id.mid = ?", follow.isFollowing(), embedLL.getEid(), embedLL.getMid())
					.executeUpdate();
			// 更改好友数量
			playerContext.getPlayer().setFriendNumber(friendNumber + (follow.isFollowing() ? 1 : -1));
			return follow.isFollowing();
		}
	}

	/**
	 * 应援好友
	 * 
	 * @param playerContext
	 * @param friendId
	 */
	@Transaction(rollback = Exception.class)
	public synchronized void encourage(PlayerContext playerContext, long friendId) {
		synchronized (playerContext) {
			JEmbedLL embedLL = new JEmbedLL();
			embedLL.setEid(playerContext.getId());
			embedLL.setMid(friendId);
			Session session = BeanDao.getSession();
			JFollow follow = BeanDao.get(session, JFollow.class, embedLL);
			if (follow == null || follow.isEncouraging()) {
				throw new ServerException(ServerStatus.ON_FAIL);
			}

			// 应援好友状态生效
			QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.encouraging = ? , o.encouragingTime = ? WHERE o.id.eid = ? AND o.id.mid = ?", true, ContextUtils.getContextTime(),
					embedLL.getEid(), embedLL.getMid()).executeUpdate();
			playerContext.modifyFriendShipNumber(socialConfigure.getEncourageFriendShip());
		}
	}

	/**
	 * 一键应援
	 * 
	 * @param playerContext
	 */
	@Transaction(rollback = Exception.class)
	public void encourageAll(PlayerContext playerContext) {
		synchronized (playerContext) {
			int encourageNumber = QueryDaoUtils.createQueryArray(BeanDao.getSession(), "UPDATE JFollow o SET o.encouraging = ? , o.encouragingTime = ? WHERE o.id.eid = ? AND o.encouraging = ?", true,
					ContextUtils.getContextTime(), playerContext.getId(), false).executeUpdate();
			playerContext.modifyFriendShipNumber(socialConfigure.getEncourageFriendShip() * encourageNumber);
		}
	}

	/**
	 * 应援过期
	 */
	@Schedule(fixedDelay = 3600000)
	@Transaction
	public void encouragePass() {
		QueryDaoUtils.createQueryArray(BeanDao.getSession(), "UPDATE JFollow o SET o.encouraging = ? WHERE o.encouraging = ? AND o.encouragingTime < ?", false, true,
				ContextUtils.getContextTime() - 8 * 3600000).executeUpdate();
	}

	/**
	 * 获取应援卡牌
	 * 
	 * @return
	 */
	@Transaction
	public JCard getEncourageCard(JPlayer player) {
		Session session = BeanDao.getSession();
		JFollow follow = (JFollow) QueryDaoUtils.first(QueryDaoUtils.createQueryArray(session, "SELECT o FROM JFollow o WHERE o.id.mid = ? AND o.encouraging = ? ORDER BY o.encouragingTime",
				player.getId(), true));
		if (follow != null) {
			try {
				// 设置应援已经使用
				QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.encouraging = ? WHERE o.id.eid = ? AND o.id.mid = ?", false, follow.getId().getEid(), follow.getId().getMid())
						.executeUpdate();

			} catch (Throwable e) {
			}

			return PlayerService.ME.getPlayer(follow.getId().getEid()).getCard0();
		}

		return null;
	}

	/**
	 * 关注列表
	 * 
	 * @param player
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery(value = "SELECT o FROM JFollow o WHERE o.id.eid = ? AND o.following = TRUE")
	protected abstract List<JFollow> following(Long playerId, JdbcPage jdbcPage);

	/**
	 * 关注列表
	 * 
	 * @param playerContext
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<OFriender> following(PlayerContext player, JdbcPage jdbcPage) {
		List<JFollow> follows = ME.following(player.getId(), jdbcPage);
		List<OFriender> frienders = new ArrayList<OFriender>(follows.size());
		for (JFollow follow : follows) {
			OFriender friender = new OFriender(follow);
			friender.setPlayer(PlayerService.ME.getPlayer(follow.getId().getMid()));
			frienders.add(friender);
		}

		// 刷新好友数量
		player.getPlayer().setFriendNumber((int) jdbcPage.getTotalCount());
		return frienders;
	}

	/**
	 * 被关注列表
	 * 
	 * @param playerId
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery(value = "SELECT o FROM JFollow o WHERE o.id.mid = ? AND o.following = TRUE @ ORDER BY o.encouraging DESC, o.encouragingTime DESC")
	public abstract List<JFollow> follower(Long playerId, JdbcPage jdbcPage);

	/**
	 * 被关注列表
	 * 
	 * @param playerContext
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<OFriender> follower(JPlayer player, JdbcPage jdbcPage) {
		List<JFollow> follows = ME.follower(player.getId(), jdbcPage);
		List<OFriender> frienders = new ArrayList<OFriender>(follows.size());
		for (JFollow follow : follows) {
			OFriender friender = new OFriender(follow);
			friender.setPlayer(PlayerService.ME.getPlayer(follow.getId().getEid()));
			frienders.add(friender);
		}

		return frienders;
	}

	/**
	 * 系统推荐
	 * 
	 * @param playerContext
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JPlayer> favorites(PlayerContext playerContext) {
		int level = playerContext.getPlayer().getLevel();
		return PlayerContext
				.onlinePlayers(QueryDaoUtils
						.createQueryArray(
								BeanDao.getSession(),
								"SELECT o FROM JPlayer o WHERE o.id != ? AND o.id NOT IN (SELECT o.id.mid FROM JFollow o WHERE o.id.eid = ? AND o.following = TRUE) AND o.level >= ? AND o.level <= ? ORDER BY RAND()",
								playerContext.getId(), playerContext.getId(), level - 5, level + 5).setMaxResults(20).iterate());
	}

	/**
	 * 搜索好友
	 * 
	 * @param name
	 * @param playerId
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery("SELECT o FROM JPlayer o WHERE o.id != ? AND o.id NOT IN (SELECT o.id.mid FROM JFollow o WHERE o.id.eid = ? AND o.following = TRUE) AND o.name like ?")
	protected abstract Iterator<JPlayer> search(Long id, Long playerId, String name, JdbcPage jdbcPage);

	/**
	 * 搜索好友
	 * 
	 * @param playerContext
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JPlayer> search(String name, PlayerContext playerContext, JdbcPage jdbcPage) {
		return PlayerContext.onlinePlayers(ME.search(playerContext.getId(), playerContext.getId(), '%' + name + '%', jdbcPage));
	}

	/**
	 * 相互关注列表
	 * 
	 * @param playerId
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery(value = "SELECT o FROM JFollow o WHERE o.id.eid = ? AND o.following = TRUE AND o.follower = TRUE @ ORDER BY o.messageTime DESC")
	public abstract List<JFollow> friends(Long playerId, JdbcPage jdbcPage);

	/**
	 * 相互关注列表
	 * 
	 * @param playerContext
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<OFriender> friends(JPlayer player, JdbcPage jdbcPage) {
		List<JFollow> follows = ME.friends(player.getId(), jdbcPage);
		List<OFriender> frienders = new ArrayList<OFriender>(follows.size());
		for (JFollow follow : follows) {
			OFriender friender = new OFriender(follow);
			friender.setPlayer(PlayerService.ME.getPlayer(follow.getId().getMid()));
			frienders.add(friender);
		}

		return frienders;
	}

	/**
	 * 获取消息ID
	 * 
	 * @param session
	 * @param playerId
	 * @param friendId
	 * @return
	 */
	private JEmbedLL getMessageId(Long playerId, Long friendId) {
		JEmbedLL embedLL = new JEmbedLL();
		if (playerId < friendId) {
			embedLL.setEid(playerId);
			embedLL.setMid(friendId);

		} else {
			embedLL.setEid(friendId);
			embedLL.setMid(playerId);
		}

		return embedLL;
	}

	/**
	 * 获取消息关注
	 * 
	 * @param session
	 * @param playerId
	 * @param friendId
	 * @return
	 */
	private JFollow getMessageFollow(Session session, JEmbedLL embedLL) {
		JFollow follow = BeanDao.get(session, JFollow.class, embedLL);
		if (follow == null || !follow.isFollowing() || !follow.isFollower()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		return follow;
	}

	/**
	 * 发送消息
	 * 
	 * @param player
	 * @param friendId
	 * @param message
	 * @return
	 */
	@Transaction
	public JFollowMessage send(JPlayer player, Long friendId, String message) {
		Session session = BeanDao.getSession();
		JEmbedLL embedLL = getMessageId(player.getId(), friendId);
		getMessageFollow(session, embedLL);
		JFollowMessage followMessage = new JFollowMessage();
		followMessage.setEmid(embedLL);
		followMessage.setPlayerId(player.getId());
		followMessage.setCreateTime(System.currentTimeMillis());
		followMessage.setMessage(message);
		session.persist(followMessage);
		// 更新消息发送时间和数量
		QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.messageNumber = o.messageNumber + 1 , o.messageTime = ? WHERE o.id.eid = ? AND o.id.mid = ? ", ContextUtils.getContextTime(),
				friendId, player.getId());
		// 消息通知
		PlayerContext playerContext = PlayerContext.find(friendId);
		if (playerContext != null) {
			JPlayerA playerA = playerContext.getPlayerA();
			synchronized (playerA) {
				playerA.setMessageNumber(playerA.getMessageNumber() + 1);
			}

			SocketService.writeByteObject(playerContext.getSocketChannel(), SocketService.CALLBACK_MESSAGE, player.getId().toString());
		}

		return followMessage;
	}

	/**
	 * 消息列表
	 * 
	 * @param eid
	 * @param mid
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery(value = "SELECT o FROM JFollowMessage o WHERE o.emid.eid = ? AND o.emid.mid = ? @ ORDER BY o.createTime DESC", cacheable = true)
	protected abstract List<JFollowMessage> messages(Long eid, Long mid, JdbcPage jdbcPage);

	/**
	 * 消息列表
	 * 
	 * @param player
	 * @param friendId
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JFollowMessage> messages(JPlayer player, long friendId, JdbcPage jdbcPage) {
		Session session = BeanDao.getSession();
		JEmbedLL embedLL = new JEmbedLL();
		embedLL.setEid(player.getId());
		embedLL.setMid(friendId);
		JFollow follow = getMessageFollow(session, embedLL);
		// 未读消息清零
		if (follow.getMessageNumber() > 0) {
			QueryDaoUtils.createQueryArray(session, "UPDATE JFollow o SET o.messageNumber = 0 WHERE o.id.eid = ? AND o.id.mid = ?", follow.getId().getEid(), follow.getId().getMid()).executeUpdate();
		}

		// 消息列表
		embedLL = getMessageId(player.getId(), friendId);
		return ME.messages(embedLL.getEid(), embedLL.getMid(), jdbcPage);
	}

	/**
	 * 平台分享
	 * 
	 * @param playerContext
	 * @param shareCode
	 */
	@Transaction
	public Object share(PlayerContext playerContext, String shareCode) {
		shareCode = HelperCommon.encrypt(shareCode);
		if (shareCode == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		String[] shareCodes = shareCode.split(",");
		if (shareCodes.length != 2 || KernelDyna.to(shareCodes[0], long.class) != playerContext.getId()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		JEmbedLS embedLS = new JEmbedLS();
		embedLS.setEid(playerContext.getId());
		embedLS.setMid(shareCodes[1]);
		Session session = BeanDao.getSession();
		JShared shared = BeanDao.get(session, JShared.class, embedLS);
		if (shared == null) {
			shared = new JShared();

		} else {
			// 已经分享
			return null;
		}

		shared.setCreateTime(ContextUtils.getContextTime());
		session.merge(shared);

		// 执行奖励
		ContextMap contextMap = new ContextMap(playerContext.getPlayer());
		playerContext.modifyDiamond(socialConfigure.getShareDiamondNumber());
		return contextMap.comparedMap();
	}
}
