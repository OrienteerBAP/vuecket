package org.orienteer.vuecket.extensions;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueSettings;
import org.orienteer.vuecket.descriptor.VueNpm;
import org.orienteer.vuecket.method.IVuecketMethod.Context;
import org.orienteer.vuecket.npmprovider.INPMPackageProvider;
import org.orienteer.vuecket.method.JsFunction;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import org.orienteer.vuecket.method.VueMethod;
import org.orienteer.vuecket.util.VuecketUtils;

/**
 * Vuecket component for FullCalendar js library
 * https://fullcalendar.io/docs/vue
 */
@VueNpm(packageName = "@fullcalendar/vue", path = "dist/main.global.js", enablement = "")
@Slf4j
public class FullCalendar extends VueComponent<FullCalendar.Options> {
	
	/**
	 * Class for passing infor about events requests
	 */
	@Data
	public static class EventsRequestInfo implements IClusterable {
		private Date start;
		private Date end;
		private String startStr;
		private String endStr;
		private String timeZone;
	}
	
	/**
	 * Container for events data
	 */
	@Data
	@Accessors(chain = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Event implements IClusterable {
		private String id;
		private String groupId;
		private Boolean allDay;
		private Date start;
		private Date end;
		private String title;
		private String url;
		private String backgroundColor;
	}
	
	/**
	 * Options for FullCalendar
	 */
	@Data
	@Accessors(chain = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Options implements IClusterable {
		private Object events;
		private Toolbar headerToolbar = new Toolbar().setCenter("dayGridMonth,timeGridWeek");
		private Toolbar footerToolbar;
		private Float aspectRatio;
		private String initialView;
		
		public Options(Object events) {
			this.events = events;
		}
		
		public Options(FullCalendar calendar) {
			this(JsFunction.call(calendar, "lookupEvents"));
		}
	}
	
	/**
	 * Toolbar definition for FullCalendar
	 */
	@Data
	@Accessors(chain = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Toolbar implements IClusterable {
		private String start;
		private String center;
		private String end;
	}
	
	public FullCalendar(String id) {
		super(id);
		setDefaultModel(Model.of(new Options(this)));
	}
	
	public FullCalendar(String id, IModel<Options> optionsModel) {
		super(id, optionsModel);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		dataFiberBuilder("options").bindToProperty().init().build();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.setName("full-calendar");
		super.onComponentTag(tag);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		INPMPackageProvider provider = VueSettings.get().getNpmPackageProvider();
		response.render(CssHeaderItem.forReference(provider.provide("fullcalendar", "main.css")));
		response.render(JavaScriptHeaderItem.forReference(provider.provide("fullcalendar", "main.min.js"), "fullcalendar"));
		super.renderHead(response);
	}
	
	@VueMethod("lookupEvents")
	public final List<Event> lookupEvents(Context ctx, EventsRequestInfo eventsRequestInfo) {
		return lookupEvents(eventsRequestInfo.getStart(), eventsRequestInfo.getEnd());
	}
	
	public List<Event> lookupEvents(Date start, Date end) {
		return new ArrayList<FullCalendar.Event>();
	}
	
	public void refetchEvents(AjaxRequestTarget target) {
		String script = String.format("Vue.getVueById('%s').getApi().refetchEvents()", getMarkupId());
		target.appendJavaScript(script);
	}
}
