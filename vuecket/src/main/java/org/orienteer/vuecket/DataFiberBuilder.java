package org.orienteer.vuecket;

import org.apache.wicket.model.IModel;

/**
 * Builder to simplify association of IModels with different fibers: property, load, observe, refresh
 * @param <M> type of object in a model
 * @param <V> type of IVueBehaviorLocator - needed for api simplicity
 */
public class DataFiberBuilder<M, V extends IVueBehaviorLocator> {
	
	private V locator;
	private final String name;
	private final IModel<M> model;
	
	private boolean property;
	private boolean init;
	private boolean update;
	private boolean observe;
	
	DataFiberBuilder(V locator, IModel<M> model, String name) {
		this.locator = locator;
		this.model = model;
		this.name = name;
	}
	
	public DataFiberBuilder<M, V> bindToProperty() {
		this.property = true;
		return this;
	}
	
	public DataFiberBuilder<M, V> bindToData() {
		this.property = false;
		return this;
	}
	
	public DataFiberBuilder<M, V> init() {
		this.init = true;
		return this;
	}
	
	public DataFiberBuilder<M, V> observe() {
		this.observe = true;
		return this;
	}
	
	public DataFiberBuilder<M, V> update() {
		this.update = true;
		return this;
	}
	
	
	public V build() {
		VueBehavior vueBehavior = locator.getVueBehavior();
		vueBehavior.addDataFiber(name, model, property, init, update, observe);
		return locator;
	}
}
