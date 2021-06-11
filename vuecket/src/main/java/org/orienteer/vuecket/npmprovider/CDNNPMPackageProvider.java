package org.orienteer.vuecket.npmprovider;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.Strings;
import org.orienteer.vuecket.util.VuecketUtils;

/**
 * CDN based {@link INPMPackageProvider}
 */
public class CDNNPMPackageProvider implements INPMPackageProvider {
	
	public static final INPMPackageProvider JSDELIVR_PROVIDER = new CDNNPMPackageProvider("https://cdn.jsdelivr.net/npm/%s/%s");
	public static final INPMPackageProvider UNPKG_PROVIDER = new CDNNPMPackageProvider("https://unpkg.com/%s/%s");
	public static final INPMPackageProvider WICKET_JNPM_PROVIDER = new CDNNPMPackageProvider("/cdn/%s/%s");

	private String packageFormat;
	private String packageWithFileFormat;
	
	public CDNNPMPackageProvider(String format) {
		this(null, format);
	}
	
	public CDNNPMPackageProvider(String packageFormat, String packageWithFileFormat) {
		this.packageFormat = packageFormat;
		this.packageWithFileFormat = packageWithFileFormat;
	}
	
	@Override
	public ResourceReference provide(String npmPackage, String file) {
		String url=null;
		if(Strings.isEmpty(file) && packageFormat!=null) url = String.format(packageFormat, npmPackage);
		else url = String.format(packageWithFileFormat, npmPackage, Strings.defaultIfEmpty(file, ""));
		if(packageFormat==null && Strings.isEmpty(file)) url = VuecketUtils.removeSuffix(url, "/");
		return new UrlResourceReference(Url.parse(url));
	}
	
}