package org.orienteer.vuecket;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Vue {
	public String name() default "";
	public VueDescriptor.Type type() default VueDescriptor.Type.AUTODETECT;
	public String value();
}
