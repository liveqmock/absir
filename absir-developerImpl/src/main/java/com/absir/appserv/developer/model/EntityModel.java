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

	/** groups */
	private Map<String, List<IField>> groups = new HashMap<String, List<IField>>();

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
		// KernelList(fields, field);
	}

	/**
	 * @return
	 */
	public Map<String, List<IField>> getGroups() {
		return groups;
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
			// KernelList.addOrderOnly(fields, field);
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
}
