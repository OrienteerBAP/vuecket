package org.orienteer.vuecket;


import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.orienteer.vuecket.util.IVueComponentHeaderItemSupplier;

public class VueBehavior extends AbstractDefaultAjaxBehavior {
	
	public static final JavaScriptResourceReference VUE_JS = new JavaScriptResourceReference(VueComponent.class, "external/vue/dist/vue.js");
	public static final JavaScriptResourceReference HTTP_VUE_LOADER_JS = new JavaScriptResourceReference(VueComponent.class, "external/http-vue-loader/src/httpVueLoader.js");
	public static final JavaScriptResourceReference VUECKET_JS = new JavaScriptResourceReference(VueComponent.class, "vuecket.js");
	
	private VueComponentHeaderItem vueComponentHeaderItem;
	
	private IVueComponentHeaderItemSupplier vueComponentHeaderItemSupplier;
	
	public VueBehavior(VueComponentHeaderItem vueComponentHeaderItem) {
		this.vueComponentHeaderItem = vueComponentHeaderItem;
	}
	
	public VueBehavior(IVueComponentHeaderItemSupplier vueComponentHeaderItemSupplier) {
		this.vueComponentHeaderItemSupplier = vueComponentHeaderItemSupplier;
	}
	
	public VueComponentHeaderItem getVueComponentHeaderItem() {
		return vueComponentHeaderItem!=null?vueComponentHeaderItem
										   : (vueComponentHeaderItemSupplier!=null
										   			?vueComponentHeaderItemSupplier.getVueComponentHeaderItem()
										   			:null);
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
