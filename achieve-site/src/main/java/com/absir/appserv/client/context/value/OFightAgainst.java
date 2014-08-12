/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-27 上午9:48:18
 */
package com.absir.appserv.client.context.value;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.SocketService;
import com.absir.appserv.client.service.SocketService.IFightConnect;
import com.absir.appserv.game.utils.GameUtils;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.OCard;
import com.absir.appserv.game.value.OReport;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class OFightAgainst extends OFightBase implements IFightConnect {

	/** playerContextTarget */
	private transient PlayerContext playerContextTarget;

	/**
	 * @param playerContext
	 * @param playerContextTarget
	 */
	public OFightAgainst(PlayerContext playerContext, PlayerContext playerContextTarget) {
		this(playerContext, playerContextTarget, 0);
	}

	/**
	 * @param playerContext
	 * @param playerContextTarget
	 * @param cardLength
	 */
	public OFightAgainst(PlayerContext playerContext, PlayerContext playerContextTarget, int cardLength) {
		super(playerContext);
		this.playerContextTarget = playerContextTarget;
		// 玩家卡牌
		List<JCard> jCards = playerContext.getPlayer().getCards();
		int size = jCards.size();
		cards = new OCardPlayer[size];
		for (int i = 0; i < size; i++) {
			cards[i] = new OCardAgainst(this, i + cardLength, jCards.get(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightBase#getCampQuestions()
	 */
	@JsonIgnore
	public OCampCategory[] getCampQuestions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightBase#getQuestions()
	 */
	@Override
	protected List<JQuestion> getQuestions() {
		return null;
	}

	/**
	 * @return
	 */
	public final JQuestion getQuestion() {
		return question;
	}

	/**
	 * @return
	 */
	public JQuestion getJQuestion() {
		return question;
	}

	/**
	 * @return
	 */
	public int getJQuestionCount() {
		return 0;
	}

	/**
	 * 回答问题
	 * 
	 * @param answer
	 */
	public OReport answer(int answer) {
		return null;
	}

	/**
	 * @param answer
	 * @param questionCount
	 * @return
	 */
	public synchronized OReport answer(int answer, int questionCount) {
		// 当前题目
		JQuestion question = getJQuestion();
		if (question == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		synchronized (question) {
			if (getJQuestionCount() != questionCount) {
				return null;
			}

			long time = System.currentTimeMillis();
			answer = answer(question, answer, time);
			reportResult.began(time);
			if (answer >= 0) {
				if (answer == reportResult.correct) {
					for (OCard card : getCards()) {
						card.step(time, reportResult);
					}

				} else {
					for (OCard card : getTargetCards()) {
						card.step(time, reportResult);
					}

					GameUtils.revert(reportResult);
				}
			}

			// 处理战报
			clearQuestion();
			reportResult.setResultData(getJQuestion());
			return notifyReportResult(dealReportResult(reportResult));
		}
	}

	/**
	 * 释放SS技能
	 * 
	 * @param cardId
	 * @return
	 */
	public synchronized OReport ss(int cardId) {
		return null;
	}

	/**
	 * 购买答案
	 * 
	 * @return
	 */
	public synchronized OReport correct() {
		return null;
	}

	/**
	 * @param report
	 * @return
	 */
	protected OReport notifyReportResult(OReportBase reportResult) {
		GameUtils.revert(reportResult);
		if (reportResult.answer >= 0) {
			reportResult.answer -= 5;
		}

		try {
			if (SocketService.writeByteObject(playerContextTarget.getSocketChannel(), SocketService.CALLBACK_AGAINST, reportResult)) {
				if (reportResult.answer < -1) {
					reportResult.answer += 5;
				}

				GameUtils.revert(reportResult);
				return reportResult;
			}

		} catch (Exception e) {
		}

		reportResult.began(System.currentTimeMillis());
		reportResult.setResult(EResult.VICTORY);
		reportResult.setResultData(disconnectResultData());
		playerContext.setFightBase(null);
		return reportResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.service.SocketService.SocketDisconnect#disconnect
	 * ()
	 */
	@Override
	public void disconnect() {
		SocketService.writeByteObject(playerContextTarget.getSocketChannel(), SocketService.CALLBACK_FIGHT, disconnectResultData());
	}

	/**
	 * @return
	 */
	protected Object disconnectResultData() {
		return "disconnect";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OFight#dealReportResult(com.absir.appserv
	 * .game.value.OReportResult)
	 */
	@Override
	public OReportBase dealReportResult(OReportBase reportResult) {
		// TODO Auto-generated method stub
		if (reportResult.getResult() == EResult.CONTINUE) {
			reportResult.setResultData(getJQuestion());

		} else if (reportResult.getResult() != EResult.DONE) {
			playerContext.setFightBase(null);
		}

		return reportResult;
	}
}
