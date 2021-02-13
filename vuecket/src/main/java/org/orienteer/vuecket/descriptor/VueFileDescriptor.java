package org.orienteer.vuecket.descriptor;

import org.apache.wicket.Component;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.Strings;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;
import org.orienteer.vuecket.util.VuecketUtils;

/**
 * {@link IVueDescriptor} for association of {@link Component} with some *.vue file
 */
public class VueFileDescriptor implements IVueDescriptor {
	
	private String name;
	private ResourceReference reference;
	
	public VueFileDescriptor(VueFile vueJson, Class<?> baseClass) {
		this(extractName(vueJson), new PackageResourceReference(baseClass, vueJson.value()));
	}
	
	public VueFileDescriptor(ResourceReference reference) {
		this(reference.getName(), reference);
	}
	
	public VueFileDescriptor(String name, ResourceReference reference) {
		this.name = name;
		this.reference = reference;
	}
	
	private static String extractName(VueFile vueFile) {
		return !Strings.isEmpty(vueFile.name())?vueFile.name():VuecketUtils.removeSuffix(vueFile.value(), ".vue");
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public ResourceReference getReference() {
		return reference;
	}
	
	public static VueFileDescriptor create(Class<?> clazz) {
		Class<?> compClazz = VuecketUtils.findClassWithAnnotation(clazz, VueFile.class);
		if(compClazz==null) return null;
		else return new VueFileDescriptor(compClazz.getAnnotation(VueFile.class), compClazz);
	}

	@Override
	public VueComponentHeaderItem rootHeaderItem(String elementId) {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "Vue.loadRootVue('%s', '#%s')";
		javaScript = String.format(javaScript, 
										JavaScriptUtils.escapeQuotes(resourceURL),
										JavaScriptUtils.escapeQuotes(elementId));
		return new VueComponentHeaderItem("#"+elementId, javaScript, true);
	}

	@Override
	public VueComponentHeaderItem componentHeaderItem() {
		CharSequence resourceURL = RequestCycle.get().urlFor(reference, null);
		String javaScript = "Vue.loadComponentVue('%s', '%s')";
		javaScript = String.format(javaScript, 
										JavaScriptUtils.escapeQuotes(resourceURL),
										JavaScriptUtils.escapeQuotes(getName()));
		return new VueComponentHeaderItem(getName(), javaScript, false);
	}
}
