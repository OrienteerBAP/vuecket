package org.orienteer.vuecket;

import java.util.Map;

import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;

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
	
	
	public Map<String, IVuecketMethod<?>> getVueMethods() {
		return getVueBehavior().getVueMethods();
	}
	
	public VueComponent<T> addVueMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueMethod(name, vueMethod);
		return this;
	}
	
	public Map<String, IVuecketMethod<?>> getVueOnMethods() {
		return getVueBehavior().getVueOnMethods();
	}
	
	public VueComponent<T> addVueOnMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueOnMethod(name, vueMethod);
		return this;
	}
	
	public Map<String, IVuecketMethod<?>> getVueOnceMethods() {
		return getVueBehavior().getVueOnceMethods();
	}
	
	public VueComponent<T> addVueOnceMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueOnceMethod(name, vueMethod);
		return this;
	}
	
	public Map<String, IVuecketMethod<?>> getVueWatchMethods() {
		return getVueBehavior().getVueWatchMethods();
	}
	
	public VueComponent<T> addVueWatchMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueWatchMethod(name, vueMethod);
		return this;
	}
	
	public <M> VueComponent<T> addDataFiber(String name) {
		getVueBehavior().addDataFiber(name, getDefaultModel());
		return this;
	}
	
	public <M> VueComponent<T> addDataFiber(String name, boolean load, boolean observe) {
		getVueBehavior().addDataFiber(name, getDefaultModel(), load, observe);
		return this;
	}
	
	public <M> VueComponent<T> addDataFiber(String name, IModel<M> model) {
		getVueBehavior().addDataFiber(name, model);
		return this;
	}
	
	public <M> VueComponent<T> addDataFiber(String name, IModel<M> model, boolean load, boolean observe) {
		getVueBehavior().addDataFiber(name, model, load, observe);
		return this;
	}
	
	public <M> VueComponent<T> removeDataFiber(String name) {
		getVueBehavior().removeDataFiber(name);
		return this;
	}
	
	public <M> VueComponent<T> removeDataFiber(String name, boolean load, boolean observe) {
		getVueBehavior().removeDataFiber(name, load, observe);
		return this;
	}

}
