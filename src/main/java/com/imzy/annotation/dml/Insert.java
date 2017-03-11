package com.imzy.annotation.dml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.imzy.xml.node.LangType;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
	String value();

	LangType type() default LangType.SQL;

}
