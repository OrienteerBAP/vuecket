package org.orienteer.vuecket.demo;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.descriptor.VueFile;

@VueFile("ChatApp.vue")
public class ChatApp extends VueComponent<Void> {

	public ChatApp(String id) {
		super(id);
	}

}
