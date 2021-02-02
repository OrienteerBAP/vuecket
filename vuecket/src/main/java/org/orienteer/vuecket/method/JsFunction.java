package org.orienteer.vuecket.method;

import org.apache.wicket.util.io.IClusterable;

import com.fasterxml.jackson.databind.util.RawValue;

/**
 * Object to wrap serialization of JS Functions by ObjectMapper
 */
public class JsFunction extends RawValue implements IClusterable {

	public JsFunction() {
		super((String)null);
	}
	
	public JsFunction(String v) {
		super(v);
	}
	
	public String getFunctionDefinition() {
		return (String) rawValue();
	}
	
	public JsFunction setFunctionDefinition(String definition) {
		_value = definition;
		return this;
	}

}
