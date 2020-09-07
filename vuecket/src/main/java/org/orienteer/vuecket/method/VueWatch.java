package org.orienteer.vuecket.method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for methods to be invoked upon data modification on Vue.JS side
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface VueWatch {
	String value() default "";
}
