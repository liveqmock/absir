/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-17 下午7:07:46
 */
package com.absir.appserv.client.context.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.bean.JbSkill;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.QuestionService;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OCard;
import com.absir.appserv.game.value.OFight;
import com.absir.appserv.game.value.OReport;
import com.absir.appserv.game.value.OReportDetail;
import com.absir.appserv.system.helper.HelperBase;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public abstract class OFightBase extends OFight<OFightBase.OCardBase, OReportBase> {

	// 玩家
	@JsonIgnore
	PlayerContext playerContext;

	// 玩家卡牌
	protected OCardPlayer[] cards;

	// 题目难度
	@JsonIgnore
	int level;

	// 题目分类
	OCampCategory[] campCategories;

	// 选择分类
	@JsonIgnore
	OCampCategory campCategory;

	// 当前题目
	@JsonIgnore
	List<JQuestion> questions;

	// 选择题目
	@JsonIgnore
	protected JQuestion question;

	// 消除错误答案
	@JsonIgnore
	List<Integer> disIndexs = new ArrayList<Integer>();

	// 答题次数
	@JsonIgnore
	int answerCount;

	// 正确次数
	@JsonIgnore
	int correctCount;

	// 联系正确次数
	@JsonIgnore
	int ssCorrect;

	// 答题时间
	@JsonIgnore
	protected long answerTime;

	// 应援
	@JsonIgnore
	boolean encourage;

	// 并行战报
	@JsonIgnore
	private List<OReportDetail> reportDetails;

	// 最大答题时间
	public static final long MAX_ANSWERE_TIME = 30000;

	/**
	 * @author absir
	 * 
	 */
	public class OCardBase extends OCard<OCardBase, OFightBase> {

		// 对应卡牌
		private JCard card;

		/** effectTicks */
		@JsonIgnore
		private ObjectEntry<OSkillNs, Integer>[] skillNsMapTicks;

		/**
		 * @param id
		 */
		public OCardBase(Serializable id, JCard card) {
			super(id);
			// TODO Auto-generated constructor stub
			this.card = card;
			maxHp = hp = baseHp();
			atk = baseAtk();

			List<JbSkill> skills = card.getSkills();
			synchronized (skills) {
				int size = skills.size();
				skillNsMapTicks = new ObjectEntry[size];
				ObjectEntry<OSkillNs, Integer> entry;
				for (int i = 0; i < size; i++) {
					entry = new ObjectEntry<OSkillNs, Integer>();
					entry.setKey(skills.get(i).getSkillDefine().getOSkillNs());
					entry.setValue(0);
					skillNsMapTicks[i] = entry;
				}
			}
		}

		/**
		 * @return the card
		 */
		public JCard getCard() {
			return card;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OCard#getFight()
		 */
		@Override
		public OFightBase currentFight() {
			// TODO Auto-generated method stub
			return OFightBase.this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OFight.OCard#getBaseHp()
		 */
		@Override
		public int baseHp() {
			// TODO Auto-generated method stub
			return card.getHp();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OFight.OCard#getBaseAtk()
		 */
		@Override
		public int baseAtk() {
			// TODO Auto-generated method stub
			return card.getAtk();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OCard#target()
		 */
		@Override
		public OCardBase fetchTarget() {
			// JeCamp camp = card.getCardDefine().getCamp();
			OCardBase[] cardBases = inTarget() ? getCards() : getTargetCards();
			int length = cardBases.length;
			if (length > 0) {
				target = null;
				for (OCardBase card : cardBases) {
					if (card.hp > 0) {
						if (target == null) {
							target = card;
						}

						if (!card.invincible) {
							if (HelperRandom.RANDOM.nextFloat() <= (1.0f / length)) {
								return card;
							}

							if (target.isInvincible()) {
								target = card;
							}
						}
					}

					length--;
				}
			}

			return target;
		}

		/*
		 * 普通攻击
		 * 
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.game.value.OObject#atk(com.absir.appserv.game.value
		 * .OObject, long, com.absir.appserv.game.value.OResult)
		 */
		@Override
		protected void atk(OCardBase target, long time, IResult result) {
			// TODO Auto-generated method stub
			int length = skillNsMapTicks.length;
			if (length > 0) {
				int s = -1;
				int rnd = HelperRandom.nextInt(length);
				ObjectEntry<OSkillNs, Integer> entry;
				for (int i = 0; i < length; i++) {
					entry = skillNsMapTicks[i];
					if (entry.getValue() == 0) {
						if (i == rnd) {
							s = i;
						}

						if (s < 0) {
							s = i;
						}

					} else {
						if (i == rnd) {
							rnd++;
						}

						entry.setValue(entry.getValue() - 1);
					}
				}

				if (s >= 0) {
					entry = skillNsMapTicks[s];
					entry.setValue(entry.getKey().skillDefine.getTick());
					entry.getKey().cast(this, result);

					return;
				}
			}

			normalAtk(target, time, result);
		}

		/**
		 * 普通攻击
		 * 
		 * @param target
		 * @param time
		 * @param result
		 */
		public void normalAtk(OCardBase target, long time, IResult result) {
			JeCamp camp = card.getCardDefine().getCamp();
			addReportDetail(target, null, camp);
			atk(target, FightServiceUtils.aioiGramsAtk(this, target, atk), camp, result);
		}
	}

	/**
	 * @param playerContext
	 */
	public OFightBase(PlayerContext playerContext) {
		this.playerContext = playerContext;
		playerContext.setFightBase(this);
	}

	/**
	 * @return the playerContext
	 */
	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	/**
	 * @param playerContext
	 *            the playerContext to set
	 */
	public void setPlayerContext(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 战斗步进
	 * 
	 * @param contextTime
	 */
	public void steping(long contextTime) {
	}

	/**
	 * 战斗关闭
	 */
	public void closed() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OFight#createReport()
	 */
	@Override
	protected OReportBase createReportResult() {
		// TODO Auto-generated method stub
		return new OReportBase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OFight#getCards()
	 */
	@Override
	public OCardPlayer[] getCards() {
		// TODO Auto-generated method stub
		return cards;
	}

	/**
	 * 并行战报
	 * 
	 * @param allDetails
	 */
	public void pushDetails(List<List<OReportDetail>> allDetails) {
		reportDetails = new ArrayList<OReportDetail>();
		allDetails.add(reportDetails);
	}

	/**
	 * 并行战报
	 */
	public void popDetails() {
		reportDetails = null;
	}

	/*
	 * 并行战报
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OFight#addReportDetail(java.io.Serializable,
	 * java.io.Serializable[], java.lang.String, java.lang.Object)
	 */
	@Override
	public void addReportDetail(Serializable self, Serializable[] targets, String effect, Object parameters) {
		OReportDetail reportDetail = createReportDetail();
		reportDetail.setSelf(self);
		reportDetail.setTargets(targets);
		reportDetail.setEffect(effect);
		reportDetail.setEffectData(parameters);
		if (reportDetails == null) {
			reportResult.addReportDetail(reportDetail);

		} else {
			reportDetails.add(reportDetail);
		}
	}

	/**
	 * 设置目标卡牌
	 * 
	 * @param cardId
	 * @param targetId
	 */
	public synchronized void setTarget(int cardId, int targetId) {
		targetId -= getCards().length;
		if (cardId < 0 || cardId >= getCards().length || targetId < 0 || targetId >= getTargetCards().length) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		getCards()[cardId].setTarget(getTargetCards()[targetId]);
	}

	/**
	 * 获取题目分类
	 * 
	 * @return
	 */
	public OCampCategory[] getCampQuestions() {
		getQuestions();
		return campCategories;
	}

	/**
	 * 获取题目列表
	 * 
	 * @return the questions
	 */
	protected List<JQuestion> getQuestions() {
		if (campCategories == null || questions == null) {
			question = null;
			questions = QuestionService.ME.getQuestionsForLevel(level);
			int length = questions.size();
			if (campCategories == null || length != campCategories.length) {
				campCategories = new OCampCategory[length];
				for (int i = 0; i < length; i++) {
					campCategories[i] = new OCampCategory();
				}
			}

			for (int i = 0; i < length; i++) {
				OCampCategory campQuestion = campCategories[i];
				JQuestion question = questions.get(i);
				campQuestion.categoryId = (Long) HelperBase.getLazyId(question.getCategory());
				campQuestion.camps = FightServiceUtils.getJeCamps(QuestionService.getQuestionCampCount(question));
			}
		}

		return questions;
	}

	/**
	 * 选择题目分类
	 * 
	 * @param category
	 * @return
	 */
	public synchronized JQuestion getQuestion(int category) {
		if (questions == null || category < 0 || category >= questions.size()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		disIndexs.clear();
		question = questions.get(category);
		campCategory = campCategories[category];
		answerTime = System.currentTimeMillis();
		return question;
	}

	/**
	 * 清除题目
	 */
	protected void clearQuestion() {
		question = null;
		questions = null;
	}

	/**
	 * 回答问题
	 * 
	 * @param answer
	 */
	public synchronized OReport answer(int answer) {
		if (question == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		long time = System.currentTimeMillis();
		answer(question, answer, time);
		clearQuestion();
		return step(time);
	}

	/**
	 * @param question
	 * @param answer
	 * @param time
	 * @return
	 */
	protected int answer(JQuestion question, int answer, long time) {
		reportResult.began(time);
		reportResult.correct = question.getCorrect();
		answerCount++;
		answerTime = time - answerTime;
		if (answer < 0 || answerTime > MAX_ANSWERE_TIME) {
			// 回答超时
			answer = -1;

		} else if (question.getCorrect() == answer) {
			// 回答正确
			correctCount++;
			ssCorrect++;
			if (getCards() != null) {
				for (OCardPlayer cardPlayer : getCards()) {
					cardPlayer.ssCorrect(ssCorrect);
				}
			}

		} else {
			// 回答错误
			ssCorrect = 0;
		}

		reportResult.answer = answer;
		reportResult.correct = question.getCorrect();

		QuestionService.ME.answerQuestion(playerContext, question, answer, answerTime);
		return answer;
	}

	/**
	 * 释放SS技能
	 * 
	 * @param cardId
	 * @return
	 */
	public synchronized OReport ss(int cardId) {
		if (cardId < 0 || cardId >= getCards().length) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		OCardPlayer cardPlayer = getCards()[cardId];
		if (cardPlayer.died()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		reportResult.began(System.currentTimeMillis());
		cardPlayer.ss(reportResult);

		OReport report = dealReportResult(reportResult);
		if (report.getResult() == EResult.CONTINUE) {
			report.setResultData(null);
		}

		return report;
	}

	/**
	 * 购买答案
	 * 
	 * @return
	 */
	public synchronized OReport correct() {
		if (question == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		playerContext.modifyDiamond(-1);
		return answer(question.getCorrect());
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
			reportResult.setResultData(getCampQuestions());

		} else if (reportResult.getResult() != EResult.DONE) {
			playerContext.setFightBase(null);
		}

		return reportResult;
	}
}
