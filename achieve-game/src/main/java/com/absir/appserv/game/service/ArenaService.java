/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月25日 下午1:06:14
 */
package com.absir.appserv.game.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.data.value.MaxResults;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.InjectOrder;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 *
 */
@Base
@Bean
public abstract class ArenaService {

	/** ME */
	public static final ArenaService ME = BeanFactoryUtils.get(ArenaService.class);

	/** idMapArenaBase */
	private Map<Long, ArenaBase> idMapArenaBase = new HashMap<Long, ArenaService.ArenaBase>();

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
	 * 
	 * @return
	 */
	protected ArenaBase createArenaBase() {
		return new ArenaBase();
	}

	/**
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
	@Schedule(fixedDelay = 3600000)
	@Stopping
	public void saveArenas() {
		Collection<ArenaBase> arenaBases;
		synchronized (idMapArenaBase) {
			arenaBases = new ArrayList<ArenaBase>(idMapArenaBase.values());
		}

		for (ArenaBase arenaBase : arenaBases) {
			arenaBase.save();
		}
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
					if (arn > maxArena) {
						maxArena = arn;
					}

					arenaIds.put(arn, id);
				}
			}
		}

		/**
		 * 排名列表
		 * 
		 * @param arena
		 * @param arenaCount
		 * @return
		 */
		public List<Long> list(int arena, int arenaCount) {
			int max = maxArena;
			Map<Integer, Long> arnIds = arenaIds;
			List<Long> finds = new ArrayList<Long>();
			boolean loaded = false;
			for (; arena < max; arena++) {
				Long id = arnIds.get(arena);
				if (id == null) {
					if (!loaded) {
						loaded = true;
						List<List<Object>> arenas = ArenaService.ME.listArenas(serverId, arena, arenaCount);
						synchronized (this) {
							load(arenas);
							max = maxArena;
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
			Integer arena = idArenas.get(player.getId());
			if (arena != null) {
				player.setArena(arena);
			}
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
			player.setArena(targetArena);

			boolean loaded = false;
			for (arena++; arena < targetArena; arena++) {
				Long id = arenaIds.get(arena);
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

				Integer old = idArenas.put(id, arena);
				if (old != null) {
					arenaIds.remove(old);
				}

				if (arena > maxArena) {
					maxArena = arena;
				}

				arenaIds.put(arena, id);
			}
		}

		/**
		 * 保存排名数据
		 */
		public void save() {
			Map<Long, Integer> idArns = idArenas;
			synchronized (this) {
				maxArena = 0;
				arenaIds = new HashMap<Integer, Long>();
				idArenas = new HashMap<Long, Integer>();
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
		}
	}

}
