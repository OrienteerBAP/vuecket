package org.orienteer.vuecket.demo;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueComponentHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new VueComponent<String>("app") {
			@Override
			public VueComponentHeaderItem getVueComponentHeaderItem() {
				return VueComponentHeaderItem.forRootAppDescriptor(getMarkupId(), 
						"{"
						+ "data: { message : 'Hello Vue'}"
						+ "}");
			}
		});

	}
}
