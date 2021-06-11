package org.orienteer.vuecket;

import java.util.function.Function;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.orienteer.vuecket.npmprovider.INPMPackageProvider;
import org.orienteer.vuecket.npmprovider.CDNNPMPackageProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Holder of Vuecket related settings 
 */
public class VueSettings {
	
	private static final MetaDataKey<VueSettings> VUE_SETTINGS = new MetaDataKey<VueSettings>() {
	};
	
	private INPMPackageProvider npmPackageProvider;
	private ObjectMapper objectMapper;
	private int defaultRefreshPeriod;
	
	protected VueSettings() {
		npmPackageProvider = CDNNPMPackageProvider.UNPKG_PROVIDER;
		objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		defaultRefreshPeriod = 15;
	}
	
	public INPMPackageProvider getNpmPackageProvider() {
		return npmPackageProvider;
	}

	public VueSettings setNpmPackageProvider(INPMPackageProvider npmPackageProvider) {
		this.npmPackageProvider = npmPackageProvider;
		return this;
	}
	
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public VueSettings setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		return this;
	}
	
	public int getDefaultRefreshPeriod() {
		return defaultRefreshPeriod;
	}
	
	public VueSettings setDefaultRefreshPeriod(int defaultRefreshPeriod) {
		this.defaultRefreshPeriod = defaultRefreshPeriod;
		return this;
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
