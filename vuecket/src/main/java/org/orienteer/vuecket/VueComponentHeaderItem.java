package org.orienteer.vuecket;

import java.util.Collections;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.bundles.IResourceBundle;

public class VueComponentHeaderItem extends HeaderItem {
	
	public static VueComponentHeaderItem forComponent(VueDescriptor vueDescriptor) {
		switch(vueDescriptor.getType()) {
			case JSON:
				return forComponentDescriptor(vueDescriptor.getName(), vueDescriptor.getValue());
			case VUE:
				return forComponentVue(vueDescriptor.getName(), vueDescriptor.getReference());
		}
		throw new WicketRuntimeException("This type of descriptor not yet supported: "+vueDescriptor.getType());
	}
	
	public static VueComponentHeaderItem forRootApp(String elId, VueDescriptor vueDescriptor) {
		switch(vueDescriptor.getType()) {
			case JSON:
				return forRootAppDescriptor(elId, vueDescriptor.getValue());
			case VUE:
				return forRootAppVue(elId, vueDescriptor.getReference());
		}
		throw new WicketRuntimeException("This type of descriptor not yet supported: "+vueDescriptor.getType());
	}
	
	public static VueComponentHeaderItem forComponentDescriptor(String componentName, String descriptor) {
		String javaScript = "Vue.component('"+JavaScriptUtils.escapeQuotes(componentName)+"' , "+descriptor+");";
		return new VueComponentHeaderItem(componentName,
						new JavaScriptContentHeaderItem(javaScript, componentName, null));
	}
	
	public static VueComponentHeaderItem forRootAppDescriptor(String elId, String descriptor) {
		String javaScript = "new Vue("+descriptor+").$mount('#"+JavaScriptUtils.escapeQuotes(elId)+"');";
		return new VueComponentHeaderItem("#"+elId,
						OnDomReadyHeaderItem.forScript(javaScript));
	}
	
	public static VueComponentHeaderItem forComponentVue(String componentName, ResourceReference reference) {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "httpVueLoader.register(Vue, '"+resourceURL+"');";
		return new VueComponentHeaderItem(componentName,
						new JavaScriptContentHeaderItem(javaScript, componentName, null));
	}
	
	public static VueComponentHeaderItem forRootAppVue(String elId, ResourceReference reference) {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "httpVueLoader.load('"+resourceURL+"')()"
							+ ".then(function(d){new Vue(d).$mount('#"+JavaScriptUtils.escapeQuotes(elId)+"');})"; 
		/*String javaScript = "var prom = httpVueLoader.load('"+resourceURL+"')();\r\n" + 
				"	var descr = await prom;\r\n" + 
				"	new Vue(descr).$mount('#"+JavaScriptUtils.escapeQuotes(elId)+"');";*/
		return new VueComponentHeaderItem("#"+elId,
						OnDomReadyHeaderItem.forScript(javaScript));
	}
	
	private String componentName;
	
	private HeaderItem delegate;
	
	protected VueComponentHeaderItem(String componentName, HeaderItem delegate) {
		this.componentName = componentName;
		this.delegate = delegate;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	@Override
	public Iterable<? extends HeaderItem> getProvidedResources()
	{
		if (delegate!=null)
			return Collections.singletonList(delegate);
		return super.getProvidedResources();
	}

	@Override
	public Iterable<?> getRenderTokens() {
		return Collections.singletonList(componentName);
	}

	@Override
	public void render(Response response) {
		if(delegate!=null) delegate.render(response);
	}
	
}
