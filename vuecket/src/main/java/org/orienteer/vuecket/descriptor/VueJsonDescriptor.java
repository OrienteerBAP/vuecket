package org.orienteer.vuecket.descriptor;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;
import org.orienteer.vuecket.util.VuecketUtils;

public class VueJsonDescriptor implements IVueDescriptor {
	
	public static final VueJsonDescriptor EMPTY_ROOT = new VueJsonDescriptor("{}");
	
	private String name;
	private String json;
	
	public VueJsonDescriptor(VueJson vueJson) {
		this(vueJson.name(), vueJson.json());
	}
	
	public VueJsonDescriptor(String json) {
		this(VuecketUtils.randomId(), json);
	}
	
	public VueJsonDescriptor(String name, String json) {
		this.name = name;
		this.json = json;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getJson() {
		return json;
	}
	
	public static VueJsonDescriptor create(Class<?> clazz) {
		VueJson vueJson = VuecketUtils.findAnnotation(clazz, VueJson.class);
		if(vueJson==null) return null;
		else return new VueJsonDescriptor(vueJson);
	}

	@Override
	public VueComponentHeaderItem rootHeaderItem(String elementId) {
		String javaScript = "new Vue("+getJson()+").$mount('#"+JavaScriptUtils.escapeQuotes(elementId)+"');";
		return new VueComponentHeaderItem("#"+elementId, javaScript, true);
	}

	@Override
	public VueComponentHeaderItem componentHeaderItem() {
		String javaScript = "Vue.component('"+JavaScriptUtils.escapeQuotes(getName())+"' , "+getJson()+");";
		return new VueComponentHeaderItem(getName(), javaScript, false);
	}
}
