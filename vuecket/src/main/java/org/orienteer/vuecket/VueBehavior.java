package org.orienteer.vuecket;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;
import org.orienteer.vuecket.method.ReflectionVuecketMethod;
import org.orienteer.vuecket.method.VueMethod;
import org.orienteer.vuecket.method.VueOn;
import org.orienteer.vuecket.method.VueOnce;
import org.orienteer.vuecket.util.VuecketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class VueBehavior extends AbstractDefaultAjaxBehavior {
	
	public static final JavaScriptResourceReference VUE_JS = new JavaScriptResourceReference(VueComponent.class, "external/vue/dist/vue.js");
	public static final JavaScriptResourceReference HTTP_VUE_LOADER_JS = new JavaScriptResourceReference(VueComponent.class, "external/http-vue-loader/src/httpVueLoader.js");
	public static final JavaScriptResourceReference VUECKET_JS = new JavaScriptResourceReference(VueComponent.class, "vuecket.js");
	
	private static final Logger LOG = LoggerFactory.getLogger(VueBehavior.class);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private class VueConfigView implements IClusterable {
		public CharSequence getUrl() {
			return getCallbackUrl();
		}
		
		public Collection<String> getOn() {
			return vueOnMethods.keySet();
		}
		
		public Collection<String> getOnce() {
			return vueOnceMethods.keySet();
		}
	}
	
	private IVueDescriptor vueDescriptor;
	
	private final VueConfigView configView = new VueConfigView();
	
	private final Map<String, IVuecketMethod<?>> vueMethods = new HashedMap<String, IVuecketMethod<?>>();
	private final Map<String, IVuecketMethod<?>> vueOnMethods = new HashedMap<String, IVuecketMethod<?>>();
	private final Map<String, IVuecketMethod<?>> vueOnceMethods = new HashedMap<String, IVuecketMethod<?>>();
	
	public VueBehavior() {
		
	}
	
	public VueBehavior(IVueDescriptor vueDescriptor) {
		setVueDescriptor(vueDescriptor);
	}
	
	public IVueDescriptor getVueDescriptor() {
		if(vueDescriptor==null) {
			vueDescriptor = IVueDescriptor.lookupDescriptor(getComponent());
			if(vueDescriptor==null) vueDescriptor = VueJsonDescriptor.EMPTY_ROOT;
		}
		return vueDescriptor;
	}
	
	public VueBehavior setVueDescriptor(IVueDescriptor vueDescriptor) {
		this.vueDescriptor = vueDescriptor;
		return this;
	}
	
	public VueComponentHeaderItem getVueComponentHeaderItem() {
		IVueDescriptor vueDescriptor = getVueDescriptor();
		if(findParentVueBehavior()==null) {
			return (vueDescriptor!=null?vueDescriptor:VueJsonDescriptor.EMPTY_ROOT)
											.rootHeaderItem(getComponent().getMarkupId());
		} else {
			if(vueDescriptor==null) throw new WicketRuntimeException("VueDescriptor was not defined for component '"+getComponent().getId()+"'");
			return vueDescriptor.componentHeaderItem();
		}
	}
	
	public VueBehavior findParentVueBehavior() {
		MarkupContainer current = getComponent().getParent();
		
		while (current != null)
		{
			if (VueComponent.class.isInstance(current))
			{
				return VueComponent.class.cast(current).getVueBehavior();
			} else {
				VueBehavior ret = VuecketUtils.findVueBehavior(current);
				if(ret!=null) return ret;
			}

			current = current.getParent();
		}

		return null;
	}

	@Override
	protected void respond(AjaxRequestTarget target) {
		IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
		boolean async = params.getParameterValue("a").toBoolean();
		String method = params.getParameterValue("m").toString();
		String mailBoxId = params.getParameterValue("mb").toOptionalString();
		String arguments = params.getParameterValue("args").toString();
		IVuecketMethod.Context ctx = IVuecketMethod.contextFor(this, getComponent(), target, mailBoxId);
		
		
		try {
			ArrayNode argsNode = (ArrayNode) VueSettings.get().getObjectMapper().readTree(arguments);
			IVuecketMethod<?> m = lookupMethod(method);
			if(m==null) {
				LOG.warn("Vue method for '%s' was not found", method);
				return;
			}
			if(async) m.invoke(ctx, argsNode);
			else m.call(ctx, argsNode);
		} catch (Exception e) {
			throw new WicketRuntimeException(e);
		}
	}
	
	protected IVuecketMethod<?> lookupMethod(String methodName) {
		IVuecketMethod<?> ret = vueMethods.get(methodName);
		if(ret!=null) return ret;
		ret = vueOnMethods.get(methodName);
		if(ret!=null) return ret;
		ret = vueOnceMethods.get(methodName);
		return ret;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(VUE_JS));
		response.render(JavaScriptHeaderItem.forReference(HTTP_VUE_LOADER_JS));
		response.render(JavaScriptHeaderItem.forReference(VUECKET_JS));
		VueComponentHeaderItem vueComponentHeaderItem = getVueComponentHeaderItem();
		if(vueComponentHeaderItem!=null) response.render(vueComponentHeaderItem);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		try {
			String config = VueSettings.get().getObjectMapper().writeValueAsString(configView);
			tag.put("vc-config", config );
		} catch (JsonProcessingException e) {
			throw new WicketRuntimeException(e);
		}
	}
	
	public Map<String, IVuecketMethod<?>> getVueMethods() {
		return vueMethods;
	}
	
	public VueBehavior addVueMethod(String name, IVuecketMethod<?> vueMethod) {
		vueMethods.put(name, vueMethod);
		return this;
	}
	
	public Map<String, IVuecketMethod<?>> getVueOnMethods() {
		return vueOnMethods;
	}
	
	public VueBehavior addVueOnMethod(String name, IVuecketMethod<?> vueMethod) {
		vueOnMethods.put(name, vueMethod);
		return this;
	}
	
	public Map<String, IVuecketMethod<?>> getVueOnceMethods() {
		return vueOnceMethods;
	}
	
	public VueBehavior addVueOnceMethod(String name, IVuecketMethod<?> vueMethod) {
		vueOnceMethods.put(name, vueMethod);
		return this;
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		scanForAnnotations(getComponent());
		scanForAnnotations(this);
	}
	
	private void scanForAnnotations(Object object) {
		scanForAnnotation(object, VueMethod.class, vueMethods);
		scanForAnnotation(object, VueOn.class, vueOnMethods);
		scanForAnnotation(object, VueOnce.class, vueOnceMethods);
	}
	
	private void scanForAnnotation(Object object, Class<? extends Annotation> annotationClazz, Map<String, IVuecketMethod<?>> map) {
		List<Method> methods = VuecketUtils.getMethodsAnnotatedWith(object.getClass(), annotationClazz);
		for (Method method : methods) {
			Annotation annotation = method.getAnnotation(annotationClazz);
			String name = Strings.defaultIfEmpty(VuecketUtils.getAnnotationValue(annotation), method.getName());
			map.put(name, new ReflectionVuecketMethod<Object>(method));
		}
	}

}
