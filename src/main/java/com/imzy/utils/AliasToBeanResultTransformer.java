package com.imzy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

@SuppressWarnings("rawtypes")
public class AliasToBeanResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = 1L;

	public static String clobToString(Clob clob) {
		String reString = "";
		if (clob != null) {
			try {
				Reader is = clob.getCharacterStream();
				BufferedReader br = new BufferedReader(is);
				String s = br.readLine();
				StringBuffer sb = new StringBuffer();
				while (s != null) {
					sb.append(s);
					s = br.readLine();
				}
				reString = sb.toString();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reString;
	}

	public static String getFieldName(String columnName) {
		String[] columnNames = columnName.split("_");
		StringBuilder fieldName = new StringBuilder();
		for (String string : columnNames) {
			if (StringUtils.isNotBlank(string)) {
				if (StringUtils.isBlank(fieldName.toString())) {
					fieldName.append(StringUtils.lowerCase(string));
				} else {
					fieldName.append(
							StringUtils.upperCase(string.substring(0, 1)) + StringUtils.lowerCase(string.substring(1)));
				}
			}
		}
		return fieldName.toString();
	}
	private final PropertyAccessor propertyAccessor;
	private final Class resultClass;

	private Setter[] setters;

	public AliasToBeanResultTransformer(Class resultClass) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		this.resultClass = resultClass;
		propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] { PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
						PropertyAccessorFactory.getPropertyAccessor("field") });
	}

	@Override
	public int hashCode() {
		int result;
		result = resultClass.hashCode();
		result = 31 * result + propertyAccessor.hashCode();
		return result;
	}

	public List transformList(List collection) {
		return collection;
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result = null;

		try {
			if (setters == null) {
				setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; i++) {
					String alias = aliases[i];

					if (alias != null) {
						try {
							setters[i] = propertyAccessor.getSetter(resultClass, getFieldName(alias));
						} catch (PropertyNotFoundException e) {
							setters[i] = null;
						}
					}
				}
			}
			result = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				if (setters[i] != null) {
					if (tuple[i] instanceof Number) {
						Class<?> paramType = setters[i].getMethod().getParameterTypes()[0];
						BigDecimal decimal = (BigDecimal) tuple[i];
						if (paramType == Long.class) {
							setters[i].set(result, decimal.longValue(), null);
						} else if (paramType == String.class) {
							setters[i].set(result, decimal.toString(), null);
						} else {
							setters[i].set(result, decimal, null);
						}
					} else if (tuple[i] instanceof Clob) {
						Clob clob = (Clob) tuple[i];
						tuple[i] = clobToString(clob);
						setters[i].set(result, tuple[i], null);
					} else if (tuple[i] instanceof Character) {
						tuple[i] = tuple[i].toString();
						setters[i].set(result, tuple[i], null);
					} else {
						setters[i].set(result, tuple[i], null);
					}

				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}

		return result;
	}
}
