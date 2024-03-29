package org.orienteer.vuecket.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.request.ClientInfo;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.util.string.Strings;
import org.orienteer.vuecket.IVueBehaviorLocator;
import org.orienteer.vuecket.VueBehavior;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueSettings;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * Collection of Vuecket utility methods
 */
public final class VuecketUtils {
	
	private final static Random RANDOM = new Random();
	private final static Converter<String, String> CONVERTER_TO_KEBAB 
								= CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
	
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
		if(comp instanceof IVueBehaviorLocator) return ((IVueBehaviorLocator)comp).getVueBehavior();
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
	
	public static String removeSuffix(String str, String suffix) {
		if(str==null || suffix==null ||  !str.endsWith(suffix)) return str;
		else return str.substring(0, str.length() - suffix.length());
	}
	
	public static String getSuffixAfter(String str, String delimeter) {
		if(str==null || delimeter==null) return str;
		else {
			int indx = str.lastIndexOf(delimeter);
			return indx<0?str:str.substring(indx+1);
		}
	}
	
	public static JsonNode toJsonNode(String json) {
		return toJsonNode(VueSettings.get().getObjectMapper(), json);
	}
	
	public static JsonNode toJsonNode(ObjectMapper om, String json) {
		try {
			if(Strings.isEmpty(json)) return NullNode.getInstance();
			char firstChar = json.trim().charAt(0);
			if(firstChar!='{' && firstChar!='[' && firstChar!='"') return TextNode.valueOf(json);
			else return om.readTree(json);
		} catch (JsonProcessingException e) {
			throw new WicketRuntimeException("Wrong json format: "+json, e);
		}
	}
	
	public static <T> T jsonNodeToValue(TreeNode node, Type type) throws JsonProcessingException {
		return jsonNodeToValue(VueSettings.get().getObjectMapper(), node, type);
	}
	
	public static <T> T jsonNodeToValue(ObjectMapper om, TreeNode node, Type type) throws JsonProcessingException {
		if(node==null) return null;
		try {
			if(type instanceof Class) return om.treeToValue(node, (Class<T>) type);
			else {
				return (T) om.readValue(om.treeAsTokens(node), om.getTypeFactory().constructType(type));
			}
		} catch (JsonProcessingException e) {
			throw e;
		} catch (IOException e) {
			throw new IllegalStateException("We shouldn't have exceptions here", e);
		}
	}
	
	public static String toKebab(String string) {
		return string!=null?CONVERTER_TO_KEBAB.convert(string):null;
	}
	
	public static Locale getLocale() {
		Locale ret = null;
		if(Session.exists()) {
			ret = Session.get().getLocale();
		}
		return ret!=null?ret:Locale.getDefault();
	}
	
	public static TimeZone getTimeZone() {
		TimeZone ret = null;
		if(Session.exists()) {
			ClientInfo info = Session.get().getClientInfo();
			if(info instanceof WebClientInfo) ret = ((WebClientInfo)info).getProperties().getTimeZone();
		}
		return ret!=null?ret:TimeZone.getDefault();
	}
	
	public static String toDateString(Date date) {
		return toDateString(date, DateFormat.SHORT);
	}
	
	public static String toDateString(Date date, int style) {
		DateFormat format =  DateFormat.getDateInstance(style, getLocale());
		format.setTimeZone(getTimeZone());
		return format.format(date);
	}
	
	public static String toTimeString(Date date) {
		return toTimeString(date, DateFormat.SHORT);
	}
	
	public static String toTimeString(Date date, int style) {
		DateFormat format =  DateFormat.getTimeInstance(style, getLocale());
		format.setTimeZone(getTimeZone());
		return format.format(date);
	}
}
