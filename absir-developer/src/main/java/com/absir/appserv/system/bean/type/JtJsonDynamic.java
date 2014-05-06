/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-18 下午3:48:04
 */
package com.absir.appserv.system.bean.type;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import com.absir.appserv.system.helper.HelperJson;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelReflect;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class JtJsonDynamic implements UserType, DynamicParameterizedType, Serializable {

	/** dynamicType */
	private Type dynamicType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.usertype.ParameterizedType#setParameterValues(java.util
	 * .Properties)
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		// TODO Auto-generated method stub
		Class<?> entityClass = KernelClass.forName(parameters.getProperty(ENTITY));
		if (entityClass != null) {
			Field field = KernelReflect.declaredField(entityClass, parameters.getProperty(PROPERTY));
			if (field != null) {
				dynamicType = field.getGenericType();
				return;
			}
		}

		dynamicType = KernelClass.forName(parameters.getProperty(RETURNED_CLASS));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
	public int[] sqlTypes() {
		// TODO Auto-generated method stub
		return new int[] { Types.LONGVARCHAR };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return Object.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		// TODO Auto-generated method stub
		if (x == y) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}

		return x.equals(y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(Object x) throws HibernateException {
		// TODO Auto-generated method stub
		return x.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
	 * java.lang.String[], org.hibernate.engine.spi.SessionImplementor,
	 * java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		String value = rs.getString(names[0]);
		if (value == null || value.trim().length() == 0) {
			return null;
		}

		return HelperJson.decodeNull(value, dynamicType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
	 * java.lang.Object, int, org.hibernate.engine.spi.SessionImplementor)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			try {
				st.setString(index, HelperJson.encode(value));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				st.setString(index, "");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Object deepCopy(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return KernelObject.clone(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return (Serializable) value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
	 * java.lang.Object)
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return cached;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return original;
	}
}
