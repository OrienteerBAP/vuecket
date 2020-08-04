package org.orienteer.vuecket;

import java.util.Collections;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.bundles.IResourceBundle;

public class VueComponentHeaderItem extends HeaderItem {
	
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
