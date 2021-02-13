package org.orienteer.vuecket;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.orienteer.vuecket.descriptor.VueFile;

@VueFile(value = "VueCustomComponent.vue", name="vue-custom-component")
public class VueCustomComponent extends VueComponent<String> {

	public VueCustomComponent(String id, IModel<String> model) {
		super(id, model);
	}


}
