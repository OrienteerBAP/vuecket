package org.orienteer.vuecket;

import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.Strings;

public class VueDescriptor implements IClusterable {
	public static enum Type {
		JSON, VUE, NPM, AUTODETECT
	}
	
	private String name;
	private Type type;
	private String value;
	
	public VueDescriptor(Vue vue) {
		this(vue.name(), vue.type(), vue.value());
	}
	
	public VueDescriptor(String value) {
		this(null, null, value);
	}
	
	public VueDescriptor(String name, String value) {
		this(name, null, value);
	}
	
	public VueDescriptor(String name, Type type, String value) {
		this.name = Strings.defaultIfEmpty(name, null);
		this.type = type;
		this.value = value;
		if(type==null || Type.AUTODETECT.equals(type)) this.type = guessType(value);
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Type guessType(String value) {
		if(value==null) return null;
		value = value.trim().toLowerCase();
		if(value.startsWith("{")) return Type.JSON;
		else if(value.endsWith(".vue")) return Type.VUE;
		else return Type.NPM;
	}
}
