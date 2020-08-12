package org.orienteer.vuecket.demo;

import java.io.Serializable;

import org.apache.wicket.markup.html.basic.Label;
import org.orienteer.vuecket.descriptor.VueNpm;

@VueNpm(packageName = "vue-markdown", path = "dist/vue-markdown.js", enablement = "Vue.use(VueMarkdown)")
public class VueMarkdown extends Label {

	public VueMarkdown(String id, Serializable label) {
		super(id, label);
	}
	
}
