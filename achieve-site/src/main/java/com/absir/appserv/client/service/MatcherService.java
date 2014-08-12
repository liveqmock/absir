/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-27 下午1:51:31
 */
package com.absir.appserv.client.service;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OFightPvp;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.base.ElementCompare;
import com.absir.core.kernel.KernelObject;
import com.absir.core.util.UtilPool;

/**
 * @author absir
 * 
 */
@Bean
public class MatcherService {

	// 匹配战斗最大时间
	private static final long MATCH_MAX_TIME = 60000;

	// 匹配失败通知
	private static final String MATCH_FAILED = "failed";

	// 进入战斗最大时间
	private static final long ATTEND_MAX_TIME = 10000;

	/**
	 * @author absir
	 * 
	 */
	private static class Matcher extends ElementCompare<Long, Matcher> {

		/** id */
		public Long id;

		/** socketChannel */
		private SocketChannel socketChannel;

		/** playerId */
		public Boolean attended;

		/** createTime */
		public long createTime = ContextUtils.getContextTime();

		/** rank */
		private int rank;

		/**
		 * @param playerContext
		 * @param socketChannel
		 */
		public Matcher(PlayerContext playerContext, SocketChannel socketChannel) {
			id = playerContext.getId();
			this.socketChannel = socketChannel;
			rank = playerContext.getPlayer().getLevel();
		}

		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}

			if (obj.getClass() == getClass()) {
				obj = ((Matcher) obj).id;
			}

			return KernelObject.equals(this, obj);
		}

		/**
		 * 
		 */
		public synchronized void attend() {
			if (attended == null) {
				attended = Boolean.FALSE;
			}
		}

		/**
		 * 
		 */
		public synchronized boolean clear() {
			if (attended == Boolean.FALSE) {
				attended = Boolean.TRUE;
				return true;
			}

			return false;
		}

		/**
		 * 
		 */
		public synchronized void close() {
			attended = Boolean.TRUE;
		}

		/**
		 * @return
		 */
		public int getRank() {
			return rank;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.core.base.ElementCompare#compareRank(com.absir.core.base
		 * .ElementCompare)
		 */
		@Override
		public int compareRank(Matcher o) {
			// TODO Auto-generated method stub
			return getRank() - o.getRank();
		}

		/**
		 * @param matcherTarget
		 * @return
		 */
		public boolean match(Matcher matcherTarget) {
			return true;
		}
	}

	/** categoryIdMapFightMatchers */
	private Map<Long, UtilPool<Long, Matcher>> categoryIdMapFightMatchers = new ConcurrentHashMap<Long, UtilPool<Long, Matcher>>();

	/**
	 * @author absir
	 * 
	 */
	private class Attender {

		/** categoryId */
		private Long categoryId;

		/** playerId */
		private Long playerId;

		/** matcher */
		private Matcher matcher;

		/** playerIdTarget */
		private Long playerIdTarget;

		/** matcherTarget */
		private Matcher matcherTarget;

		/** expirationTime */
		private long expirationTime = ContextUtils.getContextTime() + ATTEND_MAX_TIME;

	}

	/** playerIdMapAttender */
	private Map<Long, Attender> playerIdMapAttender = new ConcurrentHashMap<Long, MatcherService.Attender>();

	/**
	 * 报名匹配
	 * 
	 * @param categoryId
	 * @param playerContext
	 * @return
	 */
	public void sign(Long categoryId, PlayerContext playerContext) {
		SocketChannel socketChannel = playerContext.getSocketChannel();
		if (socketChannel != null) {
			UtilPool<Long, Matcher> fightMatchers = categoryIdMapFightMatchers.get(categoryId);
			if (fightMatchers == null) {
				synchronized (this) {
					fightMatchers = categoryIdMapFightMatchers.get(categoryId);
					if (fightMatchers == null) {
						fightMatchers = new UtilPool<Long, Matcher>(Collections.newSetFromMap(new ConcurrentSkipListMap<Matcher, Boolean>()));
						categoryIdMapFightMatchers.put(categoryId, fightMatchers);
					}
				}
			}

			Matcher matcher = fightMatchers.get(playerContext.getId());
			if (matcher != null) {
				matcher.close();
			}

			matcher = new Matcher(playerContext, socketChannel);
			fightMatchers.addForce(matcher.id, matcher);
		}
	}

	/**
	 * 退出匹配
	 * 
	 * @param categoryId
	 * @param playerContext
	 * @return
	 */
	public void exit(Long categoryId, PlayerContext playerContext) {
		UtilPool<Long, Matcher> fightMatchers = categoryIdMapFightMatchers.get(categoryId);
		if (fightMatchers != null) {
			fightMatchers.remove(playerContext.getId());
		}
	}

	/**
	 * 处理对战匹配
	 */
	@Schedule(fixedDelay = 3000)
	protected void stepMatching() {
		// TODO Auto-generated method stub
		// 处理对战匹配
		long contextTime = ContextUtils.getContextTime();
		for (Entry<Long, UtilPool<Long, Matcher>> entry : categoryIdMapFightMatchers.entrySet()) {
			Long categoryId = entry.getKey();
			Iterator<Matcher> iterator = entry.getValue().iterator();
			if (iterator.hasNext()) {
				Matcher matcher = null;
				Matcher matcherNext = iterator.next();
				while (true) {
					Boolean attented = matcherNext.attended;
					if (attented == null) {
						// 开始匹配
						if (contextTime - matcherNext.createTime > MATCH_MAX_TIME) {
							// 超过最大匹配时间
							matcherNext.close();
							attented = Boolean.TRUE;
							iterator.remove();
							// 通知匹配失败
							SocketService.writeByteObject(matcherNext.socketChannel, SocketService.CALLBACK_FIGHT, MATCH_FAILED);

						} else {
							if (matcher != null && matcher.attended == null && matcher.match(matcherNext)) {
								// 可以匹配
								attented = Boolean.FALSE;
								matcher.attend();
								matcherNext.attend();
								// 发送匹配消息
								Attender attender = new Attender();
								attender.categoryId = categoryId;
								attender.playerId = matcher.id;
								attender.matcher = matcher;
								attender.playerIdTarget = matcherNext.id;
								attender.matcherTarget = matcherNext;
								playerIdMapAttender.put(matcher.id, attender);
								if (SocketService.writeByteObject(matcherNext.socketChannel, SocketService.CALLBACK_FIGHT, matcher.id, true)) {
									if (!SocketService.writeByteObject(matcher.socketChannel, SocketService.CALLBACK_FIGHT, matcher.id, true)) {
										playerIdMapAttender.remove(matcher.id);
										matcher.close();
										if (matcherNext.clear()) {
											attented = null;
										}
									}

								} else {
									playerIdMapAttender.remove(matcher.id);
									matcher.clear();
									matcherNext.close();
									iterator.remove();
								}
							}
						}

					} else if (attented == Boolean.TRUE) {
						iterator.remove();
					}

					if (iterator.hasNext()) {
						if (attented == null) {
							// 可以下次匹配
							matcher = matcherNext;
						}

						// 获取下次
						matcherNext = iterator.next();

					} else {
						break;
					}
				}
			}
		}

		// 处理进入超时
		Iterator<Entry<Long, Attender>> attenderIterator = playerIdMapAttender.entrySet().iterator();
		while (attenderIterator.hasNext()) {
			Entry<Long, Attender> entry = attenderIterator.next();
			Attender attender = entry.getValue();
			if (attender.expirationTime < contextTime) {
				// 可以重新匹配
				attenderIterator.remove();
				attender.matcher.clear();
				attender.matcherTarget.clear();
			}
		}
	}

	/**
	 * 进入匹配
	 * 
	 * @param attendId
	 * @param playerContext
	 */
	public synchronized void attend(Long attendId, PlayerContext playerContext) {
		Attender attender = playerIdMapAttender.get(attendId);
		if (attender != null) {
			PlayerContext playerContextTarget = null;
			if (attender.playerId != null && attender.playerId.equals(playerContext.getId())) {
				attender.playerId = null;
				if (attender.playerIdTarget != null) {
					return;
				}

				playerContextTarget = PlayerContext.find(attender.matcherTarget.id);

			} else if (attender.playerIdTarget != null && attender.playerIdTarget.equals(playerContext.getId())) {
				attender.playerIdTarget = null;
				if (attender.playerId != null) {
					return;
				}

				playerContextTarget = playerContext;
				playerContext = PlayerContext.find(attender.matcher.id);
			}

			// 准备进入战斗了
			playerIdMapAttender.remove(attendId);
			Matcher matcher = attender.matcher;
			Matcher matcherTarget = attender.matcherTarget;
			if (playerContext == null || matcher.socketChannel != playerContext.getSocketChannel()) {
				matcherTarget.clear();
			}

			if (playerContextTarget == null || matcherTarget.socketChannel != playerContextTarget.getSocketChannel()) {
				matcher.clear();
			}

			if (matcher.attended == Boolean.FALSE && matcherTarget.attended == Boolean.FALSE) {
				// 检查玩家掉线
				matcher.close();
				matcherTarget.close();
				OFightPvp fightPvp = new OFightPvp(attender.categoryId, playerContext, playerContextTarget);
				SocketService.writeByteObject(attender.matcher.socketChannel, SocketService.CALLBACK_FIGHT, fightPvp);
				SocketService.writeByteObject(attender.matcherTarget.socketChannel, SocketService.CALLBACK_FIGHT, fightPvp.getFigthPvpTarget());
			}
		}
	}
}
