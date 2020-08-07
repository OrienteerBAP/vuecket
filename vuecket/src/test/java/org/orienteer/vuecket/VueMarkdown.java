package org.orienteer.vuecket;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.orienteer.vuecket.descriptor.VueNpm;

@VueNpm(packageName = "vue-markdown", path = "dist/vue-markdown.js", enablement = "Vue.use(VueMarkdown)")
public class VueMarkdown extends VueComponent<String> {

	public VueMarkdown(String id, String string) {
		super(id, Model.of(string));
	}
	
	public VueMarkdown(String id, IModel<String> model) {
		super(id, model);
	}
	
	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString());
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);

		if (tag.isOpenClose())
		{
			tag.setType(TagType.OPEN);
		}
	}

}
