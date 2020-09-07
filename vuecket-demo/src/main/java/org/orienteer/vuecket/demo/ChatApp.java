package org.orienteer.vuecket.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.descriptor.VueFile;
import org.orienteer.vuecket.method.IVuecketMethod;
import org.orienteer.vuecket.method.VueMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.orienteer.vuecket.method.IVuecketMethod.Context;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Main {@link VueComponent} for chat application
 */
@VueFile("ChatApp.vue")
public class ChatApp extends VueComponent<Void> {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatApp.class);
	private static final int MAX_SIZE = 20;
	private static final IModel<List<Message>> MESSAGES = Model.ofList(new ArrayList<Message>());

	public ChatApp(String id) {
		super(id);
		addDataFiber("messages", MESSAGES, true, false, false);
	}
	
	@VueMethod
	public synchronized void addMessage(Context ctx, Message message) {
		List<Message> list = MESSAGES.getObject();
		list.add(message);
		trimToSize(list, MAX_SIZE);
		IVuecketMethod.pushDataPatch(ctx, "messages", list);
	}
	
	private void trimToSize(List<Message> list, int size) {
		//It's OK to delete one by one because in most of cases we will delete just one record
		while(list.size()>size) LOG.info("Bay-bay message: {}", list.remove(0));
	}

}
