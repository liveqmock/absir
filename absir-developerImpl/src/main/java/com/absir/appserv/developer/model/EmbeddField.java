/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-18 上午11:40:51
 */
package com.absir.appserv.developer.model;

import java.util.List;
import java.util.Map;

import com.absir.appserv.support.developer.IField;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.value.JeEditable;

/**
 * @author absir
 * 
 */
public class EmbeddField implements IField {

	/** field */
	private IField field;

	/** name */
	private String name;

	/** order */
	private int order;

	/**
	 * @param field
	 * @param name
	 * @param order
	 */
	public EmbeddField(IField field, String name, int order) {
		this.field = field;
		this.name = name + "." + field.getName();
		this.order = order + field.getOrder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getCrudField()
	 */
	@Override
	public JCrudField getCrudField() {
		// TODO Auto-generated method stub
		return field.getCrudField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getType()
	 */
	@Override
	public Class<?> getType() {
		// TODO Auto-generated method stub
		return field.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getValueField()
	 */
	@Override
	public IField getValueField() {
		// TODO Auto-generated method stub
		return field.getValueField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getEntityName()
	 */
	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return field.getEntityName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getValueEntityName()
	 */
	@Override
	public String getValueEntityName() {
		// TODO Auto-generated method stub
		return field.getValueEntityName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getCaption()
	 */
	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return field.getCaption();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getGroups()
	 */
	@Override
	public String[] getGroups() {
		// TODO Auto-generated method stub
		return field.getGroups();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#isGenerated()
	 */
	@Override
	public boolean isGenerated() {
		// TODO Auto-generated method stub
		return field.isGenerated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#isCanOrder()
	 */
	@Override
	public boolean isCanOrder() {
		// TODO Auto-generated method stub
		return field.isCanOrder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#isNullable()
	 */
	@Override
	public boolean isNullable() {
		// TODO Auto-generated method stub
		return field.isNullable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#isCollection()
	 */
	@Override
	public boolean isCollection() {
		// TODO Auto-generated method stub
		return field.isCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getMappedBy()
	 */
	@Override
	public String getMappedBy() {
		// TODO Auto-generated method stub
		return field.getMappedBy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getTypes()
	 */
	@Override
	public List<String> getTypes() {
		// TODO Auto-generated method stub
		return field.getTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getEditable()
	 */
	@Override
	public JeEditable getEditable() {
		// TODO Auto-generated method stub
		return field.getEditable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getMetas()
	 */
	@Override
	public Map<String, Object> getMetas() {
		// TODO Auto-generated method stub
		return field.getMetas();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IField#getDefaultEntity()
	 */
	@Override
	public Object getDefaultEntity() {
		// TODO Auto-generated method stub
		return field.getDefaultEntity();
	}
}
