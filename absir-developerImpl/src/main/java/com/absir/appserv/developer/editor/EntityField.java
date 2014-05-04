/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-4 上午10:20:38
 */
package com.absir.appserv.developer.editor;

import java.util.Map;
import java.util.Map.Entry;

import com.absir.appserv.developer.model.DBField;
import com.absir.appserv.support.developer.JCrud;
import com.absir.core.kernel.KernelString;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.property.Property;

/**
 * @author absir
 * 
 */
public class EntityField extends DBField {

	/**
	 * @param name
	 * @param property
	 * @param editorObject
	 */
	public EntityField(String name, Property property, EditorObject editorObject) {
		// set properties
		crudField.setName(name);
		crudField.setType(property.getType());
		generated = editorObject.isGenerated();
		caption = editorObject.getLang();
		property.getAccessor().getAnnotation(annotationClass, true);
		// set entityname
		valueEntityName = editorObject.getValueName();
		if (KernelString.isEmpty(valueEntityName)) {
			valueEntityName = editorObject.getValueClass() == null ? null : SessionFactoryUtils.getJpaEntityName(editorObject.getValueClass());
		}

		if (Map.class.isAssignableFrom(property.getType())) {
			entityName = editorObject.getKeyName();
			if (KernelString.isEmpty(entityName)) {
				entityName = editorObject.getKeyClass() == null ? null : SessionFactoryUtils.getJpaEntityName(editorObject.getKeyClass());
			}

		} else {
			entityName = valueEntityName;
		}

		// set edit

		// set crud
		if (editorObject.getCrud() != null || editorObject.getCrudValue() != null) {
			JCrud jCrud = crudField.getjCrud();
			if (jCrud == null) {
				jCrud = new JCrud();
				crudField.setjCrud(jCrud);
			}

			jCrud.setJaCrud(editorObject.getCrud());
			jCrud.setJaCrudValue(editorObject.getCrudValue());
			if ((KernelString.isEmpty(jCrud.getValue()) && (jCrud.getFactory() == null || jCrud.getFactory() == void.class)) || (jCrud.getCruds() == null || jCrud.getCruds().length == 0)) {
				crudField.setjCrud(null);
			}
		}

		// set metas
		if (editorObject.getMetas() != null) {
			for (Entry<Object, Object> entry : editorObject.getMetas().entrySet()) {
				metas.put(entry.getKey().toString(), entry.getValue());
			}
		}
	}
}
