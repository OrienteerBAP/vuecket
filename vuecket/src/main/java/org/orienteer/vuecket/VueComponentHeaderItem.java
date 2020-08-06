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
	
	private String componentName;
	
	private HeaderItem delegate;
	
	public VueComponentHeaderItem(String componentName, String javaScript, boolean onDomReady) {
		this(componentName, onDomReady ? OnDomReadyHeaderItem.forScript(javaScript)
									   : new JavaScriptContentHeaderItem(javaScript, componentName, null));
	}
	
	public VueComponentHeaderItem(String componentName, HeaderItem delegate) {
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
