/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-15 下午12:50:20
 */
package com.absir.appserv.game.utils;

import java.util.List;

import com.absir.appserv.game.value.EResult;
import com.absir.appserv.game.value.IExp;
import com.absir.appserv.game.value.ILevel;
import com.absir.appserv.game.value.ILevelExp;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.LevelCxt;
import com.absir.appserv.game.value.LevelExpCxt;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class GameUtils {

	/** LEVEL_EXP_CXT */
	static final LevelExpCxt LEVEL_EXP_CXT = new LevelExpCxt();

	/**
	 * 等级分布
	 * 
	 * @param att
	 * @param maxAtt
	 * @param maxLevel
	 * @return
	 */
	public static int[] atts(int att, int maxAtt, int maxLevel) {
		float iAtt = maxAtt / (float) (maxLevel - 1);
		int[] atts = new int[maxLevel + 1];
		for (int i = 0; i < maxLevel; i++) {
			atts[i + 1] = att + (int) (i * iAtt);
		}

		return atts;
	}

	/**
	 * 经验升级
	 * 
	 * @param exp
	 * @param iLevel
	 * @param iExps
	 * @param maxLevel
	 * @return
	 */
	public static int doExp(int exp, ILevel iLevel, List<? extends IExp> iExps, int maxLevel) {
		return doExp(exp, iLevel, LEVEL_EXP_CXT, iExps, maxLevel);
	}

	/**
	 * 经验升级
	 * 
	 * @param exp
	 * @param obj
	 * @param levelCxt
	 * @param iExps
	 * @param maxLevel
	 * @return
	 */
	public static <T extends ILevel> int doExp(int exp, T obj, LevelCxt<T> levelCxt, List<? extends IExp> iExps, int maxLevel) {
		int level = levelCxt.getLevel(obj);
		while (true) {
			IExp iExp = iExps.get(level);
			if (exp < iExp.getExp()) {
				break;
			}

			exp -= iExp.getExp();
			if (++level >= maxLevel) {
				exp = 0;
				level = maxLevel;
			}
		}

		levelCxt.setLevel(obj, level);
		return exp;
	}

	/**
	 * @param iLevelExp
	 * @param iExps
	 * @param maxLevel
	 */
	public static void modifyExp(int exp, ILevelExp iLevelExp, List<? extends IExp> iExps, int maxLevel) {
		modifyExp(exp, iLevelExp, LEVEL_EXP_CXT, iExps, maxLevel);
	}

	/**
	 * 更改经验
	 * 
	 * @param exp
	 * @param obj
	 * @param levelExpCxt
	 * @param iExps
	 * @param maxLevel
	 */
	public static <T extends ILevelExp> void modifyExp(int exp, T obj, LevelExpCxt<T> levelExpCxt, List<? extends IExp> iExps, int maxLevel) {
		if (levelExpCxt.getLevel(obj) > maxLevel) {
			return;
		}

		exp += obj.getExp();
		if (exp <= 0) {
			exp = 0;

		} else {
			levelExpCxt.setExp(obj, doExp(exp, obj, levelExpCxt, iExps, maxLevel));
		}
	}

	/**
	 * 反转战斗结果
	 * 
	 * @param result
	 */
	public static void revert(IResult result) {
		if (result.getResult() == EResult.VICTORY) {
			result.setResult(EResult.LOSS);

		} else if (result.getResult() == EResult.LOSS) {
			result.setResult(EResult.VICTORY);
		}
	}
}
