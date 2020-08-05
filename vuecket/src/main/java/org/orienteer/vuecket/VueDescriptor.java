package org.orienteer.vuecket;

import org.apache.wicket.request.resource.PackageResourceReference;
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
	
	public VueDescriptor(VueComponent<?> comp, Vue vue) {
		this.name = vue.name();
		this.type = vue.type();
		this.value = vue.value();
		if(type==null || Type.AUTODETECT.equals(type)) this.type = guessType(value, null);
		if(Type.VUE.equals(type)) {
			reference = new PackageResourceReference(comp.getClass(), value);
			value = null;
		}
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
