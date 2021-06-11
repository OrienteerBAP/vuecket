package org.orienteer.vuecket.npmprovider;

import org.apache.wicket.request.resource.ResourceReference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * NPM Provider which use embedded NPM packages by WebJars.<br/>
 * <b>Important!</b><ul>
 * <li>WebJars is optional(scope=provided) dependency for Vuecket so should be included into your pom.xml</li>
 * <li>Sometimes package name in WebJars is not equal to NPM package name. You should use WebJars notation.</li>
 * <li>Don't forget specify direct filepath: WebJars doesn't provide default JS resource for a package</li>
 * </ul>
 */
public class WebJarsNPMPackageProvider implements INPMPackageProvider{
	
	public static final INPMPackageProvider WEBJARS_PROVIDER = new WebJarsNPMPackageProvider();

	private static final long serialVersionUID = 1L;

	@Override
	public ResourceReference provide(String npmPackage, String filePath) {
		int versionIndx = npmPackage.lastIndexOf('@');
		String version = "current";
		if(versionIndx>0) {
			version = npmPackage.substring(versionIndx+1);
			npmPackage = npmPackage.substring(0, versionIndx);
		}
		return new WebjarsJavaScriptResourceReference(String.format("%s/%s/%s", npmPackage, version, filePath));
	}

}
