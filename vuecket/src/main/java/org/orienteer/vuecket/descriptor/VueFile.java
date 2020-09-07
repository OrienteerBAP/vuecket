package org.orienteer.vuecket.descriptor;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to mark component and associate it with particular *.vue file
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface VueFile {
	String value();
}
