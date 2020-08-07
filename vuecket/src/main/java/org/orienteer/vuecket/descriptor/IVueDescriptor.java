package org.orienteer.vuecket.descriptor;


import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;

public interface IVueDescriptor {
	
	public String getName();
	
	public VueComponentHeaderItem rootHeaderItem(String elementId);
	public VueComponentHeaderItem componentHeaderItem();

	public static IVueDescriptor lookupDescriptor(VueComponent<?> comp) {
		Class<? extends VueComponent<?>> clazz = (Class<? extends VueComponent<?>>) comp.getClass();
		IVueDescriptor ret = VueJsonDescriptor.create(clazz);
		if(ret!=null) return ret;
		ret = VueFileDescriptor.create(clazz);
		if(ret!=null) return ret;
		ret = VueNpmDescriptor.create(clazz);
		if(ret!=null) return ret;
		return ret;
	}
}
