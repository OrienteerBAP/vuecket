package org.orienteer.vuecket.method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for methods which should be accessible through `vcCall` or `vcInvoke`
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface VueMethod {
	String value() default "";
}
