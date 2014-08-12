/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-27 上午10:48:43
 */
package com.absir.appserv.client.context.value;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.QuestionService;

/**
 * @author absir
 * 
 */
public class OFightPvp extends OFightAgainst {

	/** categoryId */
	private long categoryId;

	/** questionCount */
	private int questionCount;

	/** figthPvpTarget */
	@JsonIgnore
	private OFigthPvpTarget figthPvpTarget;

	/**
	 * @param categoryId
	 * @param playerContext
	 * @param playerContextTarget
	 */
	public OFightPvp(Long categoryId, PlayerContext playerContext, PlayerContext playerContextTarget) {
		super(playerContext, playerContextTarget);
		// TODO Auto-generated constructor stub
		this.categoryId = categoryId;
		level = (playerContext.getPlayer().getLevel() + playerContextTarget.getPlayer().getLevel()) / 2;
		figthPvpTarget = new OFigthPvpTarget(playerContextTarget, playerContext);
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return categoryId;
	}

	/**
	 * @return
	 */
	public OFigthPvpTarget getFigthPvpTarget() {
		return figthPvpTarget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OFight#getTargetCards()
	 */
	@Override
	public OCardBase[] getTargetCards() {
		// TODO Auto-generated method stub
		return figthPvpTarget.getCards();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightAgainst#getQuestion()
	 */
	@Override
	public JQuestion getJQuestion() {
		if (question == null) {
			answerTime = System.currentTimeMillis();
			figthPvpTarget.answerTime = answerTime;
			question = QuestionService.ME.getQuestionForPvp(this);
		}

		return question;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.OFightAgainst#getJQuestionCount()
	 */
	@Override
	public int getJQuestionCount() {
		return questionCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.client.context.value.OFightAgainst#clearQuestion()
	 */
	@Override
	public void clearQuestion() {
		super.clearQuestion();
		questionCount++;
	}

	/**
	 * @return
	 */
	protected Object disconnectResultData() {
		figthPvpTarget.getPlayerContext().setFightBase(null);
		return super.disconnectResultData();
	}

	/**
	 * @author absir
	 * 
	 */
	public class OFigthPvpTarget extends OFightAgainst {

		/**
		 * @param playerContext
		 * @param playerContextTarget
		 */
		public OFigthPvpTarget(PlayerContext playerContext, PlayerContext playerContextTarget) {
			super(playerContext, playerContextTarget, OFightPvp.this.getCards().length);
		}

		/**
		 * @return
		 */
		public OReportBase createReportResult() {
			return OFightPvp.this.getReportResult();
		}

		/**
		 * @return the categoryId
		 */
		public long getCategoryId() {
			return OFightPvp.this.getCategoryId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OFight#getTargetCards()
		 */
		@Override
		public OCardBase[] getTargetCards() {
			// TODO Auto-generated method stub
			return OFightPvp.this.getCards();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.client.context.value.OFightAgainst#getQuestion()
		 */
		@Override
		public JQuestion getJQuestion() {
			return OFightPvp.this.getJQuestion();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.client.context.value.OFightAgainst#getJQuestionCount
		 * ()
		 */
		@Override
		public int getJQuestionCount() {
			return OFightPvp.this.getJQuestionCount();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.client.context.value.OFightAgainst#clearQuestion()
		 */
		@Override
		public void clearQuestion() {
			OFightPvp.this.clearQuestion();
		}
	}
}
