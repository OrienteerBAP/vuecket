package org.orienteer.vuecket;

import java.util.Map;

import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;

/**
 * Wicket Component with prewired VueBehavior. It's absolutely optional to use this class or not. 
 * @param <T> the type of the component's model object
 */
public class VuePanel<T> extends GenericPanel<T> implements IVueComponent<VuePanel<T>> {
	
	private final VueBehavior vueBehavior;
	
	public VuePanel(String id) {
		this(id, null);
	}
	
	public VuePanel(String id, IModel<T> model) {
		super(id, model);
		add(vueBehavior = new VueBehavior());
	}
	
	public VueBehavior getVueBehavior() {
		return vueBehavior;
	}

	@Override
	public VuePanel<T> getThisComponent() {
		return this;
	}
}
