/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午4:25:42
 */
package com.absir.appserv.client.configure.xls;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.IPropPlayer;
import com.absir.appserv.configure.xls.XlsBeanUpdate;
import com.absir.appserv.configure.xls.value.XaParam;
import com.absir.appserv.system.bean.dto.EnumSerializer;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelClass;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XPropDefine extends XlsBeanUpdate<Integer> {

	@JaLang("道具名称")
	private String name;

	@JaLang("道具说明")
	private String desc;

	@JaLang("金币价格")
	private int price;

	@JaLang("宝石价格")
	private int diamond;

	@JaLang("道具等级")
	private int rare;

	@JaLang("道具阵营")
	@JsonSerialize(using = EnumSerializer.class)
	private JeCamp camp;

	@JaLang("道具类型")
	private String propType;

	@JaLang("道具参数")
	@XaParam
	private String[] propParmas;

	/** propInvoker */
	@JaLang("道具执行类")
	@JsonIgnore
	private transient Object propInvoker;

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
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the diamond
	 */
	public int getDiamond() {
		return diamond;
	}

	/**
	 * @return the rare
	 */
	public int getRare() {
		return rare;
	}

	/**
	 * @return the camp
	 */
	public JeCamp getCamp() {
		return camp;
	}

	/**
	 * @return the propType
	 */
	public String getPropType() {
		return propType;
	}

	/**
	 * @return the propParmas
	 */
	public String[] getPropParmas() {
		return propParmas;
	}

	/**
	 * @return the propInvoker
	 */
	@JsonIgnore
	public Object getPropInvoker() {
		if (propInvoker == null) {
			Class<?> propClass = KernelClass.forName(IPropPlayer.class.getPackage().getName() + ".OProp_" + propType);
			propInvoker = KernelClass.newInstance(propClass, this);
		}

		return propInvoker;
	}
}
