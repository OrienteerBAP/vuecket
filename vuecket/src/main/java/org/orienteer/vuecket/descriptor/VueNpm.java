package org.orienteer.vuecket.descriptor;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to associate component with some Vue.JS NPM package
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface VueNpm {
	String packageName();
	String path() default "";
	String enablement();
}
