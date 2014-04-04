/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.absir.appserv.support.developer.IField;
import com.absir.appserv.support.developer.JCrud;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaSubField;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.appserv.system.helper.HelperLang;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAnnotation;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.hibernate.boost.EntityAssoc.EntityAssocEntity;
import com.absir.orm.value.JaClasses;
import com.absir.orm.value.JaNames;
import com.absir.orm.value.JiAssoc;
import com.absir.orm.value.JiRelation;
import com.absir.orm.value.JoEntity;
import com.absir.validator.value.Digits;
import com.absir.validator.value.Email;
import com.absir.validator.value.Length;
import com.absir.validator.value.Max;
import com.absir.validator.value.Min;
import com.absir.validator.value.NotEmpty;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityField extends DBField {

	/** field */
	protected Field field;

	/**
	 * 
	 */
	public EntityField() {
	}

	/**
	 * @param joEntity
	 */
	public EntityField(JoEntity joEntity, Field field) {
		this.field = field;
		this.caption = HelperLang.getFieldCaption(field, joEntity.getEntityClass());

		JaSubField jaSubField = field.getAnnotation(JaSubField.class);
		if (jaSubField != null) {
			metas.put("subField", jaSubField.value());
		}

		generated = field.getAnnotation(GeneratedValue.class) != null || metas.containsKey("generated");
		crudField.setName(field.getName());
		crudField.setType(field.getType());

		ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
		if (manyToMany != null) {
			mappedBy = manyToMany.mappedBy();
		}

		OneToMany oneToMany = field.getAnnotation(OneToMany.class);
		if (oneToMany != null) {
			mappedBy = oneToMany.mappedBy();
		}

		/*
		 * ManyToOne manyToOne = field.getAnnotation(ManyToOne.class); if
		 * (manyToOne != null) { mappedBy = manyToOne.mappedBy(); }
		 */

		crudReference();
		initJoEntity(joEntity);
		initValidate();

		setJaEdit(field.getAnnotation(JaEdit.class));
		setJaCrud(field.getAnnotation(JaCrud.class));
		setJaClasses(field.getAnnotation(JaClasses.class));
		setJaNames(field.getAnnotation(JaNames.class));
	}

	/**
	 * @param jaEdit
	 */
	public void setJaEdit(JaEdit jaEdit) {
		if (jaEdit != null) {
			UtilAnnotation.copy(jaEdit, this);
			if (jaEdit.types().length > 0) {
				types.addAll(0, KernelArray.toList(jaEdit.types()));
			}

			Object metasObject = HelperJson.decodeMap(jaEdit.metas());
			if (metasObject != null) {
				KernelObject.copy(metasObject, metas);
			}
		}
	}

	/**
	 * @param jaCrud
	 */
	public void setJaCrud(JaCrud jaCrud) {
		if (jaCrud != null) {
			crudField.setjCrud(new JCrud(jaCrud));
		}
	}

	/**
	 * @param jaClasses
	 */
	public void setJaClasses(JaClasses jaClasses) {
		if (jaClasses != null) {
			if (Map.class.isAssignableFrom(field.getType())) {
				if (jaClasses.key() != void.class) {
					entityName = SessionFactoryUtils.getJpaEntityName(jaClasses.key());
				}

				if (jaClasses.value() != void.class) {
					valueEntityName = SessionFactoryUtils.getJpaEntityName(jaClasses.value());
				}

			} else {
				if (jaClasses.value() != void.class) {
					entityName = SessionFactoryUtils.getJpaEntityName(jaClasses.value());
				}
			}
		}
	}

	/**
	 * @param jaNames
	 */
	public void setJaNames(JaNames jaNames) {
		if (jaNames != null) {
			if (Map.class.isAssignableFrom(field.getType())) {
				if (!KernelString.isEmpty(jaNames.key())) {
					entityName = jaNames.key();
				}

				if (!KernelString.isEmpty(jaNames.value())) {
					valueEntityName = jaNames.value();
				}

			} else {
				if (!KernelString.isEmpty(jaNames.value())) {
					entityName = jaNames.value();
				}
			}
		}
	}

	/**
	 * 
	 */
	public void initEntiyName() {
		if (crudField.getJoEntity() == null && !KernelString.isEmpty(entityName) && !Map.class.isAssignableFrom(field.getType())) {
			if (field.getType().isArray()) {
				types.add("pselects");

			} else if (Collection.class.isAssignableFrom(field.getType())) {
				canOrder = false;
				types.add("iselects");

			} else {
				types.add("iselect");
			}
		}
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 
	 */
	private void crudReference() {
		if (field.getAnnotation(ElementCollection.class) != null) {
			crudField.setCruds(JaCrud.ALL);
			return;
		}

		for (Class<Annotation> annotationClass : CRUD_ANNOTATION_CLASSES) {
			Object annotation = field.getAnnotation(annotationClass);
			if (annotation != null) {
				CascadeType[] cascadeTypes = (CascadeType[]) KernelObject.send(annotation, "cascade");
				if (cascadeTypes.length > 0) {
					Set<Crud> cruds = new HashSet<Crud>();
					for (CascadeType cascadeType : cascadeTypes) {
						if (cascadeType == CascadeType.ALL) {
							crudField.setCruds(JaCrud.ALL);
							return;

						} else if (cascadeType == CascadeType.PERSIST) {
							cruds.add(Crud.CREATE);

						} else if (cascadeType == CascadeType.MERGE) {
							cruds.add(Crud.UPDATE);

						} else if (cascadeType == CascadeType.REMOVE) {
							cruds.add(Crud.DELETE);
						}
					}

					if (cruds.size() > 0) {
						crudField.setCruds(KernelCollection.toArray(cruds, Crud.class));
					}
				}

				return;
			}
		}

		crudField.setCruds(JaCrud.ALL);
	}

	/** CRUD_ANNOTATION_CLASSES */
	private static final Class<Annotation>[] CRUD_ANNOTATION_CLASSES = new Class[] { ManyToMany.class, OneToMany.class, ManyToOne.class, OneToOne.class };

	/**
	 * @param cls
	 * @return
	 */
	private boolean typeFieldType(Class cls) {
		if (Boolean.class.isAssignableFrom(cls) || boolean.class.isAssignableFrom(cls)) {
			types.add("option");

		} else if (Enum.class.isAssignableFrom(cls)) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			Enum[] enums = ((Class<? extends Enum>) cls).getEnumConstants();
			for (Enum enumerate : enums) {
				map.put(enumerate.name(), HelperLang.getEnumNameCaption(enumerate));
			}

			types.add("enum");
			metas.put("values", map);

		} else if (Date.class.isAssignableFrom(cls)) {
			types.add("date");

		} else {
			return KernelClass.isBasicClass(cls);
		}

		return true;
	}

	/**
	 * @param joEntity
	 */
	private void initJoEntity(JoEntity joEntity) {
		Class cls = field.getType();
		if (!typeFieldType(cls)) {
			if (Map.class.isAssignableFrom(cls)) {
				canOrder = false;
				Class[] rawClasses = KernelClass.rawClasses(KernelClass.typeArguments(field.getGenericType()));
				String[] entityNames = SessionFactoryUtils.getReferencedJpaEntityNames(joEntity, field);
				entityName = entityNames[1];
				valueEntityName = entityNames[0];
				if (entityNames[0] != null) {
					crudField.setJoEntity(new JoEntity(entityNames[0], null));

				} else if (rawClasses.length > 1 && rawClasses[1] != null && KernelClass.isCustomClass(rawClasses[1])) {
					crudField.setJoEntity(new JoEntity(null, rawClasses[1]));
				}

				if (entityNames[1] != null) {
					crudField.setKeyJoEntity(new JoEntity(entityNames[1], null));

				} else if (rawClasses.length > 0 && rawClasses[0] != null && KernelClass.isCustomClass(rawClasses[0])) {
					crudField.setKeyJoEntity(new JoEntity(null, rawClasses[0]));
				}

				if (entityNames[0] == null || entityNames[1] == null) {
					Type[] typeArgs = KernelClass.typeArguments(field.getGenericType());
					if (typeArgs != null && typeArgs.length > 0) {
						if (entityNames[1] == null) {
							Class rawClass = KernelClass.rawClass(typeArgs[0]);
							if (!typeFieldType(rawClass) && KernelClass.isCustomClass(rawClass)) {
								crudField.setCruds(JaCrud.ALL);
								crudField.setKeyJoEntity(new JoEntity(null, rawClass));
							}
						}

						if (entityNames[0] == null && typeArgs.length > 1) {
							Class rawClass = KernelClass.rawClass(typeArgs[0]);
							if (KernelClass.isCustomClass(rawClass)) {
								crudField.setCruds(JaCrud.ALL);
								crudField.setJoEntity(new JoEntity(null, rawClass));

							} else {
								valueField = new EntityField();
								Object key = metas.get("valueField");
								if (key != null && key instanceof Map) {
									DynaBinder.mapTo((Map) key, valueField);
								}

								((EntityField) valueField).typeFieldType(rawClass);
							}
						}
					}
				}

				types.add(0, JaEdit.EDIT_SUBTABLE);

			} else {
				String entityName = null;
				// 自动关联实体名称
				if (joEntity.getEntityName() != null) {
					if (JiAssoc.class.isAssignableFrom(field.getDeclaringClass()) && "assocId".equals(field.getName())) {
						EntityAssocEntity assocEntity = SessionFactoryUtils.getEntityAssocEntity(joEntity.getEntityName());
						if (assocEntity != null) {
							entityName = assocEntity.getAssocName();
						}

					} else if (JiRelation.class.isAssignableFrom(JiRelation.class) && "relateId".endsWith(field.getName())) {
						EntityAssocEntity assocEntity = SessionFactoryUtils.getEntityAssocEntity(joEntity.getEntityName());
						if (assocEntity != null) {
							entityName = assocEntity.getAssocEntity().getReferenceEntityName();
						}
					}
				}

				if (entityName == null) {
					entityName = SessionFactoryUtils.getReferencedJpaEntityName(joEntity, field);
				}

				if (KernelString.isEmpty(entityName)) {
					if (cls.isArray()) {
						if (KernelClass.isBasicClass(cls.getComponentType())) {
							types.add("params");

						} else {
							canOrder = false;
						}

					} else if (Collection.class.isAssignableFrom(cls)) {
						canOrder = false;
						collection = true;
						Class rawClass = KernelClass.componentClass(field.getGenericType());
						if (!typeFieldType(rawClass) && KernelClass.isCustomClass(rawClass)) {
							crudField.setCruds(JaCrud.ALL);
							crudField.setJoEntity(new JoEntity(null, rawClass));
						}

						types.add(0, JaEdit.EDIT_SUBTABLE);
					}

				} else {
					this.entityName = entityName;
					crudField.setJoEntity(new JoEntity(entityName, null));
					if (Collection.class.isAssignableFrom(cls)) {
						canOrder = false;
						types.add("selects");

					} else {
						types.add("select");
					}
				}

				if (mappedBy == null && (crudField.getCruds() == null || !(KernelArray.contain(crudField.getCruds(), Crud.CREATE) && KernelArray.contain(crudField.getCruds(), Crud.DELETE)))) {
					if (types.size() > 0 && JaEdit.EDIT_SUBTABLE.equals(types.get(0))) {
						types.remove(0);
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void initValidate() {
		if (field.getAnnotation(NotEmpty.class) != null) {
			nullable = false;

		} else {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				nullable = column.nullable();
			}
		}

		Object validators = metas.get("validators");
		Map<String, Object> validatorMap = validators == null || !(validators instanceof Map) ? null : (Map<String, Object>) validators;
		if (validatorMap == null) {
			validatorMap = new HashMap<String, Object>();
			metas.put("validators", validatorMap);
		}

		if (field.getAnnotation(Email.class) != null) {
			metas.put("validatorClass", "email");

		} else if (field.getAnnotation(Digits.class) != null) {
			metas.put("validatorClass", "digits");
		}

		Min min = field.getAnnotation(Min.class);
		if (min != null) {
			validatorMap.put("min", min.value());
		}

		Max max = field.getAnnotation(Max.class);
		if (max != null) {
			validatorMap.put("max", max.value());
		}

		Length length = field.getAnnotation(Length.class);
		if (length != null) {
			if (length.min() > 0) {
				validatorMap.put("minlength", length.min());
			}

			if (length.max() > 0) {
				validatorMap.put("maxlength", length.max());
			}
		}
	}

	/**
	 * @param iFieldScope
	 */
	protected void addEntityFieldScope(List<IField> iFieldScope) {
		if (field.getType().getAnnotation(Embeddable.class) == null) {
			iFieldScope.add(this);

		} else {
			EntityModel entityModel = ModelFactory.getModelEntity(new JoEntity(null, field.getType()));
			for (IField iField : entityModel.getFields()) {
				iFieldScope.add(new EmbeddField(iField, getName(), getOrder()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.developer.model.DBField#instanceDefaultEntity()
	 */
	@Override
	protected Object instanceDefaultEntity() {
		if (getTypes().size() > 0 && JaEdit.EDIT_SUBTABLE.equals(getTypes().get(0)))
			if (getMappedBy() == null) {
				if (crudField.getKeyJoEntity() == null && getValueField() == null) {
					if (crudField.getJoEntity() == null) {

					} else {
						return KernelClass.newInstance(crudField.getJoEntity().getEntityClass());
					}

				} else {
					ObjectEntry<Object, Object> entry = new ObjectEntry<Object, Object>();
					if (crudField.getKeyJoEntity() != null) {
						entry.setKey(KernelClass.newInstance(crudField.getKeyJoEntity().getEntityClass()));
					}

					if (crudField.getJoEntity() != null) {
						entry.setValue(KernelClass.newInstance(crudField.getJoEntity().getEntityClass()));
					}

					return entry;
				}
			}

		return null;
	}
}
