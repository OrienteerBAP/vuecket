package org.orienteer.vuecket.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.descriptor.VueFile;

import com.fasterxml.jackson.databind.JsonNode;

@VueFile("ChatApp.vue")
public class ChatApp extends VueComponent<Void> {
	
	private static final IModel<List<JsonNode>> MESSAGES = Model.ofList(new ArrayList<JsonNode>());

	public ChatApp(String id) {
		super(id);
		addDataFiber("messages", MESSAGES, true, true, true);
	}

}
