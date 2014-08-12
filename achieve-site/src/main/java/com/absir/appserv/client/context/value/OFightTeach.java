/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-15 下午5:10:04
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.api.Api_teach;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskDetail;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskPass;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.OReport;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
public class OFightTeach extends OFightTask {

	/** teachNumber */
	private int teachNumber = 1;

	/**
	 * @param playerContext
	 * @param taskId
	 * @param scene
	 * @param pass
	 * @param detail
	 * @param taskDefine
	 * @param taskPass
	 * @param taskDetail
	 */
	public OFightTeach(PlayerContext playerContext, String taskId, int scene, int pass, int detail, XTaskDefine taskDefine, TaskPass taskPass, TaskDetail taskDetail) {
		super(playerContext, taskId, scene, pass, detail, taskDefine, taskPass, taskDetail);
		// TODO Auto-generated constructor stub
		// XRewardDefine rewardDefine =
		// PlayerContext.REWARD_DEFINE_XLS_DAO.get("teach-task");
		lootMoney += 100000;
		XCardDefine cardDefine = playerContext.getPlayer().getCard0().getCardDefine();
		lootCards.add(FightServiceUtils.generate(cardDefine.getEvolutionRequires()[0], getPlayerContext()));
		cardDefine = PlayerContext.CARD_DEFINE_XLS_DAO.get(cardDefine.getCamp() == JeCamp.FIRE ? 133 : cardDefine.getCamp() == JeCamp.WATER ? 137 : 141);
		for (int i = 0; i < 10; i++) {
			lootCards.add(FightServiceUtils.generate(cardDefine, getPlayerContext()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightBase#getQuestions()
	 */
	@Override
	public OCampCategory[] getCampQuestions() {
		OCampCategory[] campCategoriesOld = this.campCategories;
		OCampCategory[] campCategories = super.getCampQuestions();
		if (teachNumber > 0 && campCategories != campCategoriesOld) {
			campCategories[0].camps = new JeCamp[] { playerContext.getPlayer().getCard0().getCardDefine().getCamp() };
		}

		return campCategories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightBase#getQuestion(int)
	 */
	@Override
	public synchronized JQuestion getQuestion(int category) {
		JQuestion questionOld = this.question;
		JQuestion question = super.getQuestion(category);
		if (teachNumber > 0 && question != questionOld && question.getCorrect() != 0) {
			String choice = question.getChoice(question.getCorrect());
			question.setChoice(question.getChoice(0), question.getCorrect());
			question.setChoice(choice, 0);
			question.setCorrect(0);
		}

		return question;
	}

	/**
	 * 回答问题
	 * 
	 * @param answer
	 */
	public synchronized OReport answer(int answer) {
		if (teachNumber > 0) {
			teachNumber--;
			answerTime = System.currentTimeMillis() - 1;
		}

		return super.answer(answer);
	}

	private boolean teachCorrect;

	/**
	 * 购买答案
	 * 
	 * @return
	 */
	public synchronized OReport correct() {
		if (question == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		if (teachCorrect) {
			return super.correct();
		}

		teachCorrect = true;
		return answer(question.getCorrect());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.OFightBase#dealReport(com.absir
	 * .appserv.client.context.value.OReportBase)
	 */
	@Override
	public OReportBase dealReportResult(OReportBase report) {
		OReportBase reportBase = super.dealReportResult(report);
		if (reportBase.getResult() == EResult.VICTORY) {
			playerContext.getPlayerA().setTeach(Api_teach.TEACH);
			// playerContext.getPlayerA().getMetaRecards().put("teach-task", 1);
		}

		return reportBase;
	}
}
