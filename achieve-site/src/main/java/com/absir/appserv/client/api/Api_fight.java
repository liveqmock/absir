/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午4:02:47
 */
package com.absir.appserv.client.api;

import java.nio.channels.SocketChannel;

import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OCampCategory;
import com.absir.appserv.client.context.value.OFightAgainst;
import com.absir.appserv.client.context.value.OFightBase;
import com.absir.appserv.client.context.value.OFightTask;
import com.absir.appserv.game.value.OReport;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_fight extends PlayerServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.api.PlayerServer#onAuthentication(com.absir.
	 * server.in.Input)
	 */
	@Override
	protected SecurityContext onAuthentication(Input input) throws Throwable {
		SecurityContext securityContext = super.onAuthentication(input);
		OFightBase fightBase = ((PlayerContext) input.getAttribute("playerContext")).getFightBase();
		if (fightBase == null) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		input.setAttribute("fightBase", fightBase);
		return securityContext;
	}

	@JaLang("获取分类")
	public OCampCategory[] category(@Attribute OFightBase fightBase) {
		return fightBase.getCampQuestions();
	}

	@JaLang("选择题目")
	public JQuestion question(int category, @Attribute OFightBase fightBase) {
		return fightBase.getQuestion(category);
	}

	@JaLang("提交选择")
	public OReport answer(int answer, @Attribute OFightBase fightBase) {
		return fightBase.answer(answer);
	}

	@JaLang("释放技能")
	public OReport ss(int cardId, @Attribute OFightBase fightBase) {
		return fightBase.ss(cardId);
	}

	@JaLang("购买答案")
	public OReport correct(@Attribute OFightBase fightBase) {
		return fightBase.correct();
	}

	@JaLang("设置目标")
	public void target(int cardId, int targetId, @Attribute OFightBase fightBase) {
		fightBase.setTarget(cardId, targetId);
	}

	@JaLang("开宝箱")
	public Object lot(@Attribute OFightBase fightBase) {
		if (!(fightBase instanceof OFightTask)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		return ((OFightTask) fightBase).openLotBox();
	}

	@JaLang("在线比赛")
	public OReport pvpAnswer(SocketChannel socketChannel, @Attribute OFightAgainst fightBase, int answer, int questionCount) {
		return fightBase.answer(answer, questionCount);
	}
}
