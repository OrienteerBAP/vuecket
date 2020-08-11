package org.orienteer.vuecket;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.method.IVuecketMethod;
import org.orienteer.vuecket.method.VueMethod;
import org.orienteer.vuecket.method.VueOn;
import org.orienteer.vuecket.method.VueWatch;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
		
		VueComponent<Object> app4 = new VueComponent<Object>("app4");
		app4.add(new VueMarkdown("markdown", "Hello **there**"));
		add(app4);
		
		add(new VueComponent<Object>("app5") {
			@VueMethod("serverCount")
			public void updateCountModel(IVuecketMethod.Context ctx, int count) {
				IVuecketMethod.pushDataPatch(ctx, "server", "Hello from server #"+count);
			}
			
			@VueOn("increase")
			public void showIncrease(int count) {
				System.out.println("On Increase called. Recieved count = "+count);
			}
			
			@VueWatch("count")
			public void countChanged(Integer newCount, Integer oldCount) {
				System.out.println("Count changed from "+oldCount+" to "+newCount);
			}
		}.setVueDescriptor("{ data: { count : 0, server: 'Hello from client side' }}"));
		
		Form<?> form = new Form<Object>("form");
		form.add(new AjaxButton("button") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("Clicked!");
			}
		});
		add(form);
	}
}
