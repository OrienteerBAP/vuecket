package org.orienteer.vuecket.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Random;

import org.apache.wicket.Component;
import org.orienteer.vuecket.VueBehavior;
import org.orienteer.vuecket.VueComponent;

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
	
	public static VueBehavior findVueBehavior(Component comp) {
		if(comp instanceof VueComponent) return ((VueComponent<?>)comp).getVueBehavior();
		else {
			List<VueBehavior> vueBehaviors = comp.getBehaviors(VueBehavior.class);
			return vueBehaviors==null || vueBehaviors.isEmpty()? null:vueBehaviors.get(0);
		}
	}
	
	public static String randomId() {
		return "id"+RANDOM.nextInt(1000);
	}
}
