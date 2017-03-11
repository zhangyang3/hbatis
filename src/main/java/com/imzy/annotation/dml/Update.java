package com.imzy.annotation.dml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.imzy.xml.node.LangType;

/**
 * ÐÞ¸Ä
 * @author yangzhang7
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {
	String value();

	LangType type() default LangType.SQL;

}
