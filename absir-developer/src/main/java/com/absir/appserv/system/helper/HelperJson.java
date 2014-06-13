/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.system.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelCharset;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HelperJson {

	/** OBJECT_MAPPER */
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		// OBJECT_MAPPER.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		OBJECT_MAPPER.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		OBJECT_MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @author absir
	 * 
	 */
	public static class HelperIntrospector extends JacksonAnnotationIntrospector {

		/**
		 * 
		 */
		public HelperIntrospector() {
		}

		@Override
		public Object findFilterId(AnnotatedClass ac) {
			return super.findFilterId(ac);
		}
	}

	/**
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String encode(Object obj) throws IOException {
		ObjectWriter writer = OBJECT_MAPPER.writer();
		return writer.writeValueAsString(obj);
	}

	public static String encodeNull(Object obj) {
		try {
			return encode(obj);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * @param obj
	 * @param includes
	 * @return
	 * @throws IOException
	 */
	public static <T> String encodeInclude(T obj, String... includes) throws IOException {
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("filter properties include", SimpleBeanPropertyFilter.serializeAllExcept(includes));
		ObjectWriter writer = OBJECT_MAPPER.writer(filterProvider);
		return writer.writeValueAsString(obj);
	}

	/**
	 * @param pojo
	 * @param excludes
	 * @return
	 * @throws IOException
	 */
	public static <T> String encodeExclude(T pojo, String... excludes) throws IOException {
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("filter properties include", SimpleBeanPropertyFilter.filterOutAllExcept(excludes));
		ObjectWriter writer = OBJECT_MAPPER.writer(filterProvider);
		return writer.writeValueAsString(pojo);
	}

	/**
	 * @param string
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static Object decode(String string) throws JsonProcessingException, IOException {
		ObjectReader reader = OBJECT_MAPPER.reader();
		return reader.readValue(string);
	}

	/**
	 * @param string
	 * @return
	 */
	public static Object decodeNull(String string) {
		try {
			return decode(string);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	/**
	 * @param string
	 * @param toClass
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static <T> T decode(String string, Class toClass) throws JsonProcessingException, IOException {
		ObjectReader reader = OBJECT_MAPPER.reader(toClass);
		return reader.readValue(string);
	}

	/**
	 * @param string
	 * @param toType
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static <T> T decode(String string, Type toType) throws JsonProcessingException, IOException {
		ObjectReader reader = OBJECT_MAPPER.reader(OBJECT_MAPPER.constructType(toType));
		return reader.readValue(string);
	}

	/**
	 * @param string
	 * @param toClass
	 * @return
	 */
	public static <T> T decodeNull(String string, Class<T> toClass) {
		if (string == null) {
			return null;
		}

		try {
			return decode(string, toClass);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	/**
	 * @param string
	 * @param toType
	 * @return
	 */
	public static <T> T decodeNull(String string, Type toType) {
		if (string == null) {
			return null;
		}

		try {
			return decode(string, toType);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	/**
	 * @param string
	 * @return
	 */
	public static List decodeList(String string) {
		return decodeNull(string, List.class);
	}

	/**
	 * @param string
	 * @return
	 */
	public static Map decodeMap(String string) {
		return decodeNull(string, Map.class);
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static byte[] encodeBase64(byte[] bytes) {
		return null;
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static byte[] decodeBase64(byte[] bytes) {
		return null;
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String encodeBase64String(byte[] bytes) {
		return new String(encodeBase64(bytes));
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String decodeBase64String(byte[] bytes) {
		return new String(decodeBase64(bytes), KernelCharset.DEFAULT);
	}

	/**
	 * @param string
	 * @return
	 */
	public static String encodeBase64String(String string) {
		return encodeBase64String(string.getBytes(KernelCharset.DEFAULT));
	}

	/**
	 * @param string
	 * @return
	 */
	public static String decodeBase64String(String string) {
		return decodeBase64String(string.getBytes());
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String encodeBase64Json(Object obj) {
		try {
			return encodeBase64String(encode(obj));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * @param string
	 * @param toClass
	 * @return
	 */
	public static <T> T decodeBase64Json(String string, Class<T> toClass) {
		try {
			return decode(decodeBase64String(string), toClass);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * @param string
	 * @param toType
	 * @return
	 */
	public static <T> T decodeBase64Json(String string, Type toType) {
		try {
			return decode(decodeBase64String(string), toType);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> xmlToMap(String xml) {
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(xml.getBytes(ContextUtils.getCharset()));
			SAXReader saxReader = new SAXReader();
			return xmlToMap(saxReader.read(inputStream).getRootElement());

		} catch (Exception e) {
			return null;

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

	/**
	 * @param element
	 * @return
	 */
	private static Map<String, Object> xmlToMap(Element element) {
		Iterator<Element> iterator = element.elementIterator();
		if (iterator.hasNext()) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			while (true) {
				Element ele = iterator.next();
				Map<String, Object> eleMap = xmlToMap(ele);
				map.put(ele.getName(), eleMap == null ? ele.getText() : eleMap);
				if (!iterator.hasNext()) {
					break;
				}
			}

			return map;
		}

		return null;
	}

	/**
	 * @param map
	 * @param rootName
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map, String rootName) {
		return mapToXml(map, rootName, null);
	}

	/**
	 * @param map
	 * @param rootName
	 * @param data
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map, String rootName, String data) {
		try {
			XMLWriter xmlWriter = mapToXmlWriter(map, rootName);
			if (data != null) {

			}

			return xmlWriter.toString();

		} catch (Exception e) {

		}

		return null;
	}

	/**
	 * @param map
	 * @param rootName
	 * @return
	 * @throws IOException
	 */
	public static XMLWriter mapToXmlWriter(Map<String, Object> map, String rootName) throws IOException {
		XMLWriter xmlWriter = new XMLWriter();
		Document document = DocumentHelper.createDocument();
		if (rootName == null) {
			rootName = "xml";
		}

		Element element = document.addElement(rootName);
		mapToXmlWriter(map, element);
		xmlWriter.write(document);
		return xmlWriter;
	}

	/**
	 * @param map
	 * @param element
	 * @return
	 */
	protected static void mapToXmlWriter(Map<String, Object> map, Element element) {
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value != null) {
				if (value instanceof Map) {
					mapToXmlWriter((Map<String, Object>) value, element.addElement(entry.getKey()));

				} else {
					element.addAttribute(entry.getKey(), value.toString());
				}
			}
		}
	}
}
