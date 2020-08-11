package org.orienteer.vuecket;


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
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;
import org.orienteer.vuecket.util.VuecketUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class VueBehavior extends AbstractDefaultAjaxBehavior {
	
	public static final JavaScriptResourceReference VUE_JS = new JavaScriptResourceReference(VueComponent.class, "external/vue/dist/vue.js");
	public static final JavaScriptResourceReference HTTP_VUE_LOADER_JS = new JavaScriptResourceReference(VueComponent.class, "external/http-vue-loader/src/httpVueLoader.js");
	public static final JavaScriptResourceReference VUECKET_JS = new JavaScriptResourceReference(VueComponent.class, "vuecket.js");
	
	private class VueConfigView implements IClusterable {
		public CharSequence getUrl() {
			return getCallbackUrl();
		}
	}
	
	private IVueDescriptor vueDescriptor;
	
	private final VueConfigView configView = new VueConfigView();
	
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
		String mailBoxId = params.getParameterValue("mb").toString();
		String arguments = params.getParameterValue("args").toString();
		IVuecketMethod.Context ctx = IVuecketMethod.contextFor(this, getComponent(), target, mailBoxId);
		
		
		try {
			ArrayNode argsNode = (ArrayNode) VueSettings.get().getObjectMapper().readTree(arguments);
			System.out.println("recieved arguments: "+argsNode);
			IVuecketMethod<?> m = (context, args) -> args; //Just Echo for now
			if(async) m.invoke(ctx, argsNode);
			else m.call(ctx, argsNode);
		} catch (Exception e) {
			throw new WicketRuntimeException(e);
		}
//		System.out.println("Method = "+arguments);
//		System.out.println("Arguments = "+arguments);
//		target.appendJavaScript("Vue.getVueById('"+getComponent().getMarkupId()+"').vcApply({server:'hello from server'})");
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

}
