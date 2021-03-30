package org.orienteer.vuecket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.orienteer.vuecket.DataFiber.DataFiberType;

/**
 * Collection of DataFibers organized in performance oriented way
 */
public class DataFibersGroup implements Iterable<DataFiber<?>>, IDetachable {
	
	private static final long serialVersionUID = 1L;
	
//	public static final SerializablePredicate<DataFiber<?>> PROPERTY_DATAFIBERS = df -> DataFiberType.PROPERTY.equals(df.getType());
//	public static final SerializablePredicate<DataFiber<?>> INIT_DATAFIBERS = DataFiber::shouldInit;
	public static final SerializablePredicate<DataFiber<?>> INIT_BY_CLIENT_DATAFIBERS = DataFiber::shouldInitByClient;
	public static final SerializablePredicate<DataFiber<?>> INIT_PROPS_DATAFIBERS = DataFiber::shouldInitPropAttribute;
	public static final SerializablePredicate<DataFiber<?>> UPDATE_DATAFIBERS = DataFiber::shouldUpdate;
	public static final SerializablePredicate<DataFiber<?>> OBSERVE_DATAFIBERS = DataFiber::shouldObserve;
	
	private final List<DataFiber<?>> dataFibers = new ArrayList<DataFiber<?>>();
	
	public DataFibersGroup registerDataFiber(DataFiber<?> df) {
		dataFibers.add(df);
		return this;
	}
	
	public Stream<DataFiber<?>> getDataFibersAsStream(Predicate<DataFiber<?>> predicate) {
		Stream<DataFiber<?>> stream = dataFibers.stream();
		return predicate!=null?stream.filter(predicate):stream;
	}
	
	public List<DataFiber<?>> getDataFibers(Predicate<DataFiber<?>> predicate) {
		return getDataFibersAsStream(predicate).collect(Collectors.toList());
	}
	
	public List<String> getDataFibersNames(Predicate<DataFiber<?>> predicate) {
		return getDataFibersAsStream(predicate).map(DataFiber::getName).collect(Collectors.toList());
	}
	
	public Map<String, DataFiber<?>> getDataFibersAsMap(Predicate<DataFiber<?>> predicate) {
		return getDataFibersAsStream(predicate).collect(Collectors.toMap(DataFiber::getName, Function.identity()));
	}
	
	public Optional<DataFiber<?>> getDataFiberByName(String name) {
		return getDataFibersAsStream(p-> Objects.equals(name, p.getName())).findFirst();
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
