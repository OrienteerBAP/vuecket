package org.orienteer.vuecket.demo;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data container / wrapper for chat messages
 */
public class Message implements IClusterable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("message")
	private String text;
	private Date date;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
	
	@Override
	public String toString() {
		return date+": "+text;
	}
}
