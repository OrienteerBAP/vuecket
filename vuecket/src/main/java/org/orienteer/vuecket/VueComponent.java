package org.orienteer.vuecket;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.util.IVueComponentHeaderItemSupplier;

public class VueComponent<T> extends GenericWebMarkupContainer<T> implements IVueComponentHeaderItemSupplier {
	
	private IVueDescriptor vueDescriptor;
	
	public VueComponent(String id) {
		this(id, null);
	}
	
	public VueComponent(String id, IModel<?> model) {
		super(id, model);
		vueDescriptor = IVueDescriptor.lookupDescriptor(this);
		add(new VueBehavior(this));
	}
	
	public IVueDescriptor getVueDescriptor() {
		return vueDescriptor;
	}
	
	public VueComponent<T> setVueDescriptor(IVueDescriptor vueDescriptor) {
		this.vueDescriptor = vueDescriptor;
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(String name, String vueDescriptor) {
		this.vueDescriptor = new VueJsonDescriptor(name, vueDescriptor);
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(String vueDescriptor) {
		this.vueDescriptor = new VueJsonDescriptor(vueDescriptor);
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(ResourceReference reference) {
		this.vueDescriptor = new VueFileDescriptor(reference);
		return this;
	}

	@Override
	public VueComponentHeaderItem getVueComponentHeaderItem() {
		if(vueDescriptor==null) throw new WicketRuntimeException("VueDescriptor was not defined for component '"+getId()+"'");
		if(findParent(VueComponent.class)==null) {
			return vueDescriptor.rootHeaderItem(getMarkupId());
		} else {
			return vueDescriptor.componentHeaderItem();
		}
	}
}
