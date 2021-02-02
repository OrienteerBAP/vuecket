package org.orienteer.vuecket;

import org.apache.wicket.model.IModel;

/**
 * Builder to simplify association of IModels with different fibers: property, load, observe, refresh
 * @param <M> type of object in a model
 * @param <V> type of IVueBehaviorLocator - needed for api simplicity
 */
public class DataFiberBuilder<M, V extends IVueBehaviorLocator> {
	
	private V locator;
	private final String defaultName;
	private final IModel<M> model;
	
	private String propertyName;
	private String loadDataName;
	private String observeDataName;
	private String refreshDataName;
	
	DataFiberBuilder(V locator, IModel<M> model, String defaultName) {
		this.locator = locator;
		this.model = model;
		this.defaultName = defaultName;
	}
	
	public DataFiberBuilder<M, V> property() {
		return property(defaultName);
	}
	
	public DataFiberBuilder<M, V> property(String name) {
		this.propertyName = name;
		return this;
	}
	
	public DataFiberBuilder<M, V> load() {
		return load(defaultName);
	}
	
	public DataFiberBuilder<M, V> load(String name) {
		this.loadDataName = name;
		return this;
	}
	
	public DataFiberBuilder<M, V> observe() {
		return observe(defaultName);
	}
	
	public DataFiberBuilder<M, V> observe(String name) {
		this.observeDataName = name;
		return this;
	}
	
	public DataFiberBuilder<M, V> refresh() {
		return refresh(defaultName);
	}
	
	public DataFiberBuilder<M, V> refresh(String name) {
		this.refreshDataName = name;
		return this;
	}
	
	public V build() {
		VueBehavior vueBehavior = locator.getVueBehavior();
		if(propertyName!=null)    vueBehavior.addDataFiber(propertyName,    model, true,  false, false, false);
		if(loadDataName!=null)    vueBehavior.addDataFiber(loadDataName,    model, false, true,  false, false);
		if(observeDataName!=null) vueBehavior.addDataFiber(observeDataName, model, false, false, true,  false);
		if(refreshDataName!=null) vueBehavior.addDataFiber(refreshDataName, model, false, false, false, true );
		return locator;
	}
}
