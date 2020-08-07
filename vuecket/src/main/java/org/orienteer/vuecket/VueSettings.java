package org.orienteer.vuecket;

import java.util.function.Function;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.Strings;

public class VueSettings {
	
	public static final INPMPackageProvider UNPKG_PROVIDER = new NPMCDNPackageProvider("https://unpkg.com/%s/%s");
	public static final INPMPackageProvider JSDELIVR_PROVIDER = new NPMCDNPackageProvider("https://cdn.jsdelivr.net/npm/%s/%s");
	
	private static final MetaDataKey<VueSettings> VUE_SETTINGS = new MetaDataKey<VueSettings>() {
	};
	
	public static interface INPMPackageProvider extends IClusterable {
		public ResourceReference provide(String npmPackage, String filePath);
		
		public default ResourceReference provide(String npmPackage) {
			return provide(npmPackage, null);
		}
	}
	
	public static class NPMCDNPackageProvider implements INPMPackageProvider {
		
		private String packageFormat;
		private String packageWithFileFormat;
		
		public NPMCDNPackageProvider(String format) {
			this(null, format);
		}
		
		public NPMCDNPackageProvider(String packageFormat, String packageWithFileFormat) {
			this.packageFormat = packageFormat;
			this.packageWithFileFormat = packageWithFileFormat;
		}
		
		@Override
		public ResourceReference provide(String npmPackage, String file) {
			String url=null;
			if(Strings.isEmpty(file) && packageFormat!=null) url = String.format(packageFormat, npmPackage);
			else url = String.format(packageWithFileFormat, npmPackage, Strings.defaultIfEmpty(file, ""));
			return new UrlResourceReference(Url.parse(url));
		}
		
	}
	
	private INPMPackageProvider npmPackageProvider;
	
	protected VueSettings() {
		npmPackageProvider = UNPKG_PROVIDER;
	}
	
	public INPMPackageProvider getNpmPackageProvider() {
		return npmPackageProvider;
	}

	public void setNpmPackageProvider(INPMPackageProvider npmPackageProvider) {
		this.npmPackageProvider = npmPackageProvider;
	}

	public static VueSettings defaultSettings() {
		return new VueSettings();
	}
	
	public static VueSettings get() {
		return get(Application.get());
	}
	
	public static VueSettings get(Application app) {
		VueSettings settings = app.getMetaData(VUE_SETTINGS);
		if(settings==null) {
			set(app, settings = defaultSettings());
		}
		return settings;
	}
	
	public static void set(VueSettings settings) {
		set(Application.get(), settings);
	}
	
	public static void set(Application app, VueSettings settings) {
		app.setMetaData(VUE_SETTINGS, settings);
	}
	
}