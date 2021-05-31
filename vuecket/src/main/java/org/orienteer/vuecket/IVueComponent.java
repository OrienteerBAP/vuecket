package org.orienteer.vuecket;

import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueFileDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.method.IVuecketMethod;

/**
 * Interface-helper to bring in bulk set of Vuecket related methods to Wicket components
 * @param <C> - type of a Component class to be attached to. Used for method chaining
 */
public interface IVueComponent<C extends IVueBehaviorLocator> extends IVueBehaviorLocator {
	
	@Override
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
	
	public default <M> DataFiberBuilder<M, C> dataFiberBuilder(String name) {
		return dataFiberBuilder((IModel<M>)getDefaultModel(), name);
	}
	
	public default <M> DataFiberBuilder<M, C> dataFiberBuilder(IModel<M> model, String name) {
		return new DataFiberBuilder<M, C>(getThisComponent(), model, name);
	}
	
	public default int getRefreshPeriod() {
		return getVueBehavior().getRefreshPeriod();
	}
	
	public default C setRefreshPeriod(Integer refreshPeriod) {
		getVueBehavior().setRefreshPeriod(refreshPeriod);
		return getThisComponent();
	}
	
	public default DataFibersGroup getDataFibers() {
		return getVueBehavior().getDataFibers();
	}
}
