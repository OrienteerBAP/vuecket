package org.orienteer.vuecket;

import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;

public class VueComponent<T> extends GenericWebMarkupContainer<T> {
	
	private final VueBehavior vueBehavior;
	
	public VueComponent(String id) {
		this(id, null);
	}
	
	public VueComponent(String id, IModel<?> model) {
		super(id, model);
		add(vueBehavior = new VueBehavior());
	}
	
	public VueBehavior getVueBehavior() {
		return vueBehavior;
	}
	
	public IVueDescriptor getVueDescriptor() {
		return getVueBehavior().getVueDescriptor();
	}
	
	public VueComponent<T> setVueDescriptor(IVueDescriptor vueDescriptor) {
		getVueBehavior().setVueDescriptor(vueDescriptor);
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(String name, String vueDescriptor) {
		return setVueDescriptor(new VueJsonDescriptor(name, vueDescriptor));
	}
	
	public VueComponent<T> setVueDescriptor(String vueDescriptor) {
		return setVueDescriptor(new VueJsonDescriptor(vueDescriptor));
	}
	
	public VueComponent<T> setVueDescriptor(ResourceReference reference) {
		return setVueDescriptor(new VueFileDescriptor(reference));
	}

}
