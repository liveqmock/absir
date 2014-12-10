/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月25日 下午1:06:14
 */
package com.absir.appserv.game.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.data.value.MaxResults;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.bean.value.IArenaDefine;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.InjectOrder;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.socket.ServerContext;
import com.absir.server.socket.SocketServerContext;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public abstract class ArenaService {

	/** ME */
	public static final ArenaService ME = BeanFactoryUtils.get(ArenaService.class);

	/** idMapArenaBase */
	private Map<Long, ArenaBase> idMapArenaBase = new ConcurrentHashMap<Long, ArenaService.ArenaBase>();

	/**
	 * 最后一名
	 * 
	 * @param serverId
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT MAX(o.arena) FROM JPlayer o WHERE o.serverId = ?")
	public abstract int maxArena(Long serverId);

	/**
	 * 查询排名
	 * 
	 * @param serverId
	 * @param minArena
	 * @param maxArena
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o.id, o.arena FROM JPlayer o WHERE o.serverId = ? AND o.arena >= ?", aliasType = List.class)
	public abstract List<List<Object>> listArenas(Long serverId, Integer minArena, @MaxResults Integer maxArena);

	/**
	 * 指定排名列表
	 * 
	 * @param serverId
	 * @param arenas
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o.id, o.arena FROM JPlayer o WHERE o.serverId = ? AND o.arena in (:arenas)", aliasType = List.class)
	public abstract List<List<Object>> findIdArenas(Long serverId, Collection<Integer> arenas);

	/**
	 * 查询排名
	 * 
	 * @param serverId
	 * @param minArena
	 * @param maxArena
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT o.id, o.arena FROM JPlayer o WHERE o.serverId = ? AND o.arena >= ? AND o.arena < ?", aliasType = List.class)
	public abstract List<List<Object>> findIdArenas(Long serverId, Integer minArena, Integer maxArena);

	/**
	 * 竞技场列表
	 * 
	 * @param arena
	 * @param arenaCount
	 * @param serverId
	 * @return
	 */
	public List<JbPlayer> getArenaList(int arena, int arenaCount, Long serverId) {
		ArenaBase arenaBase = getArenaBase(serverId);
		return arenaBase.analyzes(PlayerServiceBase.ME.findPlayers(arenaBase.list(arena, arenaCount)));
	}

	/**
	 * 
	 * 创建竞技对象
	 * 
	 * @return
	 */
	protected ArenaBase createArenaBase() {
		return new ArenaBase();
	}

	/**
	 * 获取竞技对象
	 * 
	 * @param serverId
	 * @return
	 */
	public ArenaBase getArenaBase(Long serverId) {
		ArenaBase arenaBase = idMapArenaBase.get(serverId);
		if (arenaBase == null) {
			synchronized (idMapArenaBase) {
				arenaBase = idMapArenaBase.get(serverId);
				if (arenaBase == null) {
					arenaBase = createArenaBase();
					arenaBase.serverId = serverId;
					int maxArena = ME.maxArena(serverId);
					arenaBase.maxArena = maxArena < 1 ? 1 : (maxArena + 1);
				}

				idMapArenaBase.put(serverId, arenaBase);
			}
		}

		return arenaBase;
	}

	/**
	 * 保存排名
	 */
	@Transaction
	@InjectOrder(value = -1)
	@Schedule(fixedDelay = 6 * 3600000)
	@Stopping
	public void saveArenas() {
		for (ArenaBase arenaBase : idMapArenaBase.values()) {
			arenaBase.save();
		}
	}

	/**
	 * 竞技场发奖
	 */
	@Transaction
	@InjectOrder(value = -1)
	@Schedule(cron = "0 24 * * * *")
	@Stopping
	public void rewardArenas() {
		XlsDao<IArenaDefine, Serializable> arenaDefineDao = JbPlayerContext.COMPONENT.getArenaDefineDao();
		if (arenaDefineDao != null) {
			ArenaBase arenaBase;
			int minArena = 1;
			int maxArena;
			for (ServerContext serverContext : SocketServerContext.ME.getServerContexts()) {
				if (PlayerServiceBase.ME.isSupport(serverContext)) {
					arenaBase = getArenaBase(serverContext.getServer().getId());
					synchronized (arenaBase) {
						arenaBase.save();
						for (IArenaDefine arenaDefine : arenaDefineDao.getAll()) {
							maxArena = arenaDefine.getArena() + 1;
							if (maxArena <= minArena) {
								maxArena = Integer.MAX_VALUE;
							}

							for (List<Object> arenas : findIdArenas(arenaBase.serverId, minArena, maxArena)) {
								doArenaReward((Long) arenas.get(0), (Integer) arenas.get(1), arenaDefine);
							}

							if (maxArena == Integer.MAX_VALUE) {
								break;
							}

							minArena = maxArena;
						}
					}
				}
			}
		}
	}

	/**
	 * 执行竞技场发奖
	 * 
	 * @param playerId
	 * @param reward
	 * @param arenaDefine
	 */
	protected void doArenaReward(Long playerId, int arena, IArenaDefine arenaDefine) {
		PlayerServiceBase.ME.addPlayerReward(playerId, arenaDefine.getReward(), "arena", arena, null);
	}

	/**
	 * 竞技场对象
	 * 
	 * @author absir
	 *
	 */
	public static class ArenaBase {

		/** serverId */
		protected Long serverId;

		/** maxArena */
		protected int maxArena;

		/** arenaIds */
		protected Map<Integer, Long> arenaIds = new HashMap<Integer, Long>();

		/** idArenas */
		protected Map<Long, Integer> idArenas = new HashMap<Long, Integer>();

		/**
		 * 载入查询
		 * 
		 * @param idArenas
		 */
		protected void load(List<List<Object>> arenas) {
			for (List<Object> ids : arenas) {
				Integer arn = (Integer) ids.get(1);
				if (!arenaIds.containsKey(arn)) {
					Long id = (Long) ids.get(0);
					arenaIds.put(arn, id);
				}
			}
		}

		/**
		 * 指定排名
		 * 
		 * @param arena
		 * @param arenaCount
		 * @return
		 */
		public List<Long> arenas(int... arenas) {
			Map<Integer, Integer> arenaMap = null;
			List<Long> ids = new ArrayList<Long>();
			Long id;
			int index = 0;
			for (int arena : arenas) {
				id = arenaIds.get(arena);
				if (id == null) {
					if (arenaMap == null) {
						arenaMap = new HashMap<Integer, Integer>();
						arenaMap.put(arena, index);
					}
				}

				index++;
				ids.add(id);
			}

			if (arenaMap != null) {
				List<List<Object>> idArns = ArenaService.ME.findIdArenas(serverId, arenaMap.keySet());
				Map<Integer, Long> arnIds;
				synchronized (this) {
					arnIds = arenaIds;
					load(idArns);
				}

				for (Entry<Integer, Integer> entry : arenaMap.entrySet()) {
					ids.set(entry.getValue(), arnIds.get(entry.getKey()));
				}
			}

			return ids;
		}

		/**
		 * 排名列表
		 * 
		 * @param arena
		 * @param arenaCount
		 * @return
		 */
		public List<Long> list(int arena, int arenaCount) {
			Map<Integer, Long> arnIds = arenaIds;
			List<Long> finds = new ArrayList<Long>();
			boolean loaded = false;
			for (; arena < maxArena; arena++) {
				Long id = arnIds.get(arena);
				if (id == null) {
					if (!loaded) {
						loaded = true;
						List<List<Object>> idArns = ArenaService.ME.listArenas(serverId, arena, arenaCount);
						synchronized (this) {
							load(idArns);
							arnIds = arenaIds;
						}

						id = arnIds.get(arena);
					}
				}

				if (id != null) {
					finds.add(id);
					arenaCount--;
				}

				arena++;
			}

			return finds;
		}

		/**
		 * 实时排名
		 * 
		 * @param player
		 */
		public void analyze(JbPlayer player) {
			if (player == null) {
				return;
			}

			Long id = player.getId();
			Integer arena = idArenas.get(id);
			if (arena == null) {
				int arn = player.getArena();
				if (arn <= 0) {
					synchronized (this) {
						arn = maxArena++;
					}

					setArena(id, arn);
					player.setArena(arn);
				}

			} else {
				player.setArena(arena);
			}
		}

		/**
		 * 实时排名
		 * 
		 * @param players
		 * @return
		 */
		public List<JbPlayer> analyzes(List<JbPlayer> players) {
			for (JbPlayer player : players) {
				analyze(player);
			}

			return players;
		}

		/**
		 * 设置排名
		 * 
		 * @param id
		 * @param arena
		 */
		protected void setArena(Long id, Integer arena) {
			Integer arn = idArenas.put(id, arena);
			if (arn != null) {
				if (arn.equals(arena)) {
					return;
				}

				Long oid = arenaIds.get(arn);
				if (oid != null && !oid.equals(id)) {
					arenaIds.remove(arn);
				}
			}

			arenaIds.put(arena, id);
		}

		/**
		 * 交换排名
		 * 
		 * @param player
		 * @param target
		 */
		public synchronized void exchange(JbPlayer player, JbPlayer target) {
			analyze(player);
			analyze(target);
			int arena = player.getArena();
			int targetArena = target.getArena();
			if (arena < targetArena) {
				Long id = player.getId();
				setArena(id, targetArena);
				boolean loaded = false;
				for (arena++; arena < targetArena; arena++) {
					id = arenaIds.get(arena);
					if (id == null) {
						if (loaded) {
							continue;
						}

						loaded = true;
						load(ArenaService.ME.findIdArenas(id, arena, targetArena));
						id = arenaIds.get(arena);
						if (id == null) {
							continue;
						}
					}

					setArena(id, arena - 1);
				}
			}
		}

		/**
		 * 保存排名数据
		 */
		public void save() {
			Map<Long, Integer> idArns = idArenas;
			synchronized (this) {
				arenaIds = new HashMap<Integer, Long>(arenaIds);
				idArenas = new HashMap<Long, Integer>(idArenas);
			}

			for (final Entry<Long, Integer> entry : idArns.entrySet()) {
				PlayerServiceBase.ME.modifyPlayer(entry.getKey(), new CallbackTemplate<JbPlayer>() {

					@Override
					public void doWith(JbPlayer template) {
						// TODO Auto-generated method stub
						template.setArena(entry.getValue());
					}
				});
			}

			synchronized (this) {
				Iterator<Entry<Long, Integer>> iterator = idArenas.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Long, Integer> entry = iterator.next();
					Integer arn = idArns.get(entry.getKey());
					if (arn != null && arn.equals(entry.getValue())) {
						iterator.remove();
					}
				}
			}
		}
	}

}
