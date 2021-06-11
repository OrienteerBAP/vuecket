package org.orienteer.vuecket.npmprovider;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.IVueBehaviorLocator;
import org.orienteer.vuecket.descriptor.VueNpmDescriptor;

/**
 * Interface for providers of NPM package resource {@link ResourceReference}
 */
public interface INPMPackageProvider extends IClusterable {
	public ResourceReference provide(String npmPackage, String filePath);
	
	public default ResourceReference provide(String npmPackage) {
		return provide(npmPackage, null);
	}
	
	public default void setAsCustomNPMPackageProviderFor(IVueBehaviorLocator locator) {
		VueNpmDescriptor.setCustomNpmPackageProvider(locator, this);
	}
}