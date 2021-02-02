package org.orienteer.vuecket.method;

import org.orienteer.vuecket.IVueBehaviorLocator;

/**
 * JsFunction implementation which invoke serverside for providing results.
 * Useful for any kind of JS options which can support functions as arguments
 */
public class ServerCallJsFunction extends JsFunction {
	protected ServerCallJsFunction(IVueBehaviorLocator locator, String methodName, boolean async) {
		super();
		setFunctionDefinition(generateDefinition(locator, methodName, async));
	}
	
	protected String generateDefinition(IVueBehaviorLocator locator, String methodName, boolean async) {
		String vueId = locator.getVueBehavior().getComponentMarkupId();
		return "function(...args) {"
				+ "var v = (this.Vue?this.Vue:Vue).getVueById('"+vueId+"');"
				+ (async?"v.vcInvoke.apply('"+methodName+"', ...args);"
						: "return v.vcCall('"+methodName+"', ...args);")
				+ "}";
	}
	
	public static ServerCallJsFunction invoke(IVueBehaviorLocator locator, String methodName) {
		return new ServerCallJsFunction(locator, methodName, true);
	}
	
	public static ServerCallJsFunction call(IVueBehaviorLocator locator, String methodName) {
		return new ServerCallJsFunction(locator, methodName, false);
	}
}
