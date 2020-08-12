package org.orienteer.vuecket;

import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;

public interface IVueComponent<C> {
	
	public VueBehavior getVueBehavior();
	
	public IModel<?> getDefaultModel();
	
	public C getThisComponent();
	
	public default IVueDescriptor getVueDescriptor() {
		return getVueBehavior().getVueDescriptor();
	}
	
	public default C setVueDescriptor(IVueDescriptor vueDescriptor) {
		getVueBehavior().setVueDescriptor(vueDescriptor);
		return getThisComponent();
	}
	
	public default C setVueDescriptor(String name, String vueDescriptor) {
		return setVueDescriptor(new VueJsonDescriptor(name, vueDescriptor));
	}
	
	public default C setVueDescriptor(String vueDescriptor) {
		return setVueDescriptor(new VueJsonDescriptor(vueDescriptor));
	}
	
	public default C setVueDescriptor(ResourceReference reference) {
		return setVueDescriptor(new VueFileDescriptor(reference));
	}
	
	
	public default Map<String, IVuecketMethod<?>> getVueMethods() {
		return getVueBehavior().getVueMethods();
	}
	
	public default C addVueMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueMethod(name, vueMethod);
		return getThisComponent();
	}
	
	public default Map<String, IVuecketMethod<?>> getVueOnMethods() {
		return getVueBehavior().getVueOnMethods();
	}
	
	public default C addVueOnMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueOnMethod(name, vueMethod);
		return getThisComponent();
	}
	
	public default Map<String, IVuecketMethod<?>> getVueOnceMethods() {
		return getVueBehavior().getVueOnceMethods();
	}
	
	public default C addVueOnceMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueOnceMethod(name, vueMethod);
		return getThisComponent();
	}
	
	public default Map<String, IVuecketMethod<?>> getVueWatchMethods() {
		return getVueBehavior().getVueWatchMethods();
	}
	
	public default C addVueWatchMethod(String name, IVuecketMethod<?> vueMethod) {
		getVueBehavior().addVueWatchMethod(name, vueMethod);
		return getThisComponent();
	}
	
	public default <M> C addDataFiber(String name) {
		getVueBehavior().addDataFiber(name, getDefaultModel());
		return getThisComponent();
	}
	
	public default <M> C addDataFiber(String name, boolean load, boolean observe) {
		getVueBehavior().addDataFiber(name, getDefaultModel(), load, observe);
		return getThisComponent();
	}
	
	public default <M> C addDataFiber(String name, IModel<M> model) {
		getVueBehavior().addDataFiber(name, model);
		return getThisComponent();
	}
	
	public default <M> C addDataFiber(String name, IModel<M> model, boolean load, boolean observe) {
		getVueBehavior().addDataFiber(name, model, load, observe);
		return getThisComponent();
	}
	
	public default <M> C removeDataFiber(String name) {
		getVueBehavior().removeDataFiber(name);
		return getThisComponent();
	}
	
	public default <M> C removeDataFiber(String name, boolean load, boolean observe) {
		getVueBehavior().removeDataFiber(name, load, observe);
		return getThisComponent();
	}
}
