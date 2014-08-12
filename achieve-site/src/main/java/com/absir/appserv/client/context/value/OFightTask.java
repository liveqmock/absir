/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-15 下午5:10:04
 */
package com.absir.appserv.client.context.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskDetail;
import com.absir.appserv.client.configure.xls.XTaskDefine.TaskPass;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.utils.FightServiceUtils;
import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.context.core.ContextMap;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
public class OFightTask extends OFightBase {

	// 任务Id
	String taskId;

	// 难度
	float difficulty;

	// 攻击加成
	float atkBonus;

	// 任务编号
	int detail;

	// 消耗
	int ep;

	// 关卡等级
	int level;

	// 当前波数
	int number;

	// 敌人卡牌
	OCardBase[][] oEnemies;

	// 宝箱
	boolean[] lotBoxs;

	// 获取经验
	int lootExp;

	// 获取金钱
	int lootMoney;

	// 获得卡牌
	List<JCard> lootCards;

	// 最大敌人数量
	public static final int MAX_ENEMY_COUNT = 3;

	// 最大卡牌掉落
	public static final int MAX_DROP_COUNT = 5;

	/**
	 * 敌人卡牌
	 * 
	 * @author absir
	 * 
	 */
	public class OCardEnemy extends OCardBase {

		/** drop */
		private boolean drop;

		/**
		 * @param id
		 * @param card
		 */
		OCardEnemy(Integer id, JCard card) {
			super(id, card);
			// TODO Auto-generated constructor stub
			// paused = 3;
			hp = (int) getBuffAttP("hp", baseHp(), difficulty);
			maxHp = (int) getBuffAttP("maxHp", baseHp(), difficulty);
			atk = (int) getBuffAttP("atk", baseAtk(), atkBonus * difficulty);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.game.value.OCard#inTarget()
		 */
		@Override
		public boolean inTarget() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.client.context.value.OFightBase.OCardBase#atk(com
		 * .absir.appserv.client.context.value.OFightBase.OCardBase, long,
		 * com.absir.appserv.game.value.OResult)
		 */
		@Override
		protected void atk(OCardBase target, long time, IResult result) {
			super.atk(target, time, result);
			// paused = 3;
			// normalAtk(target, time, result);
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
			if (drop) {
				drop = false;
				addReportDetail(null, "drop", null);
			}
		}
	}

	/**
	 * 任务进度Id
	 * 
	 * @param playerContext
	 * @param taskId
	 * @param scene
	 * @param pass
	 * @param detail
	 * @param taskDefine
	 * @param taskPass
	 * @param taskDetail
	 */
	public OFightTask(PlayerContext playerContext, String taskId, int scene, int pass, int detail, XTaskDefine taskDefine, TaskPass taskPass, TaskDetail taskDetail) {
		super(playerContext);
		this.taskId = taskId;
		this.difficulty = 1.0f + taskDetail.getDifficulty();
		this.detail = detail;
		atkBonus = 0.5f + taskDetail.getAtkBonus();
		initialize(taskDefine, taskPass, taskDetail);
	}

	/**
	 * 初始化战斗数据
	 * 
	 * @param taskDefine
	 * @param taskPass
	 * @param taskDetail
	 * @param playerContext
	 */
	private void initialize(XTaskDefine taskDefine, TaskPass taskPass, TaskDetail taskDetail) {
		ep = taskDetail.getEp();
		// 玩家卡牌
		List<JCard> jCards = playerContext.getPlayer().getCards();
		int cardLength = jCards.size();
		int playerCardLength = cardLength;
		cards = new OCardPlayer[cardLength];
		for (int i = 0; i < cardLength; i++) {
			cards[i] = new OCardPlayer(this, i, jCards.get(i));
		}

		// 题目难度
		level = taskDefine.getLevel() + detail;

		// 掉落卡牌
		int maxDrop = 1 + HelperRandom.nextInt(MAX_DROP_COUNT);
		List<OCardEnemy> dropCards = new ArrayList<OCardEnemy>(maxDrop);
		float[] dropChances = new float[maxDrop];

		// 小怪卡牌
		cardLength = PlayerContext.PLAYER_CONFIGURE.isTaskOnce() ? 0 : taskDetail.getNumber();
		oEnemies = new OCardBase[cardLength + 1][];
		int enemyCount = 0;
		for (int i = 0; i < cardLength; i++) {
			int currectEnemyCount = 1 + HelperRandom.nextInt(MAX_ENEMY_COUNT);
			enemyCount += currectEnemyCount;
			OCardEnemy[] oCards = new OCardEnemy[currectEnemyCount];
			oEnemies[i] = oCards;
			for (int j = 0; j < currectEnemyCount; j++) {
				JCard card = generate(taskDetail.getEnemies()[HelperRandom.nextInt(taskDetail.getEnemies().length)], HelperRandom.nextInt(taskDetail.getLevel(), taskDetail.getMaxLevel()), 0);
				// card.setCardDefine(taskDetail.getEnemies()[HelperRandom.nextInt(taskDetail.getEnemies().length)]);
				// JPlayerContext.modifyCardLevel(card,
				// HelperRandom.nextInt(taskDetail.getLevel(),
				// taskDetail.getMaxLevel()));
				oCards[j] = new OCardEnemy(playerCardLength + j, card);
				dropCards(oCards[j], dropCards, dropChances, maxDrop);
			}
		}

		// Boss卡牌
		int bossLength = taskDetail.getBosses().length;
		OCardEnemy[] oCards = new OCardEnemy[bossLength];
		oEnemies[cardLength] = oCards;
		for (int j = 0; j < bossLength; j++) {
			JCard card = generate(taskDetail.getBosses()[j], taskDetail.getMaxLevel(), 1);
			// card.setCardDefine(taskDetail.getBosses()[j]);
			// JPlayerContext.modifyCardLevel(card, taskDetail.getMaxLevel());
			oCards[j] = new OCardEnemy(playerCardLength + j, card);
			dropCards(oCards[j], dropCards, dropChances, maxDrop);
		}

		// 遇到宝箱
		float enemyPercent = cardLength == 0 ? 0 : cardLength * MAX_ENEMY_COUNT / (float) enemyCount;
		lotBoxs = new boolean[cardLength + 1];
		int lotCount = taskDetail.getLotCount();
		if (lotCount > 0) {
			lotCount = (int) (HelperRandom.nextInt(taskDetail.getLotCount() + 1) * enemyPercent);
			if (lotCount > 1) {
				for (int i = 1; i < lotCount; i++) {
					lotBoxs[HelperRandom.nextInt(lotBoxs.length)] = true;
				}
			}
		}

		// 战利品
		lootExp = taskDetail.getExp() + (int) ((taskDetail.getMaxExp() - taskDetail.getExp()) * enemyPercent);
		lootMoney = taskDetail.getMoney() + (int) ((taskDetail.getMaxMoney() - taskDetail.getMoney()) * enemyPercent);
		cardLength = dropCards.size();
		lootCards = new ArrayList<JCard>(cardLength);
		for (int i = 0; i < cardLength; i++) {
			lootCards.add(dropCards.get(i).getCard());
		}
	}

	/**
	 * @param cardDefine
	 * @param level
	 * @param min
	 * @return
	 */
	public JCard generate(XCardDefine cardDefine, int level, int min) {
		JCard card = FightServiceUtils.generate(cardDefine, playerContext);
		FightServiceUtils.gain(card);
		PlayerContext.modifyCardLevel(card, level);
		// 自带技能
		int skillm = (int) (cardDefine.getMaxSkill() * (float) (level + 5) / cardDefine.getMaxLevel() * difficulty);
		if (skillm > 1 || min >= 1) {
			skillm = (skillm < 1 ? 0 : HelperRandom.nextInt((int) skillm)) + min;
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

		return card;
	}

	/**
	 * 卡牌掉落
	 * 
	 * @param oCard
	 * @param dropCards
	 */
	private void dropCards(OCardEnemy oCard, List<OCardEnemy> dropCards, float[] dropChanges, int maxDrop) {
		float dropChange = HelperRandom.RANDOM.nextFloat();
		if (dropChange < oCard.getCard().getCardDefine().getDrop()) {
			oCard.drop = true;
			int size = dropCards.size();
			if (size < maxDrop) {
				dropCards.add(oCard);
				dropChanges[size] = dropChange;

			} else {
				for (int i = 0; i < maxDrop; i++) {
					if (dropChanges[i] < dropChange) {
						dropCards.remove(i).drop = false;
						dropCards.add(oCard);
						dropChanges[i] = dropChange;
						return;
					}
				}

				dropCards.remove(0).drop = false;
				dropCards.add(oCard);
				dropChanges[maxDrop - 1] = dropChange;
			}
		}
	}

	/**
	 * 敌人总波数
	 * 
	 * @return
	 */
	public int getEnemiesLength() {
		return oEnemies.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OFight#getTargetCards()
	 */
	@Override
	public OCardBase[] getTargetCards() {
		// TODO Auto-generated method stub
		if (number >= oEnemies.length) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		return oEnemies[number];
	}

	/**
	 * @return
	 */
	public boolean getLotBox() {
		return lotBoxs == null ? false : lotBoxs[number];
	}

	/**
	 * 开启宝箱
	 */
	public synchronized int openLotBox() {
		if (!lotBoxs[number]) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		lotBoxs[number] = false;
		int money = HelperRandom.nextInt(100, 3000);
		playerContext.modifyMoney(money);
		return money;
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
		if (report.getResult() == EResult.VICTORY) {
			while (++number < oEnemies.length) {
				if (getTargetCards().length <= 0) {
					continue;
				}

				report.setResult(EResult.DONE);
				Map<String, Object> comparedMap = new HashMap<String, Object>();
				comparedMap.put("targetCards", getTargetCards());
				comparedMap.put("campQuestions", getCampQuestions());
				comparedMap.put("lotBox", getLotBox());
				report.setResultData(comparedMap);
				break;
			}

			if (report.getResult() != EResult.DONE) {
				ContextMap contextMap = new ContextMap(playerContext.getPlayer());
				playerContext.modifyEp(-ep);
				playerContext.modifyExp(lootExp);
				playerContext.modifyMoney(lootMoney);
				for (JCard lootCard : lootCards) {
					if (playerContext.gainCard(lootCard) == null) {
						break;
					}
				}

				playerContext.countCardNumber();
				Map<String, Object> comparedMap = contextMap.comparedMap();
				comparedMap.put("lootExp", lootExp);
				comparedMap.put("lootMoney", lootMoney);
				comparedMap.put("lootCards", lootCards);
				playerContext.taskComplete(taskId, detail);
				report.setResultData(comparedMap);
			}
		}

		return super.dealReportResult(report);
	}
}
