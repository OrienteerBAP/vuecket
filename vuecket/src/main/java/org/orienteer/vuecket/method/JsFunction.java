package org.orienteer.vuecket.method;

import java.io.IOException;

import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.IVueBehaviorLocator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.RawValue;

import lombok.Data;

/**
 * Object to wrap serialization of JS Functions by ObjectMapper
 */
@Data
public class JsFunction implements JsonSerializable, IClusterable {
	
	private static final long serialVersionUID = 1L;
	
	private String definition;

	public JsFunction() {
	}
	
	public JsFunction(String definition) {
		this.definition = definition;
	}

	public static JsFunction invoke(IVueBehaviorLocator locator, String methodName) {
		return new JsFunction(generateDefinition(locator, methodName, true));
	}
	
	public static JsFunction call(IVueBehaviorLocator locator, String methodName) {
		return new JsFunction(generateDefinition(locator, methodName, false));
	}
	
	public static String generateDefinition(IVueBehaviorLocator locator, String methodName, boolean async) {
		String vueId = locator.getVueBehavior().getComponentMarkupId();
		return "function(...args) {"
				+ "var v = (this.Vue?this.Vue:Vue).getVueById('"+vueId+"');"
				+ (async?"v.vcInvoke.apply('"+methodName+"', ...args);"
						: "return v.vcCall('"+methodName+"', ...args);")
				+ "}";
	}

	@Override
	public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeRawValue(String.valueOf(definition));
	}

	@Override
	public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		serialize(gen, serializers);
	}

}
