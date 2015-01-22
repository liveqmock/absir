/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-22 下午4:50:38
 */
package com.absir.appserv.system.bean;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.feature.menu.IMenuBean;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.feature.menu.value.MeUrlType;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.proxy.JiTree;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("菜单管理") }, name = "菜单")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JMenu extends JbBean implements IMenuBean, JiTree<JMenu> {

	@JaLang(value = "父级菜单", tag = "parentMenu")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@ManyToOne
	private JMenu parent;

	@JaLang(value = "菜单名称", tag = "menuName")
	private String name;

	@JaLang("类型")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String type;

	@JaLang("排序")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private int ordinal;

	@JaLang("链接")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String url;

	@JaLang("标注")
	private String ref;

	@JaLang("链接类型")
	private MeUrlType urlType;

	@JaLang("图标")
	@JaEdit(types = "icon", metas = "{type:menu}")
	private String icon;

	@JaLang(value = "子级菜单", tag = "subMenu")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(mappedBy = "parent")
	@OrderBy("ordinal")
	private List<JMenu> children;

	/**
	 * @return the parent
	 */
	public JMenu getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(JMenu parent) {
		this.parent = parent;
	}

	/**
	 * @return the name
	 */
	@Langs
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal
	 *            the ordinal to set
	 */
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref
	 *            the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	/**
	 * @return the urlType
	 */
	public MeUrlType getUrlType() {
		return urlType;
	}

	/**
	 * @param urlType
	 *            the urlType to set
	 */
	public void setUrlType(MeUrlType urlType) {
		this.urlType = urlType;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the children
	 */
	public List<JMenu> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<JMenu> children) {
		this.children = children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return ordinal;
	}
}
