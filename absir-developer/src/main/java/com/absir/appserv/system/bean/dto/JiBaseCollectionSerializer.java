/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-29 下午12:41:38
 */
package com.absir.appserv.system.bean.dto;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.absir.appserv.system.bean.proxy.JiBase;

/**
 * @author absir
 * 
 */
public class JiBaseCollectionSerializer extends JsonSerializer<Collection<? extends JiBase>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(Collection<? extends JiBase> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		jgen.writeStartArray();
		for (JiBase base : value) {
			jgen.writeObject(base.getId());
		}

		jgen.writeEndArray();
	}

}
