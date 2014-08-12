/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-24 下午12:15:53
 */
package com.absir.appserv.client.configure.xls;

import com.absir.appserv.configure.xls.XlsBeanUpdate;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XStoryDefine extends XlsBeanUpdate<Long> {

	/**
	 * @author absir
	 * 
	 */
	public static class StoryDetail {

		@JaLang("头像")
		private String photo;

		@JaLang("阵营")
		private boolean target;

		@JaLang("对话")
		private String desc;

		@JaLang("标示")
		private int tag;

		/**
		 * @return the photo
		 */
		public String getPhoto() {
			return photo;
		}

		/**
		 * @return the target
		 */
		public boolean isTarget() {
			return target;
		}

		/**
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}

		/**
		 * @return the tag
		 */
		public int getTag() {
			return tag;
		}
	}

	@JaLang("详细剧情")
	private StoryDetail[] storyDetails;

	/**
	 * @return the storyDetails
	 */
	public StoryDetail[] getStoryDetails() {
		return storyDetails;
	}
}
