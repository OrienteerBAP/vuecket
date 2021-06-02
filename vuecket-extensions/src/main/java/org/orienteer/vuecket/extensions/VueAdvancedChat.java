package org.orienteer.vuecket.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IClusterable;
import org.orienteer.vuecket.DataFiber;
import org.orienteer.vuecket.VueComponent;
import org.orienteer.vuecket.VueSettings;
import org.orienteer.vuecket.descriptor.VueNpm;
import org.orienteer.vuecket.method.IVuecketMethod;
import org.orienteer.vuecket.method.VueOn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Vuecket component to integrate with vue-advanced-chat
 * https://github.com/antoine92190/vue-advanced-chat
 */
@VueNpm(packageName = "vue-advanced-chat", path = "dist/vue-advanced-chat.umd.js", 
		enablement = "Vue.component('ChatWindow', window['vue-advanced-chat'].default)")
@Slf4j
public abstract class VueAdvancedChat extends VueComponent<Void> {

	/**
	 * Data class to provide information about Room
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Room implements IClusterable {
		private Object roomId;
		private String roomName;
		private List<ChatUser> users;
		private List<ChatMessage> messages = new ArrayList<ChatMessage>();
		private ChatMessage lastMessage;
		private String avatar;
		
		public Room addMessage(ChatMessage message) {
			messages.add(message);
			return this;
		}
	}
	
	/**
	 * Data class to provide information about available Actions
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Action implements IClusterable {
		private String name;
		private String title;
	}
	
	/**
	 * Data class to provide information about Message which has been sent
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SendMessage implements IClusterable {
		private Object roomId;
		private String content;
	}
	
	/**
	 * Data class to provide information about a User in chat room
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChatUser implements IClusterable {
		@JsonProperty("_id")
		private String id;
		private String username;
		private ChatUserStatus status = new ChatUserStatus();
	}
	
	/**
	 * Data class to provide information about status of a User in chat room
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChatUserStatus implements IClusterable {
		private String state = "online";
		private Date lastChanged;
	}
	
	/**
	 * Data class to provide information about a message in a chat room
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChatMessage implements IClusterable {
		@Getter(onMethod=@__({@JsonProperty("_id")}))
		private Object id;
		private String content;
		private Object senderId;
		private String username;
		private boolean system=false;
		private String date;
		private String timestamp;
		private Boolean saved;
		private Boolean distributed;
		private Boolean seen;
	}
	
	/**
	 * Data class to reflect event to fetch messages from a room
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FetchMessages implements IClusterable {
		private Room room;
		private Object options;
	}
	
	/**
	 * Data class to reflect event out of action selected by user to be executed
	 */
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ActionHandlerEvent implements IClusterable {
		private Object roomId;
		private Action action;
	}
	
	protected IModel<Boolean> roomsLoaded = Model.of(true);
	protected IModel<Boolean> messagesLoaded = Model.of(true);
	protected IModel<List<Action>> roomActions = Model.ofList(new ArrayList<Action>());
	protected IModel<List<Action>> menuActions = Model.ofList(new ArrayList<Action>());
	
	public VueAdvancedChat(String id, IModel<List<Room>> rooms, IModel<List<ChatMessage>> messages) {
			
		super(id);
		dataFiberBuilder(rooms, "rooms").bindToProperty(new ArrayList<VueAdvancedChat.Room>()).init().update().bind();
		dataFiberBuilder(messages, "messages")
				.bindToProperty(getDefaultMessages()).init().update().bind();
		dataFiberBuilder(messagesLoaded, "messages-loaded").bindToProperty(false).bind();
		dataFiberBuilder(roomsLoaded, "rooms-loaded").bindToProperty(false).init().bind();
		dataFiberBuilder(roomActions, "room-actions").bindToProperty().init().bind();
		dataFiberBuilder(roomActions, "menu-actions").bindToProperty().init().bind();
	}
	
	private static IModel<List<ChatMessage>> getDefaultMessages() {
		return Model.ofList(Arrays.asList(new ChatMessage()
				.setId("-1")
				.setSystem(true)
				.setContent("Chat")
				.setSenderId("System")
				));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(
							VueSettings.get().getNpmPackageProvider()
								.provide("vue-advanced-chat", "dist/vue-advanced-chat.css")));
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.setName("chat-window");
		tag.put("current-user-id", getCurrentUserId());
//		tag.put("room-id", DAO.asDocument(getModelObject()).getIdentity().toString());
		tag.put(":show-add-room", false);
		tag.put(":show-audio", false);
		List<Room> rooms = ((DataFiber<List<Room>>)getDataFibers().getDataFiberByName("rooms").get()).getValueInternal();
		tag.put(":single-room", rooms==null || rooms.size()<=1);
	}
	
	public VueAdvancedChat addRoomAction(String key) {
		roomActions.getObject().add(new Action().setName(key).setTitle(getLocalizer().getString("room.action."+key, this)));
		return this;
	}
	
	public VueAdvancedChat addRoomActions(String... keys) {
		for (String key : keys) {
			addRoomAction(key);
		}
		return this;
	}
	
	public VueAdvancedChat addMenuAction(String key) {
		menuActions.getObject().add(new Action().setName(key).setTitle(getLocalizer().getString("menu.action."+key, this)));
		return this;
	}
	
	public VueAdvancedChat addMenuActions(String... keys) {
		for (String key : keys) {
			addMenuAction(key);
		}
		return this;
	}
	
	public abstract String getCurrentUserId();
	
	@VueOn("send-message")
	public abstract void onSendMessage(IVuecketMethod.Context ctx, SendMessage message);
	
	@VueOn("fetch-messages")
	public void onFetchMessage(IVuecketMethod.Context ctx, FetchMessages fetchMessages) {
		log.info("Fetch: "+ctx);
	}
	
	@VueOn("room-action-handler")
	public void onRoomActionHandler(IVuecketMethod.Context ctx, ActionHandlerEvent event) {
		log.info("onRoomActionHandler: "+ctx);
	}
	
	@VueOn("menu-action-handler")
	public void onMenuActionHandler(IVuecketMethod.Context ctx, ActionHandlerEvent event) {
		log.info("onMenuActionHandler: "+ctx);
	}

}
