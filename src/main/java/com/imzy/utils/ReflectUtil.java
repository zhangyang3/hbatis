package com.imzy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectUtil {

	public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) { // �����ֶ�ֵ
																								// �磺username
																								// �ֶ�,setUsername(String
																								// username)
		if (target == null || fname == null || "".equals(fname)
				|| (fvalue != null && !ftype.isAssignableFrom(fvalue.getClass()))) {// ������Ͳ�ƥ�䣬ֱ���˳�
			return;
		}
		Class clazz = target.getClass();
		try { // ��ͨ��setXxx()��������������ֵ
			String methodname = "set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
			System.out.println(methodname);
			Method method = clazz.getDeclaredMethod(methodname, ftype); // ��ȡ����ķ���
			if (!Modifier.isPublic(method.getModifiers())) { // ���÷ǹ��з���Ȩ��
				method.setAccessible(true);
			}
			method.invoke(target, fvalue); // ִ�з����ص�
		} catch (Exception me) {// ���set���������ڣ���ֱ������������ֵ
			try {
				Field field = clazz.getDeclaredField(fname); // ��ȡ�����������
				if (!Modifier.isPublic(field.getModifiers())) { // ���÷ǹ���������Ȩ��
					field.setAccessible(true);
				}
				field.set(target, fvalue); // ����������ֵ

			} catch (Exception fe) {
			}
		}
	}

	/**
	 * ��ȡ�����ĳ������ֵ
	 * @param target
	 * @param fname
	 * @return
	 */
	public static Object getFieldValue(Object target, String fname) {
		if (target == null || fname == null || "".equals(fname)) {
			return null;
		}
		Class clazz = target.getClass();
		try { // ��ͨ��getXxx()������ȡ������ֵ
			String methodname = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
			Method method = clazz.getMethod(methodname); // ��ȡ����ķ���
			if (!Modifier.isPublic(method.getModifiers())) { // ���÷ǹ��з���Ȩ��
				method.setAccessible(true);
			}
			return method.invoke(target); // �����ص�������ֵ
		} catch (Exception me) {// ���get���������ڣ���ֱ�ӻ�ȡ������ֵ
			try {
				Field field = clazz.getDeclaredField(fname); // ��ȡ�����������
				if (!Modifier.isPublic(field.getModifiers())) { // ���÷ǹ���������Ȩ��
					field.setAccessible(true);
				}
				return field.get(target);// ����������ֵ
			} catch (Exception fe) {
			}
		}
		return null;
	}

}