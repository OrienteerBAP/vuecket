package org.orienteer.vuecket;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.util.IVueComponentHeaderItemSupplier;

public class VueComponent<T> extends GenericWebMarkupContainer<T> implements IVueComponentHeaderItemSupplier {
	
	private VueDescriptor vueDescriptor;
	
	public VueComponent(String id) {
		this(id, null);
	}
	
	public VueComponent(String id, IModel<?> model) {
		super(id, model);
		Vue vue = this.getClass().getAnnotation(Vue.class);
		if(vue!=null) setVueDescriptor(new VueDescriptor(vue));
		add(new VueBehavior(this));
	}
	
	public VueDescriptor getVueDescriptor() {
		return vueDescriptor;
	}
	
	public VueComponent<T> setVueDescriptor(VueDescriptor vueDescriptor) {
		this.vueDescriptor = vueDescriptor;
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(String name, String vueDescriptor) {
		this.vueDescriptor = new VueDescriptor(name, vueDescriptor);
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(String vueDescriptor) {
		this.vueDescriptor = new VueDescriptor(vueDescriptor);
		return this;
	}
	
	public VueComponent<T> setVueDescriptor(ResourceReference reference) {
		this.vueDescriptor = new VueDescriptor(reference);
		return this;
	}

	@Override
	public VueComponentHeaderItem getVueComponentHeaderItem() {
		if(vueDescriptor==null) throw new WicketRuntimeException("VueDescriptor was not defined for component '"+getId()+"'");
		if(findParent(VueComponent.class)==null) {
			return VueComponentHeaderItem.forRootApp(getMarkupId(), vueDescriptor);
		} else {
			return VueComponentHeaderItem.forComponent(vueDescriptor);
		}
	}
}
