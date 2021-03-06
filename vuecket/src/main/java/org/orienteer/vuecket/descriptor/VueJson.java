package org.orienteer.vuecket.descriptor;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to associate component and JSON with vue desciptor
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface VueJson {
	public String name() default "";
	public String json() default "{}";
}
