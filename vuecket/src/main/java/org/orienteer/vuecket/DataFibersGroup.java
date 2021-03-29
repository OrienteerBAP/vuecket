package org.orienteer.vuecket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Collection of DataFibers organized in performance oriented way
 */
public class DataFibersGroup implements Iterable<DataFiber<?>>, IDetachable {
	
	private static final long serialVersionUID = 1L;

	private final List<DataFiber<?>> dataFibers = new ArrayList<DataFiber<?>>();
	
	private final Map<String, DataFiber<?>> propertyDataFibers = new HashMap<String, DataFiber<?>>();
	private final Map<String, DataFiber<?>> initDataFibers = new HashMap<String, DataFiber<?>>();
	private final Map<String, DataFiber<?>> updateDataFibers = new HashMap<String, DataFiber<?>>();
	private final Map<String, DataFiber<?>> observeDataFibers = new HashMap<String, DataFiber<?>>();
	
	public DataFibersGroup registerDataFiber(DataFiber<?> df) {
		dataFibers.add(df);
		if(DataFiber.DataFiberType.PROPERTY.equals(df.getType())) propertyDataFibers.put(df.getName(), df);
		if(df.shouldInit()) initDataFibers.put(df.getName(), df);
		if(df.shouldUpdate()) updateDataFibers.put(df.getName(), df);
		if(df.shouldObserve()) observeDataFibers.put(df.getName(), df);
		return this;
	}
	
	
	public Map<String, DataFiber<?>> getPropertyDataFibers() {
		return propertyDataFibers;
	}

	public Map<String, DataFiber<?>> getInitDataFibers() {
		return initDataFibers;
	}

	public Map<String, DataFiber<?>> getUpdateDataFibers() {
		return updateDataFibers;
	}

	public Map<String, DataFiber<?>> getObserveDataFibers() {
		return observeDataFibers;
	}

	public int size() {
		return dataFibers.size();
	}

	@Override
	public Iterator<DataFiber<?>> iterator() {
		return dataFibers.iterator();
	}

	@Override
	public void detach() {
		for (DataFiber<?> df : dataFibers) {
			df.detach();
		}
	}

}
