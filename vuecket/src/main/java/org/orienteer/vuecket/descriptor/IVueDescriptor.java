package org.orienteer.vuecket.descriptor;


import org.apache.wicket.Component;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;

/**
 * Descriptor for Vue related resources. Responsible for providing {@link VueComponentHeaderItem}
 */
public interface IVueDescriptor extends IClusterable {
	
	/**
	 * Provides name of the vue resource. Commonly used for de-duplication of loaded resources to browser.
	 * @return name of this resource
	 */
	public String getName();
	
	/**
	 * Provides {@link VueComponentHeaderItem} for root Vue component on a page
	 * @param elementId - rendered id of element to bind component to
	 * @return header item to be rendered
	 */
	public VueComponentHeaderItem rootHeaderItem(String elementId);
	
	/**
	 * Provides {@link VueComponentHeaderItem} for child Vue component. 
	 * Child components commonly have their own specific HTML tag
	 * @return header item to be rendered
	 */
	public VueComponentHeaderItem componentHeaderItem();

	/**
	 * Factory-like method to parse {@link Component} and try to find some descriptor
	 * @param comp component to scan
	 * @return descriptor if found
	 */
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
