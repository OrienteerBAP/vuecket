package org.orienteer.vuecket.descriptor;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.Response;
import org.orienteer.vuecket.IVueBehaviorLocator;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;
import org.orienteer.vuecket.VueSettings;
import org.orienteer.vuecket.npmprovider.INPMPackageProvider;
import org.orienteer.vuecket.util.VuecketUtils;

/**
 * {@link IVueDescriptor} for association of {@link Component} with some Vue.JS NPM package
 */
public class VueNpmDescriptor implements IVueDescriptor {
	
	private String packageName;
	private String path;
	private String enablementScript;
	private INPMPackageProvider npmPackageProvider;
	
	public VueNpmDescriptor(VueNpm vueNpm) {
		this(vueNpm.packageName(), vueNpm.path(), vueNpm.enablement());
	}
	
	public VueNpmDescriptor(String packageName, String path, String enablementScript) {
		this.packageName = packageName;
		this.path = path;
		this.enablementScript = enablementScript;
	}
	
	@Override
	public String getName() {
		return packageName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public String getPath() {
		return path;
	}

	public String getEnablementScript() {
		return enablementScript;
	}
	
	@Override
	public VueComponentHeaderItem rootHeaderItem(String elementId) {
		throw new WicketRuntimeException("NPM based vue descriptor is not supported for root Vue elements");
	}
	
	public INPMPackageProvider getNpmPackageProvider() {
		return npmPackageProvider!=null?npmPackageProvider:VueSettings.get().getNpmPackageProvider();
	}
	
	public void setNpmPackageProvider(INPMPackageProvider npmPackageProvider) {
		this.npmPackageProvider = npmPackageProvider;
	}

	@Override
	public VueComponentHeaderItem componentHeaderItem() {
		return new VueComponentHeaderItem(getPackageName(), 
						JavaScriptHeaderItem.forReference(
								getNpmPackageProvider().provide(getPackageName(), getPath()), getPackageName()),
						OnDomReadyHeaderItem.forScript(getEnablementScript()));
	}

	public static VueNpmDescriptor create(Class<?> clazz) {
		VueNpm vueNpm = VuecketUtils.findAnnotation(clazz, VueNpm.class);
		if(vueNpm==null) return null;
		else return new VueNpmDescriptor(vueNpm);
	}
	
	public static <T extends IVueBehaviorLocator> T setCustomNpmPackageProvider(T locator, INPMPackageProvider npmPackageProvider) {
		IVueDescriptor descriptor = locator.getVueBehavior().getVueDescriptor();
		if(descriptor instanceof VueNpmDescriptor) {
			((VueNpmDescriptor)descriptor).setNpmPackageProvider(npmPackageProvider);
		}
		return locator;
	}
}
