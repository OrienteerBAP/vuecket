package org.orienteer.vuecket;

import org.apache.wicket.model.IModel;
import org.orienteer.vuecket.DataFiber.DataFiberType;

/**
 * Builder to simplify association of IModels with different fibers: property, load, observe, refresh
 * @param <M> type of object in a model
 * @param <V> type of IVueBehaviorLocator - needed for api simplicity
 */
public class DataFiberBuilder<M, V extends IVueBehaviorLocator> {
	
	private V locator;
	private final String name;
	private final IModel<M> model;
	
	private IModel<M> initPropValue;
	private DataFiberType type=DataFiberType.DATA;
	private boolean init;
	private boolean update;
	private boolean observe;
	
	DataFiberBuilder(V locator, IModel<M> model, String name) {
		this.locator = locator;
		this.model = model;
		this.name = name;
	}
	
	public DataFiberBuilder<M, V> bindToProperty() {
		return bindToProperty(null);
	}
	
	public DataFiberBuilder<M, V> bindToProperty(IModel<M> initPropValue) {
		this.type = DataFiberType.PROPERTY;
		this.initPropValue = initPropValue;
		return this;
	}
	
	public DataFiberBuilder<M, V> bindToData() {
		this.type = DataFiberType.DATA;
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
	
	
	public V bind() {
		DataFiber<M> df = new DataFiber<M>(type, name, model, initPropValue, init, update, observe);
		df.bind(locator);
		return locator;
	}
}
