/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-7 下午5:42:28
 */
package com.absir.appserv.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.proxy.HibernateProxy;

import com.absir.aop.AopProxy;
import com.absir.appserv.crud.CrudHandler.CrudInvoker;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.support.Developer;
import com.absir.appserv.support.developer.IDeveloper;
import com.absir.appserv.support.developer.IModel;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.service.CrudService;
import com.absir.bean.basis.Configure;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.PropertyFilter;
import com.absir.core.util.UtilAccessor;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.core.util.UtilRuntime;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Configure
public abstract class CrudUtils {

	/** Entity_Name_Map_Crud_Entity */
	private static final Map<JoEntity, CrudEntity> Jo_Entity_Map_Crud_Entity = new HashMap<JoEntity, CrudEntity>();

	/** Jo_Entity_Map_Crud_Filter */
	private static final Map<JoEntity, Boolean> Jo_Entity_Map_Crud_Filter = new HashMap<JoEntity, Boolean>();

	/** Jo_Entity_Map_Crud_Fields */
	private static final Map<String, String[]> Jo_Entity_Map_Crud_Fields = new HashMap<String, String[]>();

	/**
	 * @param entityName
	 * @param entityClass
	 * @return
	 */
	public static JoEntity newJoEntity(String entityName, Class<?> entityClass) {
		if (entityName == null) {
			while (AopProxy.class.isAssignableFrom(entityClass) || HibernateProxy.class.isAssignableFrom(entityClass)) {
				entityClass = entityClass.getSuperclass();
			}

			entityName = SessionFactoryUtils.getEntityNameNull(entityClass);
			if (entityName == null) {
				entityName = entityClass.getSimpleName();
				ICrudSupply crudSupply = CrudService.ME.getCrudSupply(entityName);
				if (crudSupply == null || crudSupply.getEntityClass(entityName) != entityClass) {
					entityName = null;
				}
			}

		} else if (entityClass == null) {
			ICrudSupply crudSupply = CrudService.ME.getCrudSupply(entityName);
			if (crudSupply != null) {
				entityClass = crudSupply.getEntityClass(entityName);
			}
		}

		return new JoEntity(entityName, entityClass);
	}

	/**
	 * @param joEntity
	 * @param entity
	 * @param filter
	 * @return
	 */
	public static Map<String, Object> crudRecord(JoEntity joEntity, Object entity, PropertyFilter filter) {
		if (entity == null) {
			return null;
		}

		CrudEntity crudEntity = getCrudEntity(joEntity);
		if (crudEntity == null) {
			return null;
		}

		Map<String, Object> record = new HashMap<String, Object>();
		String propertyPath = filter.getPropertyPath();
		Iterator<CrudPropertyReference> rIterator = crudEntity.getCrudPropertyReferencesIterator();
		if (rIterator != null) {
			while (rIterator.hasNext()) {
				CrudPropertyReference crudPropertyReference = rIterator.next();
				if (filter.isMatchPath(propertyPath, crudPropertyReference.getCrudProperty().getName())) {
					record.put(filter.getPropertyPath(), crudPropertyReference.getCrudProperty().get(entity));
				}
			}
		}

		Iterator<CrudProperty> pIterator = crudEntity.getCrudPropertiesIterator();
		if (pIterator != null) {
			while (pIterator.hasNext()) {
				CrudProperty crudProperty = pIterator.next();
				if (filter.isMatchPath(propertyPath, crudProperty.getName())) {
					record.put(filter.getPropertyPath(), crudProperty.get(entity));
				}
			}
		}

		return record;
	}

	/**
	 * @param crud
	 * @param joEntity
	 * @param entity
	 * @param filter
	 * @param group
	 * @param user
	 */
	public static void crud(JaCrud.Crud crud, Map<String, Object> crudRecord, JoEntity joEntity, Object entity, PropertyFilter filter, final JiUserBase user) {
		CrudEntity crudEntity = getCrudEntity(joEntity);
		if (crudEntity == null) {
			return;
		}

		if (filter == null) {
			filter = new PropertyFilter();

		} else {
			filter = filter.newly();
		}

		CrudInvoker crudInvoker = new CrudInvoker(crud, crudRecord, filter, crudEntity, entity) {

			@Override
			public boolean isSupport(CrudProperty crudProperty) {
				// TODO Auto-generated method stub
				return filter.allow(crudProperty.getInclude(), crudProperty.getExclude());
			}

			@Override
			public void crudInvoke(CrudProperty crudProperty, Object entity) {
				// TODO Auto-generated method stub
				crudProperty.crudProcessor.crud(crudProperty, entity, this, user);
			}
		};

		if (entity instanceof ICrudBean) {
			((ICrudBean) entity).proccessCrud(crud, crudInvoker);
		}

		crud(entity, crudEntity, crudInvoker);
	}

	/**
	 * @param entity
	 * @param crudEntity
	 * @param crudInvoker
	 */
	protected static void crud(Object entity, CrudEntity crudEntity, CrudInvoker crudInvoker) {
		if (crudEntity != null) {
			PropertyFilter filter = crudInvoker.filter;
			String propertyPath = filter.getPropertyPath();
			Iterator<CrudPropertyReference> rIterator = crudEntity.getCrudPropertyReferencesIterator();
			List<Object[]> updateRecords = null;
			if (rIterator != null) {
				while (rIterator.hasNext()) {
					CrudPropertyReference crudPropertyReference = rIterator.next();
					CrudProperty crudProperty = crudPropertyReference.getCrudProperty();
					if (filter.isMatchPath(propertyPath, crudProperty.getName())) {
						crudPropertyReference.crud(entity, crudInvoker);
						if (crudInvoker.crud == Crud.UPDATE && entity == crudInvoker.getRoot() && crudInvoker.getCrudRecord() != null) {
							Object record = crudProperty.get(entity);
							if (record != null) {
								Object property = crudProperty.get(entity);
								if (record != property) {
									if (updateRecords == null) {
										updateRecords = new ArrayList<Object[]>();
									}

									updateRecords.add(new Object[] { filter.getPropertyPath(), crudPropertyReference, record, property });
								}
							}
						}
					}
				}
			}

			Iterator<CrudProperty> pIterator = crudEntity.getCrudPropertiesIterator();
			if (pIterator != null) {
				while (pIterator.hasNext()) {
					CrudProperty crudProperty = pIterator.next();
					if (crudInvoker.isSupport(crudInvoker.crud, crudProperty) && filter.isMatchPath(propertyPath, crudProperty.getName())) {
						crudInvoker.crudInvoke(crudProperty, entity);
					}
				}
			}

			if (updateRecords != null) {
				crudInvoker.crud = Crud.DELETE;
				for (Object[] update : updateRecords) {
					crudInvoker.filter.setPropertyPath((String) update[0]);
					CrudPropertyReference crudPropertyReference = (CrudPropertyReference) update[1];
					CrudProperty crudProperty = crudPropertyReference.getCrudProperty();
					crudProperty.set(entity, update[2]);
					crudPropertyReference.crud(entity, crudInvoker);
					crudProperty.set(entity, update[3]);
				}

				crudInvoker.crud = Crud.UPDATE;
			}
		}
	}

	/**
	 * @param joEntity
	 * @return
	 */
	public static Boolean getCrudFilter(JoEntity joEntity) {
		Boolean filter = Jo_Entity_Map_Crud_Filter.get(joEntity);
		if (filter == null) {
			synchronized (joEntity.getEntityToken()) {
				filter = Jo_Entity_Map_Crud_Filter.get(joEntity);
				if (filter == null) {
					String runtimeName = UtilRuntime.getRuntimeName(CrudUtils.class, "crudFiters/" + joEntity);
					if (IDeveloper.ME == null) {
						filter = (Boolean) Developer.getRuntime(runtimeName);

					} else {
						IModel model = IDeveloper.ME.getModelEntity(joEntity);
						filter = model == null ? false : model.isFilter();
						Developer.setRuntime(runtimeName, filter);
					}

					if (filter == null) {
						filter = false;
					}

					Jo_Entity_Map_Crud_Filter.put(joEntity, filter);
				}
			}
		}

		return filter;
	}

	/**
	 * @param joEntity
	 * @param group
	 * @return
	 */
	public static String[] getGroupFields(JoEntity joEntity, String group) {
		String crudFieldsKey = joEntity.getEntityName() + joEntity.getClass() + "/" + group;
		String[] fields = Jo_Entity_Map_Crud_Fields.get(crudFieldsKey);
		if (fields == null) {
			synchronized (joEntity.getEntityToken()) {
				fields = Jo_Entity_Map_Crud_Fields.get(crudFieldsKey);
				if (fields == null) {
					String runtimeName = UtilRuntime.getRuntimeName(CrudUtils.class, "crudFields/" + crudFieldsKey);
					if (IDeveloper.ME == null) {
						fields = (String[]) Developer.getRuntime(runtimeName);

					} else {
						fields = IDeveloper.ME.getGroupFields(joEntity, group);
						Developer.setRuntime(runtimeName, fields);
					}

					if (fields == null) {
						fields = KernelLang.NULL_STRINGS;
					}

					Jo_Entity_Map_Crud_Fields.put(crudFieldsKey, fields);
				}
			}
		}

		return fields;
	}

	/**
	 * @author absir
	 * 
	 */
	private static class CrudPropertyNone extends CrudProperty {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#getName()
		 */
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#get(java.lang.Object)
		 */
		@Override
		public Object get(Object entity) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#set(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public void set(Object entity, Object propertyValue) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * @author absir
	 * 
	 */
	private static class CrudPropertyName extends CrudProperty {

		/** name */
		private String name;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#getName()
		 */
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#get(java.lang.Object)
		 */

		@Override
		public Object get(Object entity) {
			// TODO Auto-generated method stub
			return ((Map) entity).get(name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#set(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public void set(Object entity, Object propertyValue) {
			// TODO Auto-generated method stub
			((Map) entity).put(name, propertyValue);
		}
	}

	/**
	 * @author absir
	 * 
	 */
	private static class CrudPropertyAccessor extends CrudProperty {

		/** name */
		private String name;

		/** accessor */
		private Accessor accessor;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#getName()
		 */
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#get(java.lang.Object)
		 */
		@Override
		public Object get(Object entity) {
			// TODO Auto-generated method stub
			return accessor.get(entity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#set(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public void set(Object entity, Object propertyValue) {
			// TODO Auto-generated method stub
			accessor.set(entity, propertyValue);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.CrudProperty#getAccessor()
		 */
		@Override
		public Accessor getAccessor() {
			// TODO Auto-generated method stub
			return accessor;
		}
	}

	/**
	 * @author absir
	 * 
	 */
	private static class CrudPropertyEntity extends CrudPropertyReference {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.CrudPropertyReference#crud(java.lang.Object,
		 * com.absir.appserv.crud.CrudHandler.CrudInvoker)
		 */
		@Override
		protected void crud(Object entity, CrudInvoker crudInvoker) {
			// TODO Auto-generated method stub
			if (KernelArray.contain(cruds, crudInvoker.crud)) {
				entity = crudProperty.get(entity);
				if (entity != null) {
					CrudUtils.crud(entity, crudProperty.crudEntity, crudInvoker);
				}
			}
		}
	}

	/**
	 * @author absir
	 * 
	 */
	private static class CrudPropertyCollection extends CrudPropertyReference {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.CrudPropertyReference#crud(java.lang.Object,
		 * com.absir.appserv.crud.CrudHandler.CrudInvoker)
		 */
		@Override
		protected void crud(Object entity, CrudInvoker crudInvoker) {
			// TODO Auto-generated method stub
			if (KernelArray.contain(cruds, crudInvoker.crud)) {
				entity = crudProperty.get(entity);
				if (entity != null) {
					String propertyPath = crudInvoker.filter.getPropertyPath();
					int i = 0;
					for (Object element : (Collection<Object>) entity) {
						if (crudInvoker.filter.isMatch(String.valueOf(i++))) {
							CrudUtils.crud(element, crudProperty.crudEntity, crudInvoker);
						}

						crudInvoker.filter.setPropertyPath(propertyPath);
					}
				}
			}
		}
	}

	/**
	 * @author absir
	 * 
	 */
	protected static class CrudPropertyMap extends CrudPropertyReference {

		/** keyCrudEntity */
		protected CrudEntity keyCrudEntity;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.CrudPropertyReference#crud(java.lang.Object,
		 * com.absir.appserv.crud.CrudHandler.CrudInvoker)
		 */
		@Override
		protected void crud(Object entity, CrudInvoker crudInvoker) {
			// TODO Auto-generated method stub
			if (KernelArray.contain(cruds, crudInvoker.crud)) {
				entity = crudProperty.get(entity);
				if (entity != null) {
					String propertyPath = crudInvoker.filter.getPropertyPath();
					int i = 0;
					for (Entry<Object, Object> entry : ((Map<Object, Object>) entity).entrySet()) {
						if (keyCrudEntity != null && crudInvoker.filter.isMatch(String.valueOf(i))) {
							CrudUtils.crud(entry.getKey(), crudProperty.crudEntity, crudInvoker);
						}

						if (valueCrudEntity != null && crudInvoker.filter.isMatchPath(propertyPath, ":" + String.valueOf(i))) {
							CrudUtils.crud(entry.getValue(), crudProperty.crudEntity, crudInvoker);
						}

						crudInvoker.filter.setPropertyPath(propertyPath);
						i++;
					}
				}
			}
		}
	}

	/**
	 * @param joEntity
	 * @return
	 */
	public static CrudEntity getCrudEntity(JoEntity joEntity) {
		CrudEntity crudEntity = Jo_Entity_Map_Crud_Entity.get(joEntity);
		if (crudEntity != null) {
			return crudEntity;
		}

		synchronized (joEntity.getEntityToken()) {
			crudEntity = generateCrudEntity(joEntity);
		}

		if (crudEntity.isCrudEntityNone()) {
			return null;
		}

		return crudEntity;
	}

	/**
	 * @param joEntity
	 * @param name
	 * @return
	 */
	public static CrudProperty getCrudProperty(JoEntity joEntity, String name) {
		CrudEntity crudEntity = getCrudEntity(joEntity);
		if (crudEntity != null && crudEntity.crudProperties != null) {
			for (CrudProperty crudProperty : crudEntity.crudProperties) {
				if (crudProperty.getName().equals(name)) {
					return crudProperty;
				}
			}
		}

		return null;
	}

	/**
	 * @param joEntity
	 * @param name
	 * @return
	 */
	public static CrudPropertyReference getCrudPropertyReference(JoEntity joEntity, String name) {
		CrudEntity crudEntity = getCrudEntity(joEntity);
		if (crudEntity != null && crudEntity.crudPropertyReferences != null) {
			for (CrudPropertyReference crudPropertyReference : crudEntity.crudPropertyReferences) {
				if (crudPropertyReference.getCrudProperty().getName().equals(name)) {
					return crudPropertyReference;
				}
			}
		}

		return null;
	}

	/**
	 * @param entityName
	 * @return
	 */
	private static CrudEntity generateCrudEntity(JoEntity joEntity) {
		CrudEntity crudEntity = Jo_Entity_Map_Crud_Entity.get(joEntity);
		if (crudEntity != null) {
			return crudEntity;
		}

		List<JCrudField> crudFields = null;
		String runtimeName = UtilRuntime.getRuntimeName(CrudUtils.class, "Crud_Fields_" + joEntity.toString());
		if (IDeveloper.ME == null) {
			crudFields = (List<JCrudField>) UtilRuntime.getRuntime(runtimeName);

		} else {
			crudFields = IDeveloper.ME.getCrudFields(joEntity);
		}

		crudEntity = new CrudEntity();
		for (JCrudField crudField : crudFields) {
			addCrudEntityProperty(joEntity, crudEntity, crudField, joEntity.getEntityClass());
		}

		crudEntity.joEntity = joEntity;
		Jo_Entity_Map_Crud_Entity.put(joEntity, crudEntity);
		crudEntity.initCrudEntity();
		return crudEntity;
	}

	/**
	 * @param joEntity
	 * @param crudEntity
	 * @param crudField
	 * @param entityClass
	 */
	private static void addCrudEntityProperty(JoEntity joEntity, CrudEntity crudEntity, JCrudField crudField, Class<?> entityClass) {
		ICrudProcessor crudProcessor = getCrudProcessor(joEntity, crudField);
		if (crudProcessor == null && crudField.getJoEntity() == null && crudField.getKeyJoEntity() == null) {
			return;
		}

		CrudProperty crudProperty = null;
		if (crudField.getName() == null) {
			crudProperty = new CrudPropertyNone();

		} else if (Map.class.isAssignableFrom(entityClass)) {
			CrudPropertyName crudPropertyName = new CrudPropertyName();
			crudPropertyName.name = crudField.getName();
			crudProperty = crudPropertyName;

		} else {
			Accessor accessor = UtilAccessor.getAccessorProperty(entityClass, crudField.getName());
			if (accessor == null) {
				return;
			}

			CrudPropertyAccessor crudPropertyAccessor = new CrudPropertyAccessor();
			crudPropertyAccessor.name = crudField.getName();
			crudPropertyAccessor.accessor = accessor;
			crudProperty = crudPropertyAccessor;
		}

		crudProperty.crudProcessor = crudProcessor;
		crudProperty.type = crudField.getType();
		crudProperty.include = crudField.getInclude();
		crudProperty.exclude = crudField.getExclude();
		crudProperty.jCrud = crudField.getjCrud();
		crudProperty.keyEntity = crudField.getKeyJoEntity();
		crudProperty.valueEntity = crudField.getJoEntity();

		if (crudProcessor != null && crudProperty.jCrud != null) {
			crudEntity.addCrudProperty(crudProperty);
		}

		crudProperty.crudEntity = crudEntity;
		if (crudField.getCruds() != null && !(crudField.getJoEntity() == null && crudField.getKeyJoEntity() == null)) {
			CrudPropertyReference crudPropertyReference = null;
			if (Map.class.isAssignableFrom(crudProperty.type)) {
				crudPropertyReference = new CrudPropertyMap();
				if (crudField.getJoEntity() != null) {
					crudPropertyReference.valueCrudEntity = generateCrudEntity(crudField.getJoEntity());
				}

				if (crudField.getKeyJoEntity() != null) {
					((CrudPropertyMap) crudPropertyReference).keyCrudEntity = generateCrudEntity(crudField.getKeyJoEntity());
				}

				if ((crudPropertyReference.valueCrudEntity == null || crudPropertyReference.valueCrudEntity.crudProperties == null)
						&& (((CrudPropertyMap) crudPropertyReference).keyCrudEntity == null || ((CrudPropertyMap) crudPropertyReference).keyCrudEntity.crudProperties == null)) {
					return;
				}

			} else if (crudField.getJoEntity() != null) {
				if (Collection.class.isAssignableFrom(crudProperty.type)) {
					crudPropertyReference = new CrudPropertyCollection();

				} else {
					crudPropertyReference = new CrudPropertyEntity();
				}

				crudPropertyReference.valueCrudEntity = generateCrudEntity(crudField.getJoEntity());
				if (crudPropertyReference.valueCrudEntity == null || crudPropertyReference.valueCrudEntity.crudProperties == null) {
					return;
				}
			}

			if (crudPropertyReference != null) {
				crudPropertyReference.crudProperty = crudProperty;
				crudPropertyReference.cruds = crudField.getCruds();
				crudEntity.addCrudPropertyReference(crudPropertyReference);
			}
		}
	}

	/**
	 * @param joEntity
	 * @param crudField
	 * @return
	 */
	private static ICrudProcessor getCrudProcessor(JoEntity joEntity, JCrudField crudField) {
		if (crudField.getjCrud() == null) {
			return null;
		}

		ICrudFactory crudFactory = BeanFactoryUtils.getRegisterBeanObject(crudField.getjCrud().getValue(), ICrudFactory.class, (Class<? extends ICrudFactory>) crudField.getjCrud().getFactory());
		return crudFactory == null ? null : crudFactory.getProcessor(joEntity, crudField);
	}
}
