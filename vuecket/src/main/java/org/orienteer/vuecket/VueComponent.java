package org.orienteer.vuecket;

import java.util.Map;

import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;

public class VueComponent<T> extends GenericWebMarkupContainer<T> implements IVueComponent<VueComponent<T>> {
	
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

	@Override
	public VueComponent<T> getThisComponent() {
		return this;
	}
}
