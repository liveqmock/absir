/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-25 上午10:29:40
 */
package com.absir.appserv.client.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.Session;

import com.absir.appserv.client.bean.JActivity;
import com.absir.appserv.client.bean.JActivityHistory;
import com.absir.appserv.client.bean.JActivityRecard;
import com.absir.appserv.client.bean.JActivityReward;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.bean.value.JeRepetition;
import com.absir.appserv.client.bean.value.JeType;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.client.context.value.OCampCategory;
import com.absir.appserv.client.context.value.OFightBase;
import com.absir.appserv.client.context.value.OPlayerCorrect;
import com.absir.appserv.client.context.value.OPlayerResult;
import com.absir.appserv.client.context.value.OReportBase;
import com.absir.appserv.client.service.ActivityService.OActivity.OActivityResults;
import com.absir.appserv.client.service.ActivityService.OActivity.OFightActivity;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.OReport;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.domain.DActiver;
import com.absir.appserv.system.domain.DActiverRepetition;
import com.absir.async.value.Async;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextService;
import com.absir.context.core.ContextUtils;
import com.absir.orm.hibernate.boost.IEntityMerge;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
public class ActivityService extends ContextService implements IEntityMerge<JActivity> {

	/** 对象单例访问 */
	public static final ActivityService ME = BeanFactoryUtils.get(ActivityService.class);

	/** activities */
	private DActiver<JActivity> actitityActitity;

	/** onlineActivities */
	private Map<Long, OActivity> onlineActivities;

	/**
	 * 初始化服务
	 */
	@Inject
	protected void initService() {
		actitityActitity = new DActiverRepetition<JActivity>("JActivity");
		onlineActivities = new LinkedHashMap<Long, OActivity>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextService#step(long)
	 */
	@Override
	public void step(long contextTime) {
		// TODO Auto-generated method stub
		if (actitityActitity.stepNext(contextTime)) {
			ME.reload(contextTime);
		}
	}

	/**
	 * @param contextTime
	 */
	@Async(notifier = true)
	@Transaction
	protected void reload(long contextTime) {
		List<JActivity> activities = actitityActitity.reloadActives(contextTime);
		LinkedHashMap<Long, OActivity> activityMap = new LinkedHashMap<Long, OActivity>();
		for (JActivity activity : activities) {
			OActivity oActivity = onlineActivities.get(activity.getId());
			if (oActivity != null && oActivity.closed != 0) {
				// 已关闭活动
				onlineActivities.remove(activity.getId());
				oActivity = null;
			}

			if (oActivity == null) {
				// 新开启活动
				activity.setQuestions(QueryDaoUtils.selectQuery(BeanDao.getSession(), "JQuestion", "o", new Object[] { "o.difficult >=", QuestionService.MIN_DIFFICULT }, "ORDER BY RAND()", 0, 10));
				if (activity.getQuestions().isEmpty()) {
					continue;
				}

				oActivity = new OActivity(activity);
			}

			activityMap.put(activity.getId(), oActivity);
		}

		for (Entry<Long, OActivity> entry : onlineActivities.entrySet()) {
			if (!activityMap.containsKey(entry.getKey())) {
				entry.getValue().completeClose();
			}
		}

		onlineActivities = activityMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.hibernate.boost.IEntityMerge#merge(java.lang.String,
	 * java.lang.Object, com.absir.orm.hibernate.boost.IEntityMerge.MergeType,
	 * java.lang.Object)
	 */
	@Override
	public void merge(String entityName, JActivity entity, MergeType mergeType, Object mergeEvent) {
		// TODO Auto-generated method stub
		actitityActitity.merge(entity, mergeType, mergeEvent);
	}

	/**
	 * 获取活动实例
	 * 
	 * @return the activities
	 */
	public OActivity getActivity(Long id) {
		return onlineActivities.get(id);
	}

	/**
	 * 参加活动
	 * 
	 * @param id
	 * @param playerContext
	 * @return
	 */
	public OFightActivity attend(Long id, PlayerContext playerContext) {
		OActivity activity = onlineActivities.get(id);
		if (activity == null || activity.closed != 0) {
			throw new ServerException(ServerStatus.ON_ERROR);
		}

		return activity.attend(playerContext);
	}

	/** ACTIVITY_RESULTSCOM_COMPARATOR */
	private static final Comparator<Entry<Long, OActivityResults>> ACTIVITY_RESULTS_COMPARATOR = new Comparator<Map.Entry<Long, OActivityResults>>() {

		@Override
		public int compare(Entry<Long, OActivityResults> o1, Entry<Long, OActivityResults> o2) {
			// TODO Auto-generated method stub
			OActivityResults r1 = o1.getValue();
			OActivityResults r2 = o2.getValue();
			int compare = r2.correctCount - r1.correctCount;
			if (compare == 0) {
				compare = (int) (r1.answerTime - r2.answerTime);
			}

			return compare;
		}
	};

	/** MAX_HISOTRY_PLAYER_COUNT */
	private static final int MAX_HISOTRY_PLAYER_COUNT = 10;

	/**
	 * 完成活动
	 */
	@Async
	@Transaction
	protected void completeOActivity(OActivity oActivity) {
		JActivity activity = oActivity.activityA.activity;
		// 纪录活动历史
		JActivityHistory activityHistory = new JActivityHistory();
		activityHistory.setActivityBase(activity.getActivityBase());
		activityHistory.setBeginTime(activity.getBeginTime());
		activityHistory.setPassTime(activity.getPassTime());
		activityHistory.setParticipants(oActivity.participants);
		activityHistory.setMaxOnline(oActivity.maxOnline);
		Session session = BeanDao.getSession();
		session.persist(activityHistory);

		int playerSize = MAX_HISOTRY_PLAYER_COUNT;
		List<OPlayerResult> playerResults = new ArrayList<OPlayerResult>();
		activityHistory.setPlayerResults(playerResults);

		// 纪录玩家活动历史
		List<Entry<Long, OActivityResults>> activityResults = new ArrayList<Entry<Long, OActivityResults>>(oActivity.activityA.playerActivityResults.entrySet());
		Collections.sort(activityResults, ACTIVITY_RESULTS_COMPARATOR);
		Iterator<JActivityReward> iterator = activity.getActivityBase().getActivityRewards().iterator();
		JActivityReward activityReward = iterator.hasNext() ? iterator.next() : null;
		int size = activityResults.size();
		for (int i = 0; i < size; i++) {
			Entry<Long, OActivityResults> entry = activityResults.get(i);
			Long playerId = entry.getKey();
			if (playerSize > 0) {
				playerSize--;
				OPlayerResult playerResult = new OPlayerResult(PlayerService.ME.getPlayer(playerId));
				playerResults.add(playerResult);
			}

			JActivityRecard activityRecard = new JActivityRecard();
			activityRecard.setPlayerId(playerId);
			activityRecard.setActivityHistory(activityHistory);
			activityRecard.setRanking(i);
			session.persist(activityRecard);
			// 玩家奖励
			if (activityReward != null) {
				if (i >= activityReward.getRanking()) {
					activityReward = iterator.hasNext() ? iterator.next() : null;
				}

				if (activityRecard != null) {
					PlayerService.ME.addPlayerReward(playerId, activityReward, activity.getActivityBase().getName(), JeType.ACTIVITY, String.valueOf(i));
				}
			}
		}

		// 重启循环活动
		if (activity.getRepetition() != null) {
			activity = (JActivity) session.get(JActivity.class, activity.getId());
			long contextTime = ContextUtils.getContextTime();
			if (activity.getRepetition() != null && activity.getBeginTime() < contextTime && activity.getPassTime() < contextTime) {
				JeRepetition repetition = activity.getRepetition();
				activity.setBeginTime(repetition.getNextTime(activity.getBeginTime()));
				activity.setPassTime(repetition.getNextTime(activity.getPassTime()));
				session.merge(activity);
			}
		}
	}

	/**
	 * 活动信息
	 * 
	 * @author absir
	 * 
	 */
	protected static class OActivity {

		/** activity */
		@JsonIgnore
		OActivityA activityA;

		// 活动关闭
		public int closed;

		// 题目总数
		public int questionCount;

		// 参加人数
		public long participants;

		// 在线人数
		public long online;

		// 最大在线人数
		public long maxOnline;

		/**
		 * @param activity
		 */
		public OActivity(JActivity activity) {
			activityA = new OActivityA(activity);
			questionCount = activity.getQuestions().size();
		}

		/**
		 * 进入活动战斗
		 * 
		 * @param playerContext
		 * @return
		 */
		public synchronized OFightActivity attend(PlayerContext playerContext) {
			if (playerContext.getSocketChannel() == null || activityA.playerActivityResults.containsKey(playerContext.getId())) {
				throw new ServerException(ServerStatus.ON_DENIED);
			}

			++participants;
			if (++online > maxOnline) {
				maxOnline = online;
			}

			return new OFightActivity(playerContext);
		}

		/**
		 * 玩家完成活动
		 */
		public synchronized void completeOnline() {
			--online;
			// if (--online == 0 && closed == 1) {
			// completeEnd();
			// }
		}

		/**
		 * 关闭活动结束
		 */
		public synchronized void completeClose() {
			closed = 1;
			completeEnd();
		}

		/**
		 * 活动[结束]
		 */
		private void completeEnd() {
			// 活动结束, 发放奖励
			closed = 2;
			ME.completeOActivity(this);
		}

		/**
		 * @author absir
		 * 
		 */
		protected static class OActivityResults {

			/** questionIndex */
			@JsonIgnore
			public int questionIndex;

			/** correctCount */
			public int correctCount;

			/** answerTime */
			public long answerTime;

			/** playerCorrects */
			private List<OPlayerCorrect> playerCorrects = new ArrayList<OPlayerCorrect>();

			/**
			 * @param playerCorrect
			 */
			public void addPlayerCorrect(OPlayerCorrect playerCorrect) {
				if (playerCorrects.size() < 5) {
					playerCorrects.add(playerCorrect);
				}
			}

			/**
			 * @return
			 */
			public List<OPlayerCorrect> takePlayerCorrects() {
				int size = playerCorrects.size();
				if (size == 0) {
					return null;
				}

				int i = size - 5;
				if (i < 0) {
					i = 0;
				}

				List<OPlayerCorrect> corrects = new ArrayList<OPlayerCorrect>(size - i);
				for (; i < size; i++) {
					corrects.add(playerCorrects.get(i));
				}

				playerCorrects.clear();
				return corrects;
			}
		}

		/**
		 * @author absir
		 * 
		 */
		public class OFightActivity extends OFightBase {

			/** activityResults */
			private OActivityResults activityResults;

			/**
			 * @param playerContext
			 */
			public OFightActivity(PlayerContext playerContext) {
				super(playerContext);
				// TODO Auto-generated constructor stub
				activityResults = new OActivityResults();
				activityA.playerActivityResults.put(playerContext.getId(), activityResults);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.absir.appserv.game.value.OFight#getTargetCards()
			 */
			@JsonIgnore
			@Override
			public OCardBase[] getTargetCards() {
				// TODO Auto-generated method stub
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.absir.appserv.client.context.value.OFightBase#getCampQuestions
			 * ()
			 */
			@JsonIgnore
			@Override
			public OCampCategory[] getCampQuestions() {
				return null;
			}

			/**
			 * @return
			 */
			public JQuestion getJQuestion() {
				if (question == null) {
					answerTime = System.currentTimeMillis();
					question = activityA.activity.getQuestions().get(activityResults.questionIndex);
				}

				return question;
			}

			/** ACTIVITY_PUSH_INTERVAL */
			private static final long ACTIVITY_PUSH_INTERVAL = 3000;

			/** pushNextTime */
			private long pushNextTime;

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.absir.appserv.client.context.value.OFightBase#steping(long)
			 */
			@Override
			public void steping(long contextTime) {
				if (pushNextTime < contextTime) {
					pushNextTime = contextTime + ACTIVITY_PUSH_INTERVAL;
					SocketService.writeByteObject(getPlayerContext().getSocketChannel(), SocketService.CALLBACK_ACTIVITY, activityResults.takePlayerCorrects());
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.absir.appserv.client.context.value.OFightBase#closed()
			 */
			@Override
			public void closed() {
				completeOnline();
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
				activityResults.answerTime += time - answerTime;
				answer(question, answer, time);
				int questionIndex = activityResults.questionIndex;
				if (++activityResults.questionIndex >= activityA.activity.getQuestions().size()) {
					reportResult.setResult(EResult.VICTORY);
					getPlayerContext().setFightBase(null);

				} else {
					question = null;
					reportResult.setResultData(getJQuestion());
				}

				if (getReportResult().answer == getReportResult().correct) {
					activityResults.correctCount++;
				}

				// 添加推送结果
				activityA.addReportResult(questionIndex, getPlayerContext(), getReportResult());
				return reportResult;
			}

			/**
			 * 购买答案
			 * 
			 * @return
			 */
			public OReport correct() {
				return null;
			}
		}
	}

	/**
	 * 活动详细信息
	 * 
	 * @author absir
	 * 
	 */
	protected static class OActivityA {

		/** activity */
		public JActivity activity;

		/** playerActivityResults */
		public Map<Long, OActivityResults> playerActivityResults = new ConcurrentHashMap<Long, OActivityResults>();

		/**
		 * @param activity
		 */
		public OActivityA(JActivity activity) {
			this.activity = activity;
		}

		/**
		 * @param playerContext
		 * @param reportBase
		 */
		public void addReportResult(int questionIndex, PlayerContext playerContext, OReportBase reportBase) {
			OPlayerCorrect playerCorrect = new OPlayerCorrect(playerContext.getPlayer(), reportBase.answer == reportBase.correct);
			OActivityResults activityResults;
			for (Entry<Long, OActivityResults> entry : playerActivityResults.entrySet()) {
				activityResults = entry.getValue();
				if (questionIndex == activityResults.questionIndex && !playerContext.getId().equals(entry.getKey())) {
					entry.getValue().addPlayerCorrect(playerCorrect);
				}
			}
		}
	}
}
