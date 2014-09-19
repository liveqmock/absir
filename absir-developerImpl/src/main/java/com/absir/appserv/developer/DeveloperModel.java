/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-9 下午2:29:09
 */
package com.absir.appserv.developer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletRequest;

import com.absir.appserv.crud.CrudUtils;
import com.absir.appserv.developer.model.EntityModel;
import com.absir.appserv.developer.model.ModelFactory;
import com.absir.appserv.support.developer.IField;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class DeveloperModel {

	/**
	 * @param entityName
	 * @param request
	 * @return
	 */
	public static DeveloperModel forEntityName(String entityName, ServletRequest request) {
		return forEntityNameClass(CrudUtils.newJoEntity(entityName, null), request);
	}

	/**
	 * @param entityClass
	 * @param request
	 * @return
	 */
	public static DeveloperModel forEntityClass(Class<?> entityClass, ServletRequest request) {
		return forEntityNameClass(CrudUtils.newJoEntity(null, entityClass), request);
	}

	/**
	 * @param joEntity
	 * @param request
	 * @return
	 */
	public static DeveloperModel forEntityNameClass(JoEntity joEntity, ServletRequest request) {
		String persistKey = DeveloperModel.class.getName() + joEntity.toString();
		Object modelObject = request.getAttribute(persistKey);
		if (modelObject == null || !(modelObject instanceof DeveloperModel)) {
			DeveloperModel developerModel = new DeveloperModel();
			developerModel.entityName = joEntity.getEntityName();
			developerModel.entityModel = ModelFactory.getModelEntity(joEntity);
			developerModel.entitySuggests = developerModel.entityModel.getGroupFields(JaEdit.GROUP_SUGGEST);
			request.setAttribute(persistKey, developerModel);
			return developerModel;
		}

		return (DeveloperModel) modelObject;
	}

	/** entityName */
	private String entityName;

	/** entityModel */
	private EntityModel entityModel;

	/** entitySuggests */
	private List<IField> entitySuggests;

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @return the entityModel
	 */
	public EntityModel getEntityModel() {
		return entityModel;
	}

	/**
	 * @param value
	 * @return
	 */
	public String suggest(String value) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		out.println("<c:set value=\"" + value + "\" var=\"value\"/>");
		suggest(out);
		return writer.toString();
	}

	/**
	 * @param values
	 * @return
	 */
	public String suggests(String values) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		out.println("<c:set value=\"\" var=\"values\"/>");
		out.println("<c:set value=\"\" var=\"ids\"/>");
		out.println("<c:forEach items='" + values + "' var=\"value\" varStatus=\"status\">");
		out.println("<c:if test=\"${status.index > 0}\">");
		out.println("<c:set value=\"${ids},\" var=\"ids\"/>");
		out.println("<c:set value=\"${values},\" var=\"values\"/>");
		out.println("</c:if>");
		suggest(out);
		out.println("<c:set value=\"${ids}${id}\" var=\"ids\"/>");
		out.println("<c:set value=\"${values}${value}\" var=\"values\"/>");
		out.print("</c:forEach>");
		return writer.toString();
	}

	/**
	 * @param out
	 */
	private void suggest(PrintWriter out) {
		String values = "";
		int size = entitySuggests == null ? 0 : entitySuggests.size();
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				values += ".";
			}

			values += "${value." + entitySuggests.get(i).getName() + "}";
		}

		out.println("<c:set value=\"${value." + entityModel.getPrimary().getName() + "}\" var=\"id\"/>");
		out.println("<c:set value=\"" + values + "\" var=\"value\"/>");
	}
}
