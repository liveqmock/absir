/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-1 下午3:48:25
 */
package com.absir.appserv.system.bean.dto;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author absir
 * 
 */
public class ObjectSerializer extends JsonSerializer<Object> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		if (value instanceof Number) {
			jgen.writeString(value.toString());

		} else {
			jgen.writeObject(value);
		}
	}
}
