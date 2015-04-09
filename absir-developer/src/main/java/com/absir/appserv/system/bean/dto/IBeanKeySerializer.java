/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-24 下午2:23:23
 */
package com.absir.appserv.system.bean.dto;

import java.io.IOException;

import com.absir.core.base.IBase;
import com.absir.core.dyna.DynaBinder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class IBeanKeySerializer extends JsonSerializer<IBase> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(IBase value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		jgen.writeFieldName(DynaBinder.to(value.getId(), String.class));
	}
}
