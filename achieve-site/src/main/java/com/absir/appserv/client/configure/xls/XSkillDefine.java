/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-17 上午9:59:06
 */
package com.absir.appserv.client.configure.xls;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OSkillNs;
import com.absir.appserv.configure.xls.XlsBeanUpdate;
import com.absir.appserv.configure.xls.value.XaParam;
import com.absir.appserv.system.bean.dto.EnumSerializer;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XSkillDefine extends XlsBeanUpdate<Long> {

	@JaLang("技能名称")
	private String name;

	@JaLang("技能描述")
	private String desc;

	@JaLang("阵营")
	@JsonSerialize(using = EnumSerializer.class)
	private JeCamp camp;

	@JaLang("技能等级")
	private int rare;

	@JaLang("冷却回合")
	private int tick;

	@JaLang("效果类型")
	private String effectType;

	@JaLang("效果参数")
	@XaParam
	private String[] effectParameters;

	@JaLang("目标类型")
	private int targetType;

	@JaLang("目标数量")
	private int targetCount;

	@JaLang("目标阵营")
	@XaParam
	@JsonSerialize(contentUsing = EnumSerializer.class)
	private JeCamp[] targetCamps;

	/** oSkillNs */
	private transient OSkillNs oSkillNs;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the camp
	 */
	public JeCamp getCamp() {
		return camp;
	}

	/**
	 * @return the rare
	 */
	public int getRare() {
		return rare;
	}

	/**
	 * @return the tick
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * @return the effectType
	 */
	public String getEffectType() {
		return effectType;
	}

	/**
	 * @return the effectParameters
	 */
	public String[] getEffectParameters() {
		return effectParameters;
	}

	/**
	 * @return the targetType
	 */
	public int getTargetType() {
		return targetType;
	}

	/**
	 * @return the targetCount
	 */
	public int getTargetCount() {
		return targetCount;
	}

	/**
	 * @return the targetCamps
	 */
	public JeCamp[] getTargetCamps() {
		return targetCamps;
	}

	/**
	 * @param targetCamps
	 */
	protected void setTargetCamps(JeCamp[] targetCamps) {
		this.targetCamps = targetCamps;

	}

	/**
	 * @return
	 */
	@JsonIgnore
	public OSkillNs getOSkillNs() {
		if (oSkillNs == null) {
			synchronized (this) {
				if (oSkillNs == null) {
					oSkillNs = new OSkillNs(this);
				}
			}
		}

		return oSkillNs;
	}
}
