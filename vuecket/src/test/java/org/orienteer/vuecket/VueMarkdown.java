package org.orienteer.vuecket;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.orienteer.vuecket.descriptor.VueNpm;

@VueNpm(packageName = "vue-markdown", path = "dist/vue-markdown.js", enablement = "Vue.use(VueMarkdown)")
public class VueMarkdown extends Label {

	public VueMarkdown(String id, IModel<?> model) {
		super(id, model);
	}

	public VueMarkdown(String id, Serializable label) {
		super(id, label);
	}

	public VueMarkdown(String id) {
		super(id);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.setName("vue-markdown");
		super.onComponentTag(tag);
	}
}
