package org.orienteer.vuecket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.util.parse.metapattern.OptionalMetaPattern;
import org.apache.wicket.util.parse.metapattern.parsers.VariableAssignmentParser;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class WicketParserTest {
	
	private WicketTester tester = new WicketTester();
	
	@Test
	public void testParsing() throws Exception {
		String html = "<label wicket:id=\"id\" v-bind:test=\"test\">test</label>";
		MarkupParser parser = new MarkupParser(html);
		Markup markup = parser.parse();
		assertEquals(html, markup.toString(true));
	}
	
	@Test
	public void testTagAttributeParsing() throws Exception {
		String tag = "label wicket:id=\"myid\" bind:test=\"test\" v-bind:test2=\"test2\" @test3=\"test3\" :test4=\"test4\"";
		VariableAssignmentParser attributeParser = new VariableAssignmentParser(tag);
		int pos = 5;
		assertTrue(attributeParser.matcher().find(pos));
		String key = attributeParser.getKey();
		String value = attributeParser.getValue();
		pos = attributeParser.matcher().end(0);
		assertEquals("wicket:id", key);
		assertEquals("\"myid\"", value);
		
		assertTrue(attributeParser.matcher().find(pos));
		key = attributeParser.getKey();
		value = attributeParser.getValue();
		pos = attributeParser.matcher().end(0);
		assertEquals("bind:test", key);
		assertEquals("\"test\"", value);
		
		assertTrue(attributeParser.matcher().find(pos));
		key = attributeParser.getKey();
		value = attributeParser.getValue();
		pos = attributeParser.matcher().end(0);
		assertEquals("v-bind:test2", key);
		assertEquals("\"test2\"", value);
		
		assertTrue(attributeParser.matcher().find(pos));
		key = attributeParser.getKey();
		value = attributeParser.getValue();
		pos = attributeParser.matcher().end(0);
		assertEquals("@test3", key);
		assertEquals("\"test3\"", value);
		
		assertTrue(attributeParser.matcher().find(pos));
		key = attributeParser.getKey();
		value = attributeParser.getValue();
		pos = attributeParser.matcher().end(0);
		assertEquals(":test4", key);
		assertEquals("\"test4\"", value);
	}
}
