/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-22 上午10:26:04
 */
package com.absir.orm.hibernate;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.BasicType;

import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelObject;
import com.absir.orm.hibernate.boost.EntityAssoc.AssocEntity;
import com.absir.orm.hibernate.boost.EntityAssoc.AssocType;
import com.absir.orm.hibernate.boost.EntityBoost;
import com.absir.orm.hibernate.boost.IEventService;
import com.absir.orm.value.JiRelation;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public class SessionFactoryBoost {

	@Inject(type = InjectType.Selectable)
	private BasicType[] basicTypes;

	@Inject(type = InjectType.Selectable)
	private IEventService[] eventServices;

	/**
	 * @return the basicTypes
	 */
	public BasicType[] getBasicTypes() {
		return basicTypes;
	}

	/**
	 * @param configuration
	 * @param locale
	 */
	public void beforeBuildConfiguration(Configuration configuration, boolean locale) {
		EntityBoost.boost(configuration, this, locale);
	}

	/**
	 * @param configuration
	 * @param sessionFactory
	 */
	public void afterBuildConfiguration(Configuration configuration, SessionFactoryImpl sessionFactory) {
		EventListenerRegistry eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		if (eventServices != null) {
			for (IEventService eventService : eventServices) {
				eventService.setEventListenerRegistry(eventListenerRegistry);
			}
		}

		boost(configuration, SessionFactoryUtils.get(), sessionFactory);
		boost(sessionFactory);
	}

	/**
	 * @param classes
	 * @param persistentClass
	 * @param property
	 * @param field
	 * @param referencedEntityName
	 */
	public void boost(Map<String, PersistentClass> classes, PersistentClass persistentClass, Property property, Field field, String referencedEntityName) {
		if (eventServices != null) {
			for (IEventService eventService : eventServices) {
				eventService.boost(classes, persistentClass, property, field, referencedEntityName);
			}
		}
	}

	/**
	 * @param configuration
	 * @param sessionFactoryBean
	 * @param sessionFactory
	 */
	protected void boost(Configuration configuration, SessionFactoryBean sessionFactoryBean, SessionFactory sessionFactory) {
		Map<String, PersistentClass> classes = (Map<String, PersistentClass>) KernelObject.declaredGet(configuration, "classes");
		for (Entry<String, PersistentClass> entry : classes.entrySet()) {
			PersistentClass persistentClass = entry.getValue();
			sessionFactoryBean.getEntityNameMapJpaEntityName().put(persistentClass.getEntityName(), persistentClass.getJpaEntityName());
			Class<?> mappedClass = persistentClass.getMappedClass();
			if (mappedClass != null) {
				sessionFactoryBean.getJpaEntityNameMapEntityClassFactory().put(persistentClass.getJpaEntityName(), new ObjectEntry<Class<?>, SessionFactory>(mappedClass, sessionFactory));
			}
		}
	}

	/**
	 * @param sessionFactory
	 * @param entityNameMapJpaEntityName
	 */
	protected void boost(SessionFactoryImpl sessionFactory) {
		Map<String, ClassMetadata> classMetadata = new HashMap<String, ClassMetadata>();
		for (Entry<String, ClassMetadata> entry : sessionFactory.getAllClassMetadata().entrySet()) {
			classMetadata.put(entry.getKey(), entry.getValue());
			String jpaEntityName = SessionFactoryUtils.getJpaEntityName(entry.getKey());
			classMetadata.put(jpaEntityName, entry.getValue());
			List<AssocEntity> assocEntities = SessionFactoryUtils.get().getNameMapAssocEntities().get(entry.getKey());
			if (assocEntities != null) {
				String identifierName = entry.getValue().getIdentifierPropertyName();
				Object propertyMap = KernelObject.declaredGet(entry.getValue(), "propertyMapping");
				Map<Object, Object> typesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(propertyMap, "typesByPropertyPath");
				Map<Object, Object> columnsByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(propertyMap, "columnsByPropertyPath");
				Map<Object, Object> formulaTemplatesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(propertyMap, "formulaTemplatesByPropertyPath");
				Map<Object, Object> columnReaderTemplatesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(propertyMap, "columnReaderTemplatesByPropertyPath");
				for (AssocEntity assocEntity : assocEntities) {
					String assocEntityName = SessionFactoryUtils.getEntityName(assocEntity.getEntityName());
					if (JiRelation.class.isAssignableFrom(assocEntity.getEntityClass())) {
						ClassMetadata relateClassMetadata = sessionFactory.getAllClassMetadata().get(assocEntityName);
						Object relatePropertyMap = KernelObject.declaredGet(relateClassMetadata, "propertyMapping");
						Map<Object, Object> relateTypesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(relatePropertyMap, "typesByPropertyPath");
						Map<Object, Object> relateColumnsByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(relatePropertyMap, "columnsByPropertyPath");
						Map<Object, Object> relateFormulaTemplatesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(relatePropertyMap, "formulaTemplatesByPropertyPath");
						Map<Object, Object> relateColumnReaderTemplatesByPropertyPath = (Map<Object, Object>) KernelObject.declaredGet(relatePropertyMap, "columnReaderTemplatesByPropertyPath");

						String relatePropertyName = "$" + assocEntity.getReferenceEntityName();
						String relateIdentifierName = "relateId";
						relateTypesByPropertyPath.put(relatePropertyName, new AssocType(null, SessionFactoryUtils.getEntityName(assocEntity.getReferenceEntityName())));
						relateColumnsByPropertyPath.put(relatePropertyName, relateColumnsByPropertyPath.get(relateIdentifierName));
						relateFormulaTemplatesByPropertyPath.put(relatePropertyName, relateFormulaTemplatesByPropertyPath.get(relateIdentifierName));
						relateColumnReaderTemplatesByPropertyPath.put(relatePropertyName, relateColumnReaderTemplatesByPropertyPath.get(relateIdentifierName));
					}

					String propertyName = "$" + assocEntity.getEntityName();
					typesByPropertyPath.put(propertyName, new AssocType(null, assocEntityName, "assocId"));
					columnsByPropertyPath.put(propertyName, columnsByPropertyPath.get(identifierName));
					formulaTemplatesByPropertyPath.put(propertyName, formulaTemplatesByPropertyPath.get(identifierName));
					columnReaderTemplatesByPropertyPath.put(propertyName, columnReaderTemplatesByPropertyPath.get(identifierName));
				}
			}
		}

		classMetadata = Collections.unmodifiableMap(classMetadata);
		KernelObject.declaredSet(sessionFactory, "classMetadata", classMetadata);
	}
}
