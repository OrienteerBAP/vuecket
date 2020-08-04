package org.orienteer.vuecket.util;

import java.util.function.Supplier;

import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.VueComponentHeaderItem;

@FunctionalInterface
public interface IVueComponentHeaderItemSupplier extends IClusterable{

		public VueComponentHeaderItem getVueComponentHeaderItem();
}
