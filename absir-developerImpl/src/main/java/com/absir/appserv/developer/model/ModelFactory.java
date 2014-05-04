/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.absir.appserv.support.Developer;
import com.absir.appserv.support.developer.IField;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaEditor;
import com.absir.appserv.system.bean.value.JaEditors;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.appserv.system.helper.HelperLang;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelReflect;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class ModelFactory {

	/** Jo_Entity_Map_Entity_Model */
	private static final Map<JoEntity, EntityModel> Jo_Entity_Map_Entity_Model = new HashMap<JoEntity, EntityModel>();

	/**
	 * @param entityName
	 * @return
	 */
	public static EntityModel getModelEntity(String entityName) {
		return getModelEntity(new JoEntity(entityName, null));
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static EntityModel getModelEntity(Class<?> entityClass) {
		return getModelEntity(new JoEntity(null, entityClass));
	}

	/**
	 * @param joEntity
	 * @return
	 */
	public static EntityModel getModelEntity(JoEntity joEntity) {
		if (joEntity.getEntityClass() == null) {
			return null;
		}

		EntityModel entityModel = Jo_Entity_Map_Entity_Model.get(joEntity);
		if (entityModel == null) {
			synchronized (joEntity.getEntityToken()) {
				entityModel = Jo_Entity_Map_Entity_Model.get(joEntity);
				if (entityModel == null) {
					entityModel = generateModelEntity(joEntity);

					if (entityModel != null) {
						Jo_Entity_Map_Entity_Model.put(joEntity, entityModel);
					}
				}
			}
		}

		return entityModel;
	}

	/**
	 * @param joEntity
	 * @return
	 */
	protected static EntityModel generateModelEntity(JoEntity joEntity) {
		if (joEntity.getClass() == null) {
			return null;

		} else {
			return getModelEntityClass(joEntity, new EntityModel());
		}
	}

	/**
	 * @param joEntity
	 * @param entityModel
	 * @return
	 */
	private static EntityModel getModelEntityClass(final JoEntity joEntity, final EntityModel entityModel) {
		if (BeanFactoryUtils.getEnvironment() != Environment.DEVELOP) {
			entityModel.setUpdate(Developer.lastModified(joEntity));
		}

		entityModel.setJoEntity(joEntity);
		entityModel.setCaption(HelperLang.getTypeCaption(joEntity.getEntityClass(), joEntity.getEntityName()));
		final String identifierName = SessionFactoryUtils.getIdentifierName(joEntity.getEntityName(), joEntity.getEntityClass());
		final List<IField> iFieldList = new ArrayList<IField>();
		final List<IField> iFieldScope = new ArrayList<IField>();
		final List<JaCrud> jaCrudList = new ArrayList<JaCrud>();
		final List<JaCrud> jaCrudScope = new ArrayList<JaCrud>();
		final Map<String, JaEditor> iEditors = new HashMap<String, JaEditor>();
		JaEditors jaEditors = joEntity.getEntityClass().getAnnotation(JaEditors.class);
		if (jaEditors != null) {
			for (JaEditor editor : jaEditors.editors()) {
				iEditors.put(editor.name(), editor);
			}
		}

		KernelReflect.doWithClasses(joEntity.getEntityClass(), new CallbackBreak<Class<?>>() {

			@Override
			public void doWith(Class<?> template) throws BreakException {

				for (Field field : template.getDeclaredFields()) {
					// TODO Auto-generated method stub
					if (isTransientField(field)) {
						continue;
					}

					EntityField entityField = null;
					if (field.getAnnotation(Id.class) != null || field.getName().equals(identifierName)) {
						entityField = new EntityField(joEntity, field);
						entityModel.setPrimary(entityField);
						entityField.addEntityFieldScope(entityModel.getPrimaries());

					} else if (!isMappedByField(field)) {
						entityField = new EntityField(joEntity, field);
						entityField.addEntityFieldScope(iFieldScope);

					}

					if (entityField == null || entityField.getEditable() != JeEditable.ENABLE) {
						entityModel.setFilter(true);
					}

					if (entityField != null) {
						if (entityField.getEditable() == JeEditable.ENABLE) {
							entityModel.addGroupField("editable", entityField);

						} else if (entityField.getEditable() == JeEditable.OPTIONAL) {
							entityModel.addGroupField("optional", entityField);
						}

						JaEditor editor = iEditors.get(entityField.getName());
						if (editor != null) {
							if (editor.edit().length > 0) {
								entityField.setJaEdit(editor.edit()[0]);
							}

							if (editor.crud().length > 0) {
								entityField.setJaCrud(editor.crud()[0]);
							}

							if (editor.classes().length > 0) {
								entityField.setJaClasses(editor.classes()[0]);
							}

							if (editor.names().length > 0) {
								entityField.setJaNames(editor.names()[0]);
							}
						}

						// 关联实体名称
						entityField.initEntiyName();
					}
				}

				iFieldList.addAll(0, iFieldScope);
				iFieldScope.clear();

				for (Class<?> cls : template.getInterfaces()) {
					JaCrud jaCrud = cls.getAnnotation(JaCrud.class);
					if (jaCrud != null) {
						jaCrudScope.add(jaCrud);
					}
				}

				JaCrud jaCrud = template.getAnnotation(JaCrud.class);
				if (jaCrud != null) {
					jaCrudScope.add(jaCrud);
				}

				jaCrudList.addAll(0, jaCrudScope);
				jaCrudScope.clear();
			}
		});

		entityModel.addGroupField(JaEdit.GROUP_LIST, entityModel.getPrimary());
		// KernelList.sortOrderable(iFieldList);
		for (IField iField : iFieldList) {
			entityModel.addField(iField);
			if (iField.getGroups() == null) {
				if ("name".equals(iField.getName())) {
					entityModel.addGroupField(JaEdit.GROUP_SUGGEST, iField);
				}

			} else {
				for (String group : iField.getGroups()) {
					entityModel.addGroupField(group, iField);
				}
			}
		}

		for (JaCrud jaCrud : jaCrudList) {
			entityModel.addJaCrud(jaCrud);
		}

		return entityModel;
	}

	/**
	 * @param field
	 * @return
	 */
	private static boolean isTransientField(Field field) {
		int modifier = field.getModifiers();
		if (Modifier.isStatic(modifier)) {
			return true;
		}

		JaEdit jaEdit = field.getAnnotation(JaEdit.class);
		if (jaEdit == null) {
			return Modifier.isTransient(modifier) || field.getAnnotation(Transient.class) != null || (!Modifier.isPublic(modifier) && KernelClass.setter(field) == null);

		} else {
			return jaEdit.editable() == JeEditable.DISABLE;
		}
	}

	/**
	 * @param field
	 * @return
	 */
	private static boolean isMappedByField(Field field) {
		OneToMany oneToMany = field.getAnnotation(OneToMany.class);
		ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
		if ((oneToMany == null || oneToMany.mappedBy().isEmpty()) && (manyToMany == null || manyToMany.mappedBy().isEmpty())) {
			return false;
		}

		JaEdit jaEdit = field.getAnnotation(JaEdit.class);
		if (jaEdit != null && jaEdit.types().length > 0) {
			return false;
		}

		return true;
	}
}
