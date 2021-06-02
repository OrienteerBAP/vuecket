package org.orienteer.vuecket.demo;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.extensions.VueMarkdown;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

/**
 * HomePage for our application
 */
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new ChatApp("app")
				.add(new VueMarkdown("markdown")));

	}
}
