package org.orienteer.vuecket;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
		return bindToProperty((IModel<M>)null);
	}
	
	public DataFiberBuilder<M, V> bindToProperty(M initPropValue) {
		return bindToProperty((IModel<M>)(initPropValue==null?null:Model.of((Serializable)initPropValue)));
	}
	
	public DataFiberBuilder<M, V> bindToProperty(IModel<M> initPropValue) {
		this.type = DataFiberType.PROPERTY;
		this.initPropValue = initPropValue;
		this.init = this.init || initPropValue!=null;
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
	
	@Deprecated
	public V bind() {
		return build();
	}
	
	public V build() {
		DataFiber<M> df = new DataFiber<M>(type, name, model, initPropValue, init, update, observe);
		df.bind(locator);
		return locator;
	}
}
