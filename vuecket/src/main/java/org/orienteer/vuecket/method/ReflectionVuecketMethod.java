package org.orienteer.vuecket.method;

import static org.orienteer.vuecket.util.VuecketUtils.jsonNodeToValue;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.orienteer.vuecket.VueBehavior;
import org.orienteer.vuecket.VueSettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * {@link IVuecketMethod} which use annotation {@link VueMethod} and reflection for invocation
 * @param <R> type of returned object
 */
public class ReflectionVuecketMethod<R> implements IVuecketMethod<R> {

	private transient Method method;
	private final String methodName;
	private final Class<?> declaringClass;
	private final Class<?>[] parameterTypes;
	
	public ReflectionVuecketMethod(Method method) {
		this.method = method;
		this.methodName = method.getName();
		this.declaringClass = method.getDeclaringClass();
		this.parameterTypes = method.getParameterTypes();
		this.method.setAccessible(true);
		if(!(Component.class.isAssignableFrom(declaringClass) 
				|| VueBehavior.class.isAssignableFrom(declaringClass))) 
					throw new WicketRuntimeException("Method should belong to either Component or VueBehavior");
	}
	
	protected Method getMethod() throws NoSuchMethodException, SecurityException {
		if(method==null) {
			method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
		}
		return method;
	}

	@Override
	public R invoke(Context ctx, ArrayNode args) throws Exception {
		Object obj = Component.class.isAssignableFrom(declaringClass)?ctx.getComponent():ctx.getVueBehavior();
		ObjectMapper om = VueSettings.get().getObjectMapper();
		Object[] vals = new Object[parameterTypes.length];
		int shift = 0;
		if(Context.class.isAssignableFrom(parameterTypes[0])) {
			vals[0] = ctx;
			shift=1;
		}
		Method method = getMethod();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		for(int indx = 0; indx < args.size() && indx+shift < vals.length; indx++) {
			vals[indx+shift] = jsonNodeToValue(om, args.get(indx), genericParameterTypes[indx+shift]);
		}
		return (R) method.invoke(obj, vals);
	}
	
	@Override
	public String toString() {
		return "VueMethod for "+declaringClass.getName()+"."+methodName+"(...)";
	}

}
