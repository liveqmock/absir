/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月21日 下午4:26:59
 */
package com.absir.appserv.game.bean.value;

/**
 * @author absir
 *
 */
public interface ITaskDefine {

	/**
	 * 任务等级
	 * 
	 * @return
	 */
	public int getLevel();

	/**
	 * 获取关卡列表
	 * 
	 * @return
	 */
	public ITaskPass[] getTaskPasses();

	/**
	 * @author absir
	 *
	 */
	public interface ITaskPass {

		/**
		 * 获取任务列表
		 * 
		 * @return
		 */
		public ITaskDetail[] getTaskDetails();
	}

	/**
	 * @author absir
	 *
	 */
	public interface ITaskDetail {

		/**
		 * 消耗精力
		 * 
		 * @return
		 */
		public int getEp();

	}
}
