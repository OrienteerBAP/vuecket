package org.orienteer.vuecket.method;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.VueBehavior;
import org.orienteer.vuecket.VueSettings;
import org.orienteer.vuecket.util.VuecketUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@FunctionalInterface
public interface IVuecketMethod<R> extends IClusterable {
	
	public static class Context {
		private VueBehavior vueBehavior;
		private Component component;
		private AjaxRequestTarget target;
		private String mailBoxId;
		
		private Context(VueBehavior vueBehavior, Component component, AjaxRequestTarget target, String mailBoxId) {
			super();
			this.vueBehavior = vueBehavior;
			this.component = component;
			this.target = target;
			this.mailBoxId = mailBoxId;
		}

		public VueBehavior getVueBehavior() {
			return vueBehavior;
		}
		
		public Component getComponent() {
			return component;
		}
		
		public AjaxRequestTarget getTarget() {
			return target;
		}
		
		public String getMailBoxId() {
			return mailBoxId;
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
		pushDataPatch(ctx, VuecketUtils.toMap(patchDescription));
	}
	
	public static void pushDataPatch(Context ctx, Map<String, ?> patch)  {
		if(patch==null || patch.isEmpty()) return;
		try {
			String json = VueSettings.get().getObjectMapper().writeValueAsString(patch);
			String script = String.format("Vue.getVueById('%s').vcApply(%s)", ctx.getComponent().getMarkupId(), json);
			ctx.getTarget().appendJavaScript(script);
		} catch (JsonProcessingException e) {
			throw new WicketRuntimeException(e);
		}
	}
}
