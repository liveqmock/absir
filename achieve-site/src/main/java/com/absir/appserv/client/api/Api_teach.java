/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 下午12:19:53
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JPlayerA;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskDetail;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskPass;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OFightTask;
import com.absir.appserv.client.context.value.OFightTeach;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_teach extends PlayerServer {

	@JaLang("引导进度")
	public int route(int teach, @Attribute PlayerContext playerContext) {
		JPlayerA playerA = playerContext.getPlayerA();
		// if (playerA.getTeach() < teach) {
		playerA.setTeach(teach);
		// }

		return teach;
	}

	/** TEACH */
	public static final int TEACH = 1;

	@JaLang("任务引导")
	public OFightTask task(@Attribute PlayerContext playerContext) {
		if (playerContext.getPlayerA().getTeach() >= Api_teach.TEACH) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		int scene = 0;
		int pass = 0;
		int detail = 0;
		XTaskDefine taskDefine = PlayerContext.TASK_DEFINE_XLS_DAO.get(scene);
		TaskPass taskPass = taskDefine.getTaskPasses()[pass];
		TaskDetail taskDetail = taskPass.getTaskDetails()[detail];
		return new OFightTeach(playerContext, PlayerContext.getTaskId(scene, pass), scene, pass, detail, taskDefine, taskPass, taskDetail);
	}

	/** LOT_TEACH */
	private static final int LOT_TEACH = 2;

	@JaLang("抽卡引导")
	public JCard lot(@Attribute PlayerContext playerContext) {
		synchronized (playerContext.getPlayerA()) {
			if (playerContext.getPlayerA().getTeach() >= LOT_TEACH) {
				throw new ServerException(ServerStatus.ON_DENIED);
			}

			JCard card = playerContext.gainCard(playerContext.getPlayer().getCard0().getCardDefine().getEvolutionRequires()[0], 1);
			playerContext.getPlayerA().setTeach(LOT_TEACH);
			return card;
		}
	}
}
