package org.orienteer.vuecket;

import java.util.Objects;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.io.IClusterable;

/**
 * DataFiber is object to communicate between client side and server side
 * @param <T> type of value object
 */
public class DataFiber<T> implements IClusterable, IDetachable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Defines how to bind a datafiber: to data or to a property 
	 */
	public static enum DataFiberType {
		DATA, PROPERTY;
	}
	
	private final DataFiberType type;
	private final String name;
	private final IModel<T> model;
	private int revisionHash=-1;
	private final boolean init;
	private final boolean update;
	private final boolean observe;
	
	public DataFiber(DataFiberType type, String name, IModel<T> model, boolean init, boolean update, boolean observe) {
		this.type = type;
		this.name = name;
		this.model = model;
		this.revisionHash = Objects.hashCode(model!=null?model.getObject():null);
		this.init = init;
		this.update = update;
		this.observe = observe;
	}
	
	public DataFiberType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public IModel<T> getModel() {
		return model;
	}
	
	public T getValue() {
		T value = getValueInternal();
		revisionHash = Objects.hashCode(value);
		return value;
	}
	
	public DataFiber<T> setValue(T value) {
		setValueInternal(value);
		revisionHash = Objects.hashCode(value);
		return this;
	}
	
	public T getValueInternal() {
		return model!=null?model.getObject():null;
	}
	
	public DataFiber<T> setValueInternal(T value) {
		if(model!=null) model.setObject(value);
		return this;
	}
	
	public boolean isValueChanged() {
		Object value = getValueInternal();
		return revisionHash!= Objects.hashCode(value);
	}
	
	public Class<?> getValueClass() {
		if(model==null) return null;
		if(model instanceof IObjectClassAwareModel) return ((IObjectClassAwareModel<?>)model).getObjectClass();
		Object value = model.getObject();
		return value!=null?value.getClass():null;
	}
	
	public boolean shouldInit() {
		return init;
	}
	
	public boolean shouldUpdate() {
		return update;
	}
	
	public boolean shouldObserve() {
		return observe;
	}

	@Override
	public void detach() {
		if(model!=null) model.detach();
	}
	
}
