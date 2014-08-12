/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-22 下午3:40:16
 */
package com.absir.system.test.fight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskDetail;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskPass;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.value.OCardPlayer;
import com.absir.appserv.client.context.value.OFightBase;
import com.absir.appserv.client.context.value.OProp_SKILL;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OReportDetail;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.appserv.system.service.impl.BeanServiceImpl;
import com.absir.bean.config.IBeanTypeFilter;
import com.absir.bean.core.BeanDefineDiscover;
import com.absir.bean.core.BeanFactoryProvider;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.context.core.ContextFactory;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class TestFight {

	/**
	 * @author absir
	 * 
	 */
	public enum Flag {

		self,

		target,

		number,

		correct,

		taskIndex;

	}

	/**
	 * @author absir
	 * 
	 */
	public static class BeanServiceFilter extends BeanServiceImpl {

		/**
		 * 
		 */
		public BeanServiceFilter() {
			super(null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#get(java.lang.Class,
		 * java.io.Serializable)
		 */
		@Override
		public <T> T get(Class<T> entityClass, Serializable id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#get(java.lang.String,
		 * java.io.Serializable)
		 */
		@Override
		public Object get(String entityName, Serializable id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#get(java.lang.String,
		 * java.lang.Class, java.io.Serializable)
		 */
		@Override
		public <T> T get(String entityName, Class<T> entityClass, Serializable id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#find(java.lang.Class,
		 * java.lang.Object)
		 */
		@Override
		public <T> T find(Class<T> entityClass, Object id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#find(java.lang.String,
		 * java.lang.Object)
		 */
		@Override
		public Object find(String entityName, Object id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#find(java.lang.String,
		 * java.lang.Class, java.lang.Object)
		 */
		@Override
		public <T> T find(String entityName, Class<T> entityClass, Object id) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#find(java.lang.Class,
		 * java.lang.Object[])
		 */
		@Override
		public <T> T find(Class<T> entityClass, Object... conditions) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#find(java.lang.String,
		 * java.lang.Class, java.lang.Object[])
		 */
		@Override
		public <T> T find(String entityName, Class<T> entityClass, Object... conditions) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#persist(java.lang.Object
		 * )
		 */
		@Override
		public void persist(Object entity) {
			// TODO Auto-generated method stub
			// getSession().persist(entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#persist(java.lang.String
		 * , java.lang.Object)
		 */
		@Override
		public void persist(String entityName, Object entity) {
			// TODO Auto-generated method stub
			// getSession().persist(entityName, entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#update(java.lang.Object)
		 */
		@Override
		public void update(Object entity) {
			// TODO Auto-generated method stub
			// getSession().update(entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#update(java.lang.String,
		 * java.lang.Object)
		 */
		@Override
		public void update(String entityName, Object entity) {
			// TODO Auto-generated method stub
			// getSession().update(entityName, entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#merge(java.lang.Object)
		 */
		@Override
		public <T> T merge(T entity) {
			// TODO Auto-generated method stub
			return entity;
			// return (T) getSession().merge(entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#merge(java.lang.String,
		 * java.lang.Object)
		 */
		@Override
		public <T> T merge(String entityName, T entity) {
			// TODO Auto-generated method stub
			return entity;
			// return (T) getSession().merge(entityName, entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#delete(java.lang.Object)
		 */
		@Override
		public void delete(Object entity) {
			// TODO Auto-generated method stub
			// getSession().delete(entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.service.BeanService#delete(java.lang.String,
		 * java.lang.Object)
		 */
		@Override
		public void delete(String entityName, Object entity) {
			// TODO Auto-generated method stub
			// getSession().delete(entityName, entity);
		}

	}

	/**
	 * @author absir
	 * 
	 */
	public static class BeanTypeFilter implements IBeanTypeFilter {
		@Override
		public boolean filt(Class<?> beanType) {
			// TODO Auto-generated method stub
			return true;
		}
	}

	/**
	 * @param parameters
	 */
	public static void main(String... parameters) {
		// JCard card = FightServiceUtils.generate(cardDefine, this);
		// System.out.println("Thread.currentThread().getContextClassLoader()" +
		// Thread.currentThread().getContextClassLoader());
		// System.out.println("getResource()" +
		// Thread.currentThread().getContextClassLoader().getResource(""));
		// System.out.println("TestFight.class.getResource().getPath()" +
		// TestFight.class.getResource("").getPath());
		// HelperFileName.normalize(filename)
		// HelperFileName.normalize(filename)
		BeanDefineDiscover.open();
		BeanFactoryProvider beanFactoryProvider = new BeanFactoryProvider(null, null, null);
		beanFactoryProvider.scan(null, null, new BeanTypeFilter(), new BeanServiceFilter(), new ContextFactory());
		System.out.println("程序开始运行:" + BeanFactoryUtils.getBeanConfig().getResourcePath() + "\r\n");
		// System.out.println(ContextUtils.getContextFactory());

		PlayerContext playerContext = new PlayerContext();
		Flag flag = null;
		int length = parameters.length;
		int number = 1;
		float correct = 0.8f;
		boolean details = false;
		boolean ss = false;
		List<JCard> cards = new ArrayList<JCard>();
		List<JCard> targets = new ArrayList<JCard>();
		List<List<JCard>> targetAll = new ArrayList<List<JCard>>();
		for (int i = 0; i < length; i++) {
			String parameter = parameters[i];
			if (parameter.startsWith("-")) {
				if ("-s".equals(parameter)) {
					flag = Flag.self;
					continue;

				} else if ("-t".equals(parameter)) {
					flag = Flag.target;
					targets = new ArrayList<JCard>();
					targetAll.add(targets);
					continue;

				} else if ("-n".equals(parameter)) {
					flag = Flag.number;
					continue;

				} else if ("-c".equals(parameter)) {
					flag = Flag.correct;
					continue;

				} else if ("-d".equals(parameter)) {
					details = true;
					continue;

				} else if ("-ss".equals(parameter)) {
					ss = true;
					continue;

				} else if ("-x".equals(parameter)) {
					flag = Flag.taskIndex;
					continue;
				}

				flag = null;

			} else if (flag != null) {
				JCard card = null;
				switch (flag) {
				case self:
					card = generateCard(parameter, playerContext, ss);
					if (card != null) {
						cards.add(card);
					}

					break;
				case target:
					card = generateCard(parameter, playerContext, ss);
					if (card != null) {
						targets.add(card);
					}

					break;

				case number:
					number = KernelDyna.toInteger(parameter, 1);
					break;

				case correct:
					correct = KernelDyna.toFloat(parameter, 0.8f);
					break;

				case taskIndex:
					TaskDetail taskDetail = getTaskDetail(KernelDyna.toInteger(parameter, 0));
					ab = 0.5f + taskDetail.getAtkBonus();
					if (taskDetail != null) {
						for (int n = 0; n < taskDetail.getNumber(); n++) {
							targets = new ArrayList<JCard>();
							targetAll.add(targets);
							for (int m = 0; m < 3; m++) {
								targets.add(generateCard(taskDetail.getEnemies()[HelperRandom.nextInt(taskDetail.getEnemies().length)].getId() + "," + taskDetail.getLevel(), playerContext, ss));
							}
						}

						targets = new ArrayList<JCard>();
						targetAll.add(targets);
						for (XCardDefine cardDefine : taskDetail.getBosses()) {
							targets.add(generateCard(cardDefine.getId() + "," + taskDetail.getMaxLevel(), playerContext, ss));
						}
					}

					break;

				default:
					break;
				}
			}
		}

		if (cards.size() == 0) {
			System.out.println("self cards is empty, please use -s cardDefineId,level ...");
			return;
		}

		System.out.println("战斗开始=>正确率:" + correct);
		String cardParams = "";
		for (JCard card : cards) {
			cardParams += card.getCardDefine().getId() + "," + card.getLevel() + " ";
		}

		System.out.println("己方卡牌:" + cardParams);
		int index = 0;
		for (List<JCard> targetCards : targetAll) {
			cardParams = "";
			for (JCard card : targetCards) {
				cardParams += card.getCardDefine().getId() + "," + card.getLevel() + " ";
			}

			System.out.println("对方卡牌[" + index + "]:" + cardParams);
			index++;
		}

		int round = 0;
		int victory = 0;
		KernelObject.declaredSet(playerContext, "player", new JPlayer());
		playerContext.getPlayer().getCards().addAll(cards);
		for (int i = 0; i < number; i++) {
			OFightTest fightTest = new OFightTest(playerContext, targetAll, details);
			if (fightTest.answerAll(correct)) {
				victory++;
			}

			round += fightTest.getRound();
		}

		System.out.println("战斗结束=>总次数:" + number + ", 胜利次数" + victory + ", 总回合数" + round);
	}

	/**
	 * @param taskIndex
	 * @return
	 */
	private static TaskDetail getTaskDetail(int taskIndex) {
		if (taskIndex < 0) {
			return null;
		}

		int si = 0;
		for (XTaskDefine taskDefine : PlayerContext.TASK_DEFINE_XLS_DAO.getAll()) {
			int pi = 0;
			for (TaskPass taskPass : taskDefine.getTaskPasses()) {
				int di = 0;
				for (TaskDetail taskDetail : taskPass.getTaskDetails()) {
					if (taskIndex-- == 0) {
						df = 1.0f + (si * 100 + pi * 10 + di) / 100.0f + taskDetail.getDifficulty();
						return taskDetail;
					}
				}
			}
		}

		return null;
	}

	/**
	 * @param parameter
	 * @param playerContext
	 * @return
	 */
	private static JCard generateCard(String parameter, PlayerContext playerContext, boolean ss) {
		String[] parameters = parameter.split(",");
		if (parameters.length == 2) {
			XCardDefine cardDefine = PlayerContext.CARD_DEFINE_XLS_DAO.find(parameters[0]);
			// System.out.println("卡牌:"+ HelperJson.encodeNull(cardDefine));
			if (cardDefine != null) {
				JCard card = FightServiceUtils.generate(cardDefine, playerContext);
				int level = KernelDyna.toInteger(parameters[1], 1);
				if (level < 1) {
					level = 1;
				}

				PlayerContext.modifyCardLevel(card, level);
				if (ss) {
					// 自带技能
					int skillm = (int) (cardDefine.getMaxSkill() * (float) (0 + 5) / cardDefine.getMaxLevel() * 1);
					if (skillm > 1) {
						skillm = (skillm < 1 ? 0 : HelperRandom.nextInt((int) skillm));
						if (skillm > 0) {
							if (skillm > cardDefine.getMaxSkill()) {
								skillm = cardDefine.getMaxSkill();
							}

							XSkillDefine[] skillDefines = OProp_SKILL.getXSkillDefines(cardDefine.getCamp(), cardDefine.getRare());
							for (; skillm > 0; skillm--) {
								OProp_SKILL.prop(card, skillDefines, skillm);
							}
						}
					}
				}

				// System.out.println("卡牌:"+ HelperJson.encodeNull(card));
				return card;
			}
		}

		return null;
	}

	static float df = 1.0f;

	static float ab = 0.5f;

	/**
	 * @author absir
	 * 
	 */
	public static class OCardPlayerTest extends OCardPlayer {

		/**
		 * @param oFightBase
		 * @param id
		 * @param card
		 */
		public OCardPlayerTest(OFightBase oFightBase, Serializable id, JCard card) {
			super(oFightBase, id, card);
			// TODO Auto-generated constructor stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.client.context.value.OCardPlayer#atk()
		 */
		@Override
		public boolean atk() {
			OFightBase fightBase = currentFight();
			if (fightBase.getReportResult().answer == fightBase.getReportResult().correct && (paused == 0 || --paused == 0)) {
				float gaussian = HelperRandom.RANDOM.nextFloat();
				return gaussian <= 0.99;
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.game.value.OObject#die(com.absir.appserv.game.value
		 * .IResult)
		 */
		@Override
		public void die(IResult result) {
		}
	}

	public static class OFightTest extends OFightBase {

		List<List<JCard>> targetAll;

		/** targetBases */
		OCardBase[] targetBases;

		boolean details;

		int index;

		int round;

		/**
		 * @param playerContext
		 */
		public OFightTest(PlayerContext playerContext, List<List<JCard>> targetAll, boolean details) {
			super(playerContext);
			// TODO Auto-generated constructor stub
			// 玩家卡牌
			List<JCard> jCards = playerContext.getPlayer().getCards();
			int cardLength = jCards.size();
			// int playerCardLength = cardLength;
			cards = new OCardPlayer[cardLength];
			for (int i = 0; i < cardLength; i++) {
				cards[i] = new OCardPlayerTest(this, i, jCards.get(i));
			}

			this.targetAll = targetAll;
			this.details = details;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OFight#getTargetCards()
		 */
		@Override
		public OCardBase[] getTargetCards() {
			// TODO Auto-generated method stub
			if (targetBases != null) {
				return targetBases;
			}

			while (index < targetAll.size()) {
				List<JCard> targets = targetAll.get(index++);
				int size = targets.size();
				if (size > 0) {
					int cardsLength = getCards().length;
					targetBases = new OCardBase[size];
					for (int i = 0; i < size; i++) {
						OCardBase cardBase = new OCardBase(cardsLength + i, targets.get(i));
						KernelObject.declaredSet(cardBase, "hp", (Integer) (int) cardBase.getBuffAttP("hp", cardBase.baseHp(), df));
						cardBase.setMaxHp((int) cardBase.getBuffAttP("maxHp", cardBase.baseHp(), df));
						cardBase.setAtk((int) cardBase.getBuffAttP("atk", cardBase.baseAtk(), ab * df));

						targetBases[i] = cardBase;
					}

					return targetBases;
				}
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.client.context.value.OFightBase#getQuestions()
		 */
		@Override
		public List<JQuestion> getQuestions() {
			return null;
		}

		/**
		 * @param correct
		 * @return
		 */
		public int getRound() {
			return round;
		}

		/**
		 * @param correct
		 * @return
		 */
		public boolean answerAll(float correct) {
			if (details) {
				System.out.println("开始战斗");
			}

			while (true) {
				round++;
				reportResult.answer = HelperRandom.RANDOM.nextFloat() < correct ? 0 : 1;
				step(0);
				if (reportResult.getResult() == EResult.VICTORY) {
					targetBases = null;
					if (getTargetCards() == null) {
						return true;
					}

					reportResult.setResult(EResult.DONE);

				} else if (reportResult.getResult() == EResult.LOSS) {
					return false;
				}
			}
		}

		private String detailPrefix = "";

		/**
		 * 并行战报
		 * 
		 * @param allDetails
		 */
		public void pushDetails(List<List<OReportDetail>> allDetails) {
			detailPrefix += " ";
		}

		/**
		 * 并行战报
		 */
		public void popDetails() {
			if (detailPrefix.length() > 0) {
				detailPrefix = detailPrefix.substring(1, detailPrefix.length());
			}
		}

		/*
		 * 并行战报
		 * 
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.game.value.OFight#addReportDetail(java.io.Serializable
		 * , java.io.Serializable[], java.lang.String, java.lang.Object)
		 */
		@Override
		public void addReportDetail(Serializable self, Serializable[] targets, String effect, Object parameters) {
			if (details) {
				System.out.println(detailPrefix + "攻击纪录: 卡牌 " + self + " 目标 " + (targets == null ? null : KernelString.implode(targets, ',')) + " 效果 " + effect + " 参数 " + parameters);
			}
		}
	}
}
