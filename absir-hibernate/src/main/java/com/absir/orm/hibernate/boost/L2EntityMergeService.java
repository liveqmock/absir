/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午2:55:04
 */
package com.absir.orm.hibernate.boost;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.persister.entity.EntityPersister;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.InjectOnce;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Started;
import com.absir.core.kernel.KernelReflect;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.hibernate.boost.IEntityMerge.MergeType;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Bean
@Base
public class L2EntityMergeService implements IEventService, PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener, InjectOnce {

	/** nameMapEntityMerge */
	private Map<String, IEntityMerge> nameMapEntityMerge = new HashMap<String, IEntityMerge>();

	/**
	 * @param entityMerges
	 */
	@Started
	protected void loadEntityMerges() {
		List<IEntityMerge> entityMerges = BeanFactoryUtils.get().getBeanObjects(IEntityMerge.class);
		if (entityMerges.isEmpty()) {
			return;
		}

		Map<String, IEntityMerge> entityMergeBases = new HashMap<String, IEntityMerge>();
		Class<?>[] parameterTypes = new Class<?>[] { String.class, null, MergeType.class };
		for (IEntityMerge entityMerge : entityMerges) {
			Method method = KernelReflect.assignableMethod(entityMerge.getClass(), "merge", parameterTypes);
			if (method != null) {
				Class<?> entityClass = method.getParameterTypes()[1];
				String entityName = SessionFactoryUtils.getEntityName(entityClass);
				if (entityName == null) {
					for (Entry<String, Entry<Class<?>, SessionFactory>> entry : SessionFactoryUtils.get().getJpaEntityNameMapEntityClassFactory().entrySet()) {
						if (entityClass.isAssignableFrom(entry.getValue().getKey())) {
							entityMergeBases.put(entry.getValue().getKey().getName(), entityMerge);
						}
					}

				} else {
					nameMapEntityMerge.put(entityName, entityMerge);
				}
			}
		}

		for (Entry<String, IEntityMerge> entry : entityMergeBases.entrySet()) {
			if (!nameMapEntityMerge.containsKey(entry.getKey())) {
				nameMapEntityMerge.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.hibernate.boost.IEventService#boost(java.util.Map,
	 * org.hibernate.mapping.PersistentClass, org.hibernate.mapping.Property,
	 * java.lang.reflect.Field, java.lang.String)
	 */
	@Override
	public void boost(Map<String, PersistentClass> classes, PersistentClass persistentClass, Property property, Field field, String referencedEntityName) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.orm.hibernate.boost.IEventService#setEventListenerRegistry(
	 * org.hibernate.event.service.spi.EventListenerRegistry)
	 */
	@Override
	public void setEventListenerRegistry(EventListenerRegistry eventListenerRegistry) {
		// TODO Auto-generated method stub
		eventListenerRegistry.appendListeners(EventType.POST_COMMIT_INSERT, this);
		eventListenerRegistry.appendListeners(EventType.POST_COMMIT_UPDATE, this);
		eventListenerRegistry.appendListeners(EventType.POST_COMMIT_DELETE, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.event.spi.PostInsertEventListener#requiresPostCommitHanding
	 * (org.hibernate.persister.entity.EntityPersister)
	 */
	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.event.spi.PostDeleteEventListener#onPostDelete(org.hibernate
	 * .event.spi.PostDeleteEvent)
	 */
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		// TODO Auto-generated method stub
		String entityName = event.getPersister().getEntityName();
		IEntityMerge entityMerge = nameMapEntityMerge.get(entityName);
		if (entityMerge != null) {
			entityMerge.merge(entityName, event.getEntity(), MergeType.DELETE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.event.spi.PostUpdateEventListener#onPostUpdate(org.hibernate
	 * .event.spi.PostUpdateEvent)
	 */
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		// TODO Auto-generated method stub
		String entityName = event.getPersister().getEntityName();
		IEntityMerge entityMerge = nameMapEntityMerge.get(entityName);
		if (entityMerge != null) {
			entityMerge.merge(entityName, event.getEntity(), MergeType.UPDATE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.event.spi.PostInsertEventListener#onPostInsert(org.hibernate
	 * .event.spi.PostInsertEvent)
	 */
	@Override
	public void onPostInsert(PostInsertEvent event) {
		// TODO Auto-generated method stub
		String entityName = event.getPersister().getEntityName();
		IEntityMerge entityMerge = nameMapEntityMerge.get(entityName);
		if (entityMerge != null) {
			entityMerge.merge(entityName, event.getEntity(), MergeType.INSERT);
		}
	}
}
