package org.orienteer.vuecket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.Response;

public class VueComponentHeaderItem extends HeaderItem {
	
	private String componentName;
	
	private List<HeaderItem> resources;
	
	public VueComponentHeaderItem(String componentName, String javaScript, boolean onDomReady) {
		this(componentName, onDomReady ? OnDomReadyHeaderItem.forScript(javaScript)
									   : new JavaScriptContentHeaderItem(javaScript, componentName, null));
	}
	
	public VueComponentHeaderItem(String componentName, HeaderItem... resources) {
		this.componentName = componentName;
		this.resources = Arrays.asList(resources);
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	@Override
	public Iterable<? extends HeaderItem> getProvidedResources()
	{
		if (resources!=null)
			return Collections.unmodifiableList(resources);
		return super.getProvidedResources();
	}

	@Override
	public Iterable<?> getRenderTokens() {
		return Collections.singletonList(componentName);
	}

	@Override
	public void render(Response response) {
		if(resources!=null) {
			for (HeaderItem headerItem : resources) {
				headerItem.render(response);
			}
		}
	}
	
}
