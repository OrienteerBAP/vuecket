# vuecket
Power of Vue.JS married with magic of Apache Wicket

Vuecket is in early stage of development. Please refer to the following document for more details: https://docs.google.com/document/d/1Lj4LqebeZFkYGsfyC2K7upUl8VpBeAE49Q-BLtvhpFM/edit?usp=sharing

## Current Progress and Plans

- [X] Loading of Vue components code
   - [X] From JSON configuration
   - [X] From Vue files
   - [X] From NPM packages
- [ ] Support of propogation of Vue events to server side
- [ ] Support of data channels between server side and client
   - [ ] One Time - upon Vue component load
   - [ ] Periodical refresh from server side
   - [ ] WebSocket based refresh from server side
- [ ] Support of server based Vue methods

# Enabling Vuecket

Add the following dependency into your `pom.xml`:

```xml
<dependency>
   <groupId>org.orienteer.vuecket</groupId>
   <artifactId>vuecket</artifactId>
   <version>${project.version}</version>
</dependency>
```

You have different ways how to associate Wicket HTML markup components with Vue Components


### Annotations

The following code will allow you to make from common Wicket Label component which supports Markdown
```java
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
```

Check the following annotations: `@VueJson`, `@VueFile` and `@VueNpm`

### Directly on Wicket Componet
```java
add(new VueComponent<String>("app")
      .setVueDescriptor("{ data: { message : 'Hello Vue'}}")
);

add(new VueComponent<String>("app2")
      .setVueDescriptor(new PackageResourceReference(HomePage.class,"HomePage.app2.vue"))
);

add(new Label("app3").add(new VueBehavior(new VueJsonDescriptor("{ data: { message : 'Hello Vue'}}"))));
```
