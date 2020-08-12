package org.orienteer.vuecket.descriptor;


import org.apache.wicket.Component;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;

public interface IVueDescriptor extends IClusterable {
	
	public String getName();
	
	public VueComponentHeaderItem rootHeaderItem(String elementId);
	public VueComponentHeaderItem componentHeaderItem();

	public static IVueDescriptor lookupDescriptor(Component comp) {
		Class<?> clazz = comp.getClass();
		IVueDescriptor ret = VueJsonDescriptor.create(clazz);
		if(ret!=null) return ret;
		ret = VueFileDescriptor.create(clazz);
		if(ret!=null) return ret;
		ret = VueNpmDescriptor.create(clazz);
		if(ret!=null) return ret;
		return ret;
	}
}
