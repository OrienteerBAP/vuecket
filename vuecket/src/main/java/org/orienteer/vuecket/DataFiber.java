package org.orienteer.vuecket;

import java.util.Map;
import java.util.Objects;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IClusterable;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	private final IModel<T> initPropModel;
	private int revisionHash=-1;
	private final boolean init;
	private final boolean update;
	private final boolean observe;
	
	public DataFiber(DataFiberType type, String name, IModel<T> model, boolean init , boolean update, boolean observe) {
		this(type, name, model, null, init, update, observe);
	}
	
	public DataFiber(String name, IModel<T> model, IModel<T> initPropModel, boolean update, boolean observe) {
		this(DataFiberType.PROPERTY, name, model, initPropModel, initPropModel!=null, update, observe);
	}
	
	DataFiber(DataFiberType type, String name, IModel<T> model, IModel<T> initPropModel, boolean init, boolean update, boolean observe) {
		this.type = type;
		this.name = name;
		this.model = model;
		this.initPropModel = initPropModel;
		this.revisionHash = -1;
		this.init = init || update; //If fiber updatable: it should be also initialized
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
	
	public IModel<T> getInitPropModel() {
		return initPropModel;
	}
	
	public T getValue() {
		T value = getValueInternal();
		revisionHash = Objects.hashCode(value);
		return value;
	}
	
	public String getValueAsJson() throws JsonProcessingException {
		return VueSettings.get().getObjectMapper().writeValueAsString(getValue());
	}
	
	public String getInitialValueAsJson() throws JsonProcessingException {
		IModel<T> initModel = getInitPropModel();
		if(initModel!=null) {
			return VueSettings.get().getObjectMapper().writeValueAsString(initModel.getObject());
		} else return getValueAsJson();
	}
	
	public String getValueAsVueProperty(String componentId) throws JsonProcessingException {
		String attrValue = getInitialValueAsJson();
		if(shouldUpdate() || getInitPropModel()!=null) 
			attrValue = String.format("$vcDataFiber('%s', '%s', %s)", 
											componentId,
											getName(), 
											attrValue);
		return attrValue;
	}
	
	public DataFiber<T> updatePatch(Map<String, Object> patch) {
		patch.put(getName(), getValue());
		return this;
	}
	
	public DataFiber<T> updatePatch(Map<String, Object> dataPatch, Map<String, Object> propsPatch) {
		updatePatch(DataFiberType.DATA.equals(getType())?dataPatch:propsPatch);
		return this;
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
	
	public boolean shouldInitByClient() {
		return shouldInit()
				&& (DataFiberType.DATA.equals(getType()) 
					|| (getInitPropModel()!=null && !Objects.equals(getModel(), getInitPropModel())));
	}
	
	public boolean shouldInitPropAttribute() {
		return DataFiberType.PROPERTY.equals(getType()) && shouldInit();
	}
	
	public boolean shouldUpdate() {
		return update;
	}
	
	public boolean shouldObserve() {
		return observe;
	}
	
	public DataFiber<T> bind(IVueBehaviorLocator locator) {
		locator.getVueBehavior().getDataFibers().registerDataFiber(this);
		return this;
	}

	@Override
	public void detach() {
		if(model!=null) model.detach();
		if(initPropModel!=null) initPropModel.detach();
	}
	
}
