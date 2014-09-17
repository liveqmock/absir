/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.absir.appserv.support.developer.IField;
import com.absir.appserv.support.developer.IModel;
import com.absir.appserv.support.developer.JCrud;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.crud.BeanCrudFactory;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelList;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class EntityModel implements IModel {

	/** name */
	private JoEntity joEntity;

	/** caption */
	private String caption;

	/** update */
	private Long update;

	/** filter */
	private boolean filter;

	/** primary */
	private IField primary;

	/** primaries */
	private List<IField> primaries = new ArrayList<IField>();

	/** fields */
	private List<IField> fields = new ArrayList<IField>();

	/** jCruds */
	private List<JCrud> jCruds;

	/** fieldMap */
	private Map<String, IField> fieldMap = new HashMap<String, IField>();

	/** groups */
	private Map<String, List<IField>> groups = new HashMap<String, List<IField>>();

	/** crudCruds */
	private List<IField> crudFields = new ArrayList<IField>();

	/**
	 * @return the joEntity
	 */
	public JoEntity getJoEntity() {
		return joEntity;
	}

	/**
	 * @param joEntity
	 *            the joEntity to set
	 */
	public void setJoEntity(JoEntity joEntity) {
		this.joEntity = joEntity;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return
	 */
	public Long lastModified() {
		return update;
	}

	/**
	 * @param update
	 */
	protected void setUpdate(Long update) {
		this.update = update;
	}

	/**
	 * @return the filter
	 */
	public boolean isFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	protected void setFilter(boolean filter) {
		this.filter = filter;
	}

	/**
	 * @return the jCruds
	 */
	public List<JCrud> getjCruds() {
		return jCruds;
	}

	/**
	 * @param jaCrud
	 */
	public void addJaCrud(JaCrud jaCrud) {
		if (jCruds == null) {
			jCruds = new ArrayList<JCrud>();
		}

		for (JCrud jCrud : jCruds) {
			if (jaCrud.factory() == jCrud.getFactory() && jaCrud.value().equals(jCrud.getValue())) {
				return;
			}
		}

		jCruds.add(new JCrud(jaCrud));
	}

	/** beanJaCruded */
	private boolean beanJaCruded;

	/**
	 * 
	 */
	public void addBeanJaCrud() {
		if (!beanJaCruded) {
			beanJaCruded = true;
			JCrud beanCrud = new JCrud();
			beanCrud.setJaCrud(null, BeanCrudFactory.class, KernelLang.NULL_OBJECTS, JaCrud.ALL);
		}
	}

	/**
	 * @return
	 */
	public IField getPrimary() {
		return primary;
	}

	/**
	 * @param primary
	 */
	protected void setPrimary(DBField primary) {
		this.primary = primary;
	}

	/**
	 * @return the primaries
	 */
	public List<IField> getPrimaries() {
		return primaries;
	}

	/**
	 * @param primaries
	 *            the primaries to set
	 */
	protected void setPrimaries(List<IField> primaries) {
		this.primaries = primaries;
	}

	/**
	 * @return
	 */
	public List<IField> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 */
	protected void setFields(List<IField> fields) {
		this.fields = fields;
	}

	/**
	 * @param field
	 */
	protected void addField(IField field) {
		fields.add(field);
	}

	/**
	 * @return the fieldMap
	 */
	public IField getField(String name) {
		return fieldMap.get(name);
	}

	/**
	 * @param groups
	 */
	protected void setGroups(Map<String, List<IField>> groups) {
		this.groups = groups;
	}

	/**
	 * @param group
	 * @return
	 */
	public List<IField> getGroupFields(String group) {
		return groups.get(group);
	}

	/**
	 * @param group
	 * @param fields
	 */
	protected void setGroupFields(String group, List<IField> fields) {
		groups.put(group, fields);
	}

	/** REFERENCED_MAP */
	private static final Map<String, Set<String>> REFERENCED_MAP = new HashMap<String, Set<String>>();
	static {
		addReferencedMap(JaEdit.GROUP_SUGGEST, JaEdit.GROUP_SUG);
		addReferencedMap(JaEdit.GROUP_SUGGEST, JaEdit.GROUP_LIST);
		addReferencedMap(JaEdit.GROUP_SUGGEST, JaEdit.GROUP_SEARCH);
		addReferencedMap(JaEdit.GROUP_LIST, JaEdit.GROUP_SEARCH);
	}

	/**
	 * @param from
	 * @param to
	 */
	private static void addReferencedMap(String from, String... tos) {
		Set<String> set = REFERENCED_MAP.get(from);
		if (set == null) {
			set = new HashSet<String>();
			REFERENCED_MAP.put(from, set);
		}

		for (String to : tos) {
			set.add(to);
		}
	}

	/**
	 * @param group
	 * @param field
	 */
	protected void addGroupField(String group, IField field) {
		addGroupField(group, field, true);
	}

	/**
	 * @param group
	 * @param field
	 * @param reference
	 */
	protected void addGroupField(String group, IField field, boolean reference) {
		List<IField> fields = groups.get(group);
		if (fields == null) {
			fields = new ArrayList<IField>();
			groups.put(group, fields);
		}

		if (field != null) {
			KernelList.addOnly(fields, field);
		}

		if (reference) {
			Set<String> set = REFERENCED_MAP.get(group);
			if (set != null) {
				for (String r : set) {
					addGroupField(r, field, false);
				}
			}
		}
	}

	/**
	 * @return the crudFields
	 */
	public List<IField> getCrudFields() {
		return crudFields;
	}

	/** crudField */
	private IField crudField;

	/**
	 * @param field
	 */
	public void addCrudField(IField field) {
		crudField = field;
		crudFields.add(field);
	}

	/**
	 * @return
	 */
	public IField getCrudField() {
		return crudField;
	}

	/**
	 * 
	 */
	protected void addComplete() {
		addGroupField(JaEdit.GROUP_LIST, primary);
		for (IField parmary : primaries) {
			fieldMap.put(parmary.getName(), parmary);
			addGroupField(JaEdit.GROUP_SUGGEST, parmary);
		}

		for (IField field : fields) {
			fieldMap.put(field.getName(), field);
			if (field.getGroups() == null) {
				if ("name".equals(field.getName())) {
					addGroupField(JaEdit.GROUP_SUGGEST, field);
				}

			} else {
				for (String group : field.getGroups()) {
					addGroupField(group, field);
				}
			}
		}
	}
}
