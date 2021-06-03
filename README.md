# vuecket
Power of [Vue.JS](https://vuejs.org/) married with magic of [Apache Wicket](https://wicket.apache.org/)

Vuecket allows to be reactive on frontend and backend without coding of REST services.

## Content

1. [Progress and Plans](#current-progress-and-plans)
1. [Guiding Principles](#guiding-principles)
1. [Enabling Vuecket](#enabling-vuecket)
1. [Association of Wicket and Vue Components](#association-of-wicket-and-vue-components)
1. [Server-side methods](#server-side-methods)
1. [Subscribing to Vue Events](#subscribing-to-vue-events)
1. [Watch data changes](#watch-data-changes)
1. [Data Fibers](#data-fibers)

Other documentation:
* [VueJS + Apache Wicket = Vuecket](https://medium.com/@phantomydn/vuejs-wicket-vuecket-ee7bd5534fee) - Tutorial
* [Знакомство с Vuecket](https://habr.com/ru/company/orienteer/blog/514938/) - Tutorial on russian

## Current Progress and Plans

- [X] Loading of Vue components code
   - [X] From JSON configuration
   - [X] From Vue files
   - [X] From NPM packages
- [X] Support of propogation of Vue events to server side: $on, $once, $watch
- [ ] Support of data channels between server side and client
   - [X] One Time - upon Vue component load
   - [X] Observe - push changes to server side IMobel if they are changed on client one
   - [X] Periodical refresh from server side
   - [ ] WebSocket based refresh from server side
- [X] Support of server based Vue methods

## Guiding Principles

Vuecket idealogy following the following principals:

1. Be declarative, not imperative
   * You can use VueJS 3rd party libraries and get benefits out of Vuecket even without modifying them
   * In a similar way, you can use 3rd party Apache Wicket component and just by adding VueBehavior (Behavior provided by Vuecket) you enable Vuecket benefits
1. Provide 80% of functionality Out Of the Box, but do have good extension points for the remaining 20%

As you can see, btw, both Vue.Js and Apache Wicket fit this Guiding Principles as well. 

## Enabling Vuecket

Add the following dependency into your `pom.xml`:

```xml
<dependency>
   <groupId>org.orienteer.vuecket</groupId>
   <artifactId>vuecket</artifactId>
   <version>${project.version}</version>
</dependency>
```

If you are using `SNAPSHOT` version, please make sure that the following repository is included into your `pom.xml`:

```xml
<repository>
	<id>Sonatype Nexus</id>
	<url>https://oss.sonatype.org/content/repositories/snapshots/</url>
	<releases>
		<enabled>false</enabled>
	</releases>
	<snapshots>
		<enabled>true</enabled>
	</snapshots>
</repository>
```

## Association of Wicket and Vue Components

To start using of Vuecket power you should associate your server-side component(Wicket) and client-side component(Vue.js).
You have 2 ways how to do that: 
* either through Annotations 
* or java code.

Vue.js components can be also defined by:
* JSON description
* VUE file
* NPM package

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

## Server-side methods

Vuecket can work transparantly for Vue code. But you can add more spice by invoking server based methods from your Vue code.

There are 2 ways how you can use Vuecket server methods:

* `vcInvoke` - asynchronous invokation of server method. No reply from server expected. But server side method has possiblity to "push" some changes to the client side, if needed.
* `vcCall` - return Promise which will contain response from server side 

Example from test Vuecket application:

```java
add(new VueComponent<Object>("app5") {
	@VueMethod("count")
	public void updateCountModel(IVuecketMethod.Context ctx, int count) {
		IVuecketMethod.pushDataPatch(ctx, "server", "Hello from server #"+count);
	}
}.setVueDescriptor("{ data: { count : 0, server: 'Hello from client side' }}"));
```

```html
<div>
	<h1>App #5</h1>
	<div wicket:id="app5">
		<button @click="count++; vcInvoke('count', count)">Clicked {{count}} times</button>
		{{ server }}
	</div>
</div>
```

## Subscribing to Vue Events

It might be helpful to subscribe to particular Vue Events on server side. To do that you can use `@VueOn` and `@VueOnce` annotations for methods which needs to be invoked if event occur on client side. Example:

```java
@VueOn("increase")
public void showIncrease(int count) {
	System.out.println("On Increase called. Recieved count = "+count);
}
```

```html
<button @click="$emit('increase', count)">Test Emit</button>
```

## Watch data changes

In the similiar way you can subscribe server side method to changes of data on client side by using `@VueWatch` annotation

```java
@VueWatch("count")
public void countChanged(Integer newCount, Integer oldCount) {
	System.out.println("Count changed from "+oldCount+" to "+newCount);
}
```
```html
<button @click="count++">Clicked {{count}} times</button>
```

## Data Fibers

Data fiber is a way to synchronize data between server side and browser. There are different types of data-fibers

* `load` - data will be provided only for initial Vue component loading
* `observe` - data will be sent back to server upon any change
* `refresh` - data periodically checked for changes and if there are any - they will be uploaded
* `wspush` - data pushed to client server through WebSocket if there are changes (NOT YET SUPPORTED)

Example:

```java
//Value of this model will be initially load to all connected clients and then kept update upon changes
private static final IModel<String> HELLO_MODEL = Model.of("Hello from server");
...
VueComponent<Object> app6 = new VueComponent<Object>("app6", HELLO_MODEL)
				.setVueDescriptor("{ data: { text : 'Hello Vue'}}")
				.addDataFiber("text");  // <===Pay attention to this call. It binds default IModel to 'text' data fiber
app6.add(new VueMarkdown("markdown", ""));
add(app6);
```
```html
<div>
	<h1>App #6</h1>
	<div wicket:id="app6">
		<textarea v-model="text"></textarea>
		<vue-markdown wicket:id="markdown" v-bind:source="text">Application 6</vue-markdown>
	</div>
</div>
```

