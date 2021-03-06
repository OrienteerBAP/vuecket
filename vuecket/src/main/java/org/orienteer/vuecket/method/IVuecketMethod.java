package org.orienteer.vuecket.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.DataFiber;
import org.orienteer.vuecket.DataFibersGroup;
import org.orienteer.vuecket.VueBehavior;
import org.orienteer.vuecket.VueSettings;
import org.orienteer.vuecket.util.VuecketUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Describes methods which will be exposed to browser for invocation either directly `vcInvoke`/`vcCall`
 * or per events or upon data modification
 * @param <R> type of the returned object
 */
@FunctionalInterface
public interface IVuecketMethod<R> extends IClusterable {
	
	/**
	 * Context for {@link IVuecketMethod} invocations. Contains multiple useful parameters.
	 */
	@Value
	public static class Context {
		private VueBehavior vueBehavior;
		private Component component;
		private AjaxRequestTarget target;
		private String mailBoxId;
		private @NonFinal @Setter ArrayNode rawArgs;
		
		private Context(VueBehavior vueBehavior, Component component, AjaxRequestTarget target, String mailBoxId) {
			super();
			this.vueBehavior = vueBehavior;
			this.component = component;
			this.target = target;
			this.mailBoxId = mailBoxId;
		}
	}
	
	public R invoke(Context ctx, ArrayNode args) throws Exception;
	public default R call(Context ctx, ArrayNode args) throws Exception{
		R ret = invoke(ctx, args);
		String json = ret!=null
							?VueSettings.get().getObjectMapper().writeValueAsString(ret)
							:"null";
		String script = String.format("Vue.getVueById('%s').$vcMailbox['%s']=%s",
														ctx.getComponent().getMarkupId(),
														ctx.getMailBoxId(),
														json);
		ctx.getTarget().appendJavaScript(script);
		return ret;
	}
	
	public static Context contextFor(VueBehavior vueBehavior, Component component, AjaxRequestTarget target, String mailBoxId) {
		return new Context(vueBehavior, component, target, mailBoxId);
	}
	
	public static void pushDataPatch(Context ctx, Object... patchDescription)  {
		pushPatch(ctx, VuecketUtils.toMap(patchDescription), null);
	}
	
	public static void pushPatch(Context ctx, Map<String, ?> dataPatch, Map<String, ?> propsPatch)  {
		if((dataPatch==null || dataPatch.isEmpty())
			 && (propsPatch==null || propsPatch.isEmpty()))	return;
		try {
			String dataJson = VueSettings.get().getObjectMapper().writeValueAsString(dataPatch);
			String propsJson = VueSettings.get().getObjectMapper().writeValueAsString(propsPatch);
			String script = String.format("Vue.getVueById('%s').vcApply(%s, %s)", ctx.getComponent().getMarkupId(), dataJson, propsJson);
			ctx.getTarget().appendJavaScript(script);
		} catch (JsonProcessingException e) {
			throw new WicketRuntimeException(e);
		}
	}
	
	public static void pushDataFibers(Context ctx, boolean ifChanged, String... dataFiberNames) {
		if(dataFiberNames==null || dataFiberNames.length==0) return;
		List<DataFiber<?>> dataFibers = new ArrayList<DataFiber<?>>();
		DataFibersGroup dataFiberGroup = ctx.getVueBehavior().getDataFibers();
		for (String name : dataFiberNames) {
			dataFiberGroup.getDataFiberByName(name).ifPresent(dataFibers::add);
		}
		pushDataFibers(ctx, ifChanged, dataFibers);
	}
	
	public static void pushDataFibers(Context ctx, boolean ifChanged, DataFiber<?>... dataFibers) {
		if(dataFibers==null || dataFibers.length==0) return;
		pushDataFibers(ctx, ifChanged, Arrays.asList(dataFibers));
	}
	
	public static void pushDataFibers(Context ctx, boolean ifChanged, Iterable<DataFiber<?>> dataFibers) {
		if(dataFibers==null) return;
		Map<String, Object> dataPatch = new HashMap<String, Object>();
		Map<String, Object> propsPatch = new HashMap<String, Object>();
		for (DataFiber<?> df : dataFibers) {
			if(!ifChanged || df.isValueChanged()) {
				df.updatePatch(dataPatch, propsPatch);
			}
		}
		IVuecketMethod.pushPatch(ctx, dataPatch, propsPatch);
	}
}
