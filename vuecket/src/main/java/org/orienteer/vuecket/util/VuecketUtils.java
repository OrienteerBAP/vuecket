package org.orienteer.vuecket.util;

import java.lang.annotation.Annotation;
import java.util.Random;

public final class VuecketUtils {
	
	private final static Random RANDOM = new Random();
	private VuecketUtils() {
		
	}
	
	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
		for(;clazz!=null && !clazz.equals(Object.class); clazz = clazz.getSuperclass())
		{
			A ret = clazz.getAnnotation(annotationClass);
			if(ret!=null) return ret;
		}
		return null;
	}
	
	public static <A extends Annotation> Class<?> findClassWithAnnotation(Class<?> clazz, Class<A> annotationClass) {
		for(;clazz!=null && !clazz.equals(Object.class); clazz = clazz.getSuperclass())
		{
			A ret = clazz.getAnnotation(annotationClass);
			if(ret!=null) return clazz;
		}
		return null;
	}
	
	public static String randomId() {
		return "id"+RANDOM.nextInt(1000);
	}
}
