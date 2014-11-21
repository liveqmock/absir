/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月21日 下午2:32:18
 */
package com.absir.appserv.game.service;

import java.util.List;

import org.hibernate.Session;

import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.game.bean.JbFriend;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Base
@Bean
public abstract class FriendService {

	/** 对象单例访问 */
	public static final FriendService ME = BeanFactoryUtils.get(FriendService.class);

	/**
	 * 获取好友关系
	 * 
	 * @param player
	 * @param target
	 * @param force
	 * @return
	 */
	@Transaction(readOnly = true)
	public JbFriend getFriend(JbPlayer player, JbPlayer target, boolean force) {
		Long id = player.getId();
		Long targetId = target.getId();
		Long targetable = null;
		if (id > targetId) {
			targetable = id;
			id = targetId;
			targetId = targetable;
		}

		JEmbedLL embedLL = new JEmbedLL(id, targetId);
		Session session = BeanDao.getSession();
		JbFriend friend = BeanDao.get(session, JbPlayerContext.COMPONENT.FRIEND_CLASS, embedLL);
		if (friend == null && force) {
			friend = JbPlayerContext.COMPONENT.createFriend();
			friend.setId(embedLL);
			friend.setPlayer(targetable == null ? player : target);
			friend.setTarget(targetable == null ? target : player);
			friend.setAccord(targetable == null ? -1 : -2);
		}

		return friend;
	}

	// 好友标示
	public static final int ACCORD_SUCCESS = 9;

	// 删除标示
	public static final int ACCORD_DELETED = -9;

	/**
	 * 是否是好友
	 * 
	 * @param player
	 * @param target
	 * @return
	 */
	public boolean isFriend(JbPlayer player, JbPlayer target) {
		JbFriend friend = getFriend(player, target, false);
		return friend != null && friend.getAccord() == ACCORD_SUCCESS;
	}

	/**
	 * 添加好友
	 * 
	 * @param playerContext
	 * @param target
	 */
	@Transaction
	public int addFriend(final JbPlayerContext playerContext, JbPlayer target) {
		JbFriend friend = getFriend(playerContext.getPlayer(), target, true);
		int accord = friend.getAccord();
		if (accord != ACCORD_SUCCESS) {
			if (accord < 0) {
				friend.setAccord(-accord);
				BeanDao.getSession().merge(friend);

			} else {
				JEmbedLL embedLL = friend.getId();
				if ((accord == 2 && embedLL.getEid().equals(target.getId())) || (accord == 1 && embedLL.getMid().equals(target.getId()))) {
					final JbFriend finalFriend = friend;
					PlayerServiceBase.ME.modifyPlayer(target.getId(), new CallbackTemplate<JbPlayer>() {

						@Override
						public void doWith(JbPlayer template) {
							// TODO Auto-generated method stub
							int friendNumber = template.getFriendNumber();
							if (friendNumber < template.getMaxFriendNumber()) {
								if (playerContext.modifyFriendNumber(1)) {
									template.setFriendNumber(friendNumber + 1);
									finalFriend.setAccord(ACCORD_SUCCESS);

								} else {
									finalFriend.setAccord(-1);
								}

							} else {
								finalFriend.setAccord(-2);
								BeanDao.getSession().delete(finalFriend);
							}
						}
					});
				}

				if (friend.getAccord() > 0) {
					BeanDao.getSession().merge(friend);
				}
			}
		}

		return friend.getAccord();
	}

	/**
	 * 删除好友
	 * 
	 * @param playerContext
	 * @param target
	 */
	@Transaction
	public int deleteFriend(JbPlayerContext playerContext, JbPlayer target) {
		JbFriend friend = getFriend(playerContext.getPlayer(), target, false);
		if (friend != null) {
			if (friend.getAccord() == ACCORD_SUCCESS && couldeDeleteFriend(friend)) {
				BeanDao.getSession().delete(friend);
				friend.setAccord(ACCORD_DELETED);
				playerContext.modifyFriendNumber(-1);
				PlayerServiceBase.ME.modifyPlayer(target.getId(), new CallbackTemplate<JbPlayer>() {

					@Override
					public void doWith(JbPlayer template) {
						// TODO Auto-generated method stub
						template.setFriendNumber(template.getFriendNumber() - 1);
					}
				});
			}
		}

		return friend == null ? 0 : friend.getAccord();
	}

	/**
	 * 删除好友条件
	 * 
	 * @param friend
	 * @return
	 */
	protected boolean couldeDeleteFriend(JbFriend friend) {
		return true;
	}

	/**
	 * 获取申请好友列表
	 * 
	 * @param playerId
	 * @param jdbcPage
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o FROM JFriend o WHERE (o.id.eid = :0 OR o.id.mid = :0) AND o.accord != " + ACCORD_SUCCESS + " @ ORDER BY o.encouraging DESC, o.encouragingTime DESC")
	public abstract List<JbFriend> getFriendings(Long playerId, JdbcPage jdbcPage);

	/**
	 * 获取已经好友列表
	 * 
	 * @param playerId
	 * @param jdbcPage
	 * @return
	 */
	@DataQuery(value = "SELECT o FROM JFriend o WHERE (o.id.eid = :0 OR o.id.mid = :0) AND o.accord = " + ACCORD_SUCCESS + " @ ORDER BY o.encouraging DESC, o.encouragingTime DESC")
	public abstract List<JbFriend> getFriendeds(Long playerId, JdbcPage jdbcPage);

	/**
	 * 
	 * @param playerId
	 * @param pageIndex
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JbPlayer> getFriendList(Long playerId, JdbcPage jdbcPage) {
		List<JbFriend> friends = FriendService.ME.getFriendeds(playerId, jdbcPage);
		return getPlayers(playerId, friends);
	}

	/**
	 * @param playerId
	 * @param friends
	 * @return
	 */
	public List<JbPlayer> getPlayers(Long playerId, List<JbFriend> friends) {
		List<JbPlayer> players = (List<JbPlayer>) (Object) friends;
		JbFriend friend;
		int size = friends.size();
		for (int i = 0; i < size; i++) {
			friend = friends.get(i);
			players.set(i, JbPlayerContext.COMPONENT.onlinePlayers(friend.getId().getEid().equals(playerId) ? friend.getTarget() : friend.getPlayer()));
		}

		return players;
	}
}
