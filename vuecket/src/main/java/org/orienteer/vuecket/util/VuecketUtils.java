package org.orienteer.vuecket.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.string.Strings;
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
	
	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    Class<?> clazz = type;
	    while (clazz != Object.class) {
	        for (final Method method : clazz.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(annotation)) {
	                methods.add(method);
	            }
	        }
	        clazz = clazz.getSuperclass();
	    }
	    return methods;
	}
	
	/**
	 * Converts given objects into map.
	 * Uses pairs of object.
	 * Call toMap("key1", "value1", "key2", "value2") will returns this map:
	 * { "key1": "value1", "key2": "value2" }
	 * Call method with not pair arguments will throw {@link IllegalStateException}.
	 * For example: toMap("key1", "value1", "key2") - throws {@link IllegalStateException}
	 * @param objects {@link Object[]} array of objects which will be used for create new map
	 * @param <K> type of map key
	 * @param <V> type of map value
	 * @return {@link Map} created from objects
	 * @throws IllegalStateException if objects are not pair
	 */
	public static final <K, V> Map<K, V> toMap(Object... objects) {
		if(objects==null || objects.length % 2 !=0) throw new IllegalArgumentException("Illegal arguments provided to construct a map");
		Map<K, V> ret = new HashMap<K, V>();
		for(int i=0; i<objects.length; i+=2) {
			ret.put((K)objects[i], (V)objects[i+1]);
		}
		return ret;
	}
	
	public static <V> V getAnnotationValue(Annotation ann) {
		return getAnnotationValue(ann, "value");
	}
	
	public static <V> V getAnnotationValue(Annotation ann, String attr) {
		if(ann==null || Strings.isEmpty(attr)) return null;
		try {
			Method method = ann.annotationType().getDeclaredMethod(attr);
			method.setAccessible(true);
			return (V) method.invoke(ann);
		} catch (Exception e) {
			throw new WicketRuntimeException("Can't read '"+attr+" from annotation: "+ann,e);
		} 
	}
	
	public static Class<?> getValueClass(IModel<?> model) {
		if(model instanceof IObjectClassAwareModel) return ((IObjectClassAwareModel<?>)model).getObjectClass();
		Object value = model.getObject();
		return value!=null?value.getClass():null;
	}
}
