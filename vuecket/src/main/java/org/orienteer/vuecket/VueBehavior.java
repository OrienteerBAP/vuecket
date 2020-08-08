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
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.util.VuecketUtils;

public class VueBehavior extends AbstractDefaultAjaxBehavior {
	
	public static final JavaScriptResourceReference VUE_JS = new JavaScriptResourceReference(VueComponent.class, "external/vue/dist/vue.js");
	public static final JavaScriptResourceReference HTTP_VUE_LOADER_JS = new JavaScriptResourceReference(VueComponent.class, "external/http-vue-loader/src/httpVueLoader.js");
	public static final JavaScriptResourceReference VUECKET_JS = new JavaScriptResourceReference(VueComponent.class, "vuecket.js");
	
	private IVueDescriptor vueDescriptor;
	
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
		// TODO Auto-generated method stub
		
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
		
		tag.put(":vuecket", StringValue.valueOf("'"+JavaScriptUtils.escapeQuotes(getCallbackUrl())+"'"));
	}

}
