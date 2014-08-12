/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-17 下午12:29:36
 */
package com.absir.appserv.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;

import com.absir.appserv.client.bean.JPlayerAnswer;
import com.absir.appserv.client.bean.JPlayerAnswerLog;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.bean.JQuestionAnswer;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OFightPvp;
import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.helper.HelperBase;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.value.Schedule;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
public class QuestionService {

	/** ME */
	public static final QuestionService ME = BeanFactoryUtils.get(QuestionService.class);

	// 屏蔽题目
	public static final float MIN_DIFFICULT = 0.1f;

	// 三色题目
	public static final float HARD_DIFFICULT = 0.4f;

	// 双色题目
	public static final float NORMAL_DIFFICULT = 0.7f;

	// 单色题目
	public static final float EASY_DIFFICULT = 1.0f;

	/** 默认难度数量 */
	private final int DIFFICULT_COUNT = 100;

	/** QUESTION_ANSWER_MIN */
	private final int QUESTION_ANSWER_MIN = 5120;

	/** QUESTION_ANSWER_BUFF_SIZE */
	private final int QUESTION_ANSWER_BUFF_SIZE = QUESTION_ANSWER_MIN + 1024;

	/** questionAnwers(答题缓冲) */
	List<Object[]> questionAnwers = new ArrayList<Object[]>(QUESTION_ANSWER_BUFF_SIZE);

	/**
	 * 获取题目
	 * 
	 * @param playerContext
	 * @return
	 */
	@Transaction(readOnly = true)
	public List<JQuestion> getQuestionsForLevel(int level) {
		return QueryDaoUtils.list(QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JQuestion o WHERE o.minLevel <= ? AND o.maxLevel >= ? AND o.difficult >= ? ORDER BY RAND()",
				level, level, MIN_DIFFICULT).setMaxResults(4));
	}

	/**
	 * PK题目
	 * 
	 * @param fightPvp
	 * @return
	 */
	@Transaction(readOnly = true)
	public JQuestion getQuestionForPvp(OFightPvp fightPvp) {
		Query query = fightPvp.getCategoryId() > 0 ? QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JQuestion o WHERE o.category.id = ? AND o.difficult >= ? ORDER BY RAND()",
				fightPvp.getCategoryId(), MIN_DIFFICULT) : QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JQuestion o WHERE o.difficult >= ? ORDER BY RAND()", MIN_DIFFICULT);
		return (JQuestion) QueryDaoUtils.first(query);
	}

	/**
	 * 获取题目色彩数量
	 * 
	 * @param question
	 * @return
	 */
	public static int getQuestionCampCount(JQuestion question) {
		float difficult = question.getDifficult();
		if (difficult <= HARD_DIFFICULT) {
			return 3;

		} else if (difficult <= NORMAL_DIFFICULT) {
			return 2;

		} else {
			return 1;
		}
	}

	/**
	 * 答题纪录
	 * 
	 * @param playerContext
	 * @param question
	 * @param correct
	 * @param answer
	 * @param answerTime
	 */
	@Transaction
	public void answerQuestion(PlayerContext playerContext, JQuestion question, int answer, long answerTime) {
		boolean correct = answer == question.getCorrect();
		// 用户答题情况
		Long categoryId = (Long) HelperBase.getLazyId(question.getCategory());
		if (categoryId != null) {
			playerContext.answer(categoryId, correct, answerTime);
		}

		// 纪录用户答题情况
		++answer;
		questionAnwers.add(new Object[] { playerContext.getPlayer().getId(), question.getId(), correct ? answer : -answer, answerTime, ContextUtils.getContextTime() });
	}

	/**
	 * 处理答题缓冲
	 */
	@Schedule(fixedDelay = 600000)
	@Transaction
	public void doQuestionAnswers() {
		if (questionAnwers.size() >= QUESTION_ANSWER_MIN) {
			updateQuesionAnswers();
		}
	}

	/**
	 * 定时更新答题缓冲
	 */
	@Stopping
	@Transaction
	public void updateQuesionAnswers() {
		if (questionAnwers.size() == 0) {
			return;
		}

		List<Object[]> questionAnwers = this.questionAnwers;
		this.questionAnwers = new ArrayList<Object[]>(QUESTION_ANSWER_BUFF_SIZE);
		Map<Long, JPlayerAnswer> playerAnswerMap = new HashMap<Long, JPlayerAnswer>();
		Map<Long, JQuestionAnswer> questionAnswerMap = new HashMap<Long, JQuestionAnswer>();
		for (Object[] questionAnwer : questionAnwers) {
			Long playerId = (Long) questionAnwer[0];
			Long questionId = (Long) questionAnwer[1];
			int answer = (Integer) questionAnwer[2];
			long answerTime = (Long) questionAnwer[3];
			long createTime = (Long) questionAnwer[4];
			JEmbedLL embedLL = new JEmbedLL();
			embedLL.setEid(playerId);
			embedLL.setMid(questionId);

			// 玩家答题日志
			JPlayerAnswerLog playerAnswerLog = new JPlayerAnswerLog();
			playerAnswerLog.setEmbedLL(embedLL);
			playerAnswerLog.setAnswer(answer);
			playerAnswerLog.setAnswerTime(answerTime);
			playerAnswerLog.setCreateTime(createTime);
			BeanDao.getSession().persist(playerAnswerLog);

			// 玩家回答更新
			JPlayerAnswer playerAnswer = playerAnswerMap.get(playerId);
			if (playerAnswer == null) {
				playerAnswer = BeanDao.get(BeanDao.getSession(), JPlayerAnswer.class, embedLL);
				if (playerAnswer == null) {
					playerAnswer = new JPlayerAnswer();
					playerAnswer.setId(embedLL);
				}

				playerAnswerMap.put(playerId, playerAnswer);
			}

			playerAnswer.setAnswerCount(playerAnswer.getAnswerCount() + 1);
			if (answer > 0) {
				playerAnswer.setAnswerCorrect(playerAnswer.getAnswerCorrect() + 1);
			}

			playerAnswer.setAnswerTime(playerAnswer.getAnswerTime() + answerTime);

			// 题目回答更新
			JQuestionAnswer questionAnswer = questionAnswerMap.get(questionId);
			if (questionAnswer == null) {
				questionAnswer = BeanDao.get(BeanDao.getSession(), JQuestionAnswer.class, questionId);
				if (questionAnswer == null) {
					questionAnswer = new JQuestionAnswer();
					questionAnswer.setId(questionId);
				}

				questionAnswerMap.put(questionId, questionAnswer);
			}

			questionAnswer.setAnswerCount(questionAnswer.getAnswerCount() + 1);
			if (answer > 0) {
				questionAnswer.setAnswerCorrect(questionAnswer.getAnswerCorrect() + 1);
			}

			questionAnswer.setAnswerTime(questionAnswer.getAnswerTime() + answerTime);
		}

		// 清除缓存
		BeanDao.getSession().flush();
		BeanDao.getSession().clear();

		// 更新玩家回答
		for (Entry<Long, JPlayerAnswer> entry : playerAnswerMap.entrySet()) {
			BeanDao.getSession().merge(entry.getValue());
		}

		// 清除缓存
		BeanDao.getSession().flush();
		BeanDao.getSession().clear();

		// 更新题目回答
		for (Entry<Long, JQuestionAnswer> entry : questionAnswerMap.entrySet()) {
			JQuestionAnswer questionAnswer = entry.getValue();
			BeanDao.getSession().merge(questionAnswer);
			// 更新题目正确率
			JQuestion question = BeanDao.get(BeanDao.getSession(), JQuestion.class, entry.getKey());
			if (question != null) {
				question.setDifficult((questionAnswer.getAnswerCorrect() + question.getDifficult() * DIFFICULT_COUNT + 1) / (questionAnswer.getAnswerCount() + DIFFICULT_COUNT + 1));
				BeanDao.getSession().merge(question);
			}
		}
	}
}
