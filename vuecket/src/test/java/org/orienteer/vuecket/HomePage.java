package org.orienteer.vuecket;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.orienteer.vuecket.VueComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new VueComponent<String>("app")
				.setVueDescriptor("{ data: { message : 'Hello Vue'}}")
		);
		
		add(new VueComponent<String>("app2")
				.setVueDescriptor(new PackageResourceReference(HomePage.class,"HomePage.app2.vue"))
		);
		add(new VueApp3("app3"));
	}
}
