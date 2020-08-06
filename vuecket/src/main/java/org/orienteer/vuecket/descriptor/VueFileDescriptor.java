package org.orienteer.vuecket.descriptor;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;
import org.orienteer.vuecket.util.VuecketUtils;

public class VueFileDescriptor implements IVueDescriptor {
	
	private String name;
	private ResourceReference reference;
	
	public VueFileDescriptor(VueFile vueJson, Class<?> baseClass) {
		this(new PackageResourceReference(baseClass, vueJson.value()));
	}
	
	public VueFileDescriptor(ResourceReference reference) {
		this(reference.getName(), reference);
	}
	
	public VueFileDescriptor(String name, ResourceReference reference) {
		this.name = name;
		this.reference = reference;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public ResourceReference getReference() {
		return reference;
	}
	
	public static VueFileDescriptor create(Class<? extends VueComponent<?>> clazz) {
		Class<?> compClazz = VuecketUtils.findClassWithAnnotation(clazz, VueFile.class);
		if(compClazz==null) return null;
		else return new VueFileDescriptor(compClazz.getAnnotation(VueFile.class), compClazz);
	}

	@Override
	public VueComponentHeaderItem rootHeaderItem(String elementId) {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "httpVueLoader.load('"+resourceURL+"')()"
							+ ".then(function(d){new Vue(d).$mount('#"+JavaScriptUtils.escapeQuotes(elementId)+"');})"; 
		return new VueComponentHeaderItem("#"+elementId, javaScript, true);
	}

	@Override
	public VueComponentHeaderItem componentHeaderItem() {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "httpVueLoader.register(Vue, '"+resourceURL+"');";
		return new VueComponentHeaderItem(getName(), javaScript, true);
	}
}
