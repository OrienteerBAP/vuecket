package org.orienteer.vuecket;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.Strings;

public class VueDescriptor implements IClusterable {
	public static enum Type {
		JSON, VUE, NPM, AUTODETECT
	}
	
	private String name;
	private Type type;
	private String value;
	private ResourceReference reference;
	
	public VueDescriptor(Vue vue) {
		this(vue.name(), vue.type(), vue.value());
	}
	
	public VueDescriptor(String value) {
		this(null, null, value);
	}
	
	public VueDescriptor(ResourceReference reference) {
		this(reference.getName(), null, null, reference);
	}
	
	public VueDescriptor(String name, String value) {
		this(name, null, value);
	}
	
	private VueDescriptor(String name, Type type, String value) {
		this(name, type, value, null);
	}
	
	private VueDescriptor(String name, Type type, String value, ResourceReference reference) {
		this.name = Strings.defaultIfEmpty(name, null);
		this.type = type;
		this.value = value;
		this.reference = reference;
		if(type==null || Type.AUTODETECT.equals(type)) this.type = guessType(value, reference);
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
	
	public ResourceReference getReference() {
		return reference;
	}
	
	public static Type guessType(String value, ResourceReference reference) {
		if(value==null) {
			if(reference ==null) return null;
			else return Type.VUE; //TODO should support NPM as well
		}
		value = value.trim().toLowerCase();
		if(value.startsWith("{")) return Type.JSON;
		else if(value.endsWith(".vue")) return Type.VUE;
		else return Type.NPM;
	}
}
