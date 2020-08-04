package org.orienteer.vuecket;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.orienteer.vuecket.util.IVueComponentHeaderItemSupplier;

public class VueComponent<T> extends GenericWebMarkupContainer<T> implements IVueComponentHeaderItemSupplier {
	
	public VueComponent(String id) {
		this(id, null);
	}
	
	public VueComponent(String id, IModel<?> model) {
		super(id, model);
		add(new VueBehavior(this));
	}

	@Override
	public VueComponentHeaderItem getVueComponentHeaderItem() {
		return null;
	}
}
