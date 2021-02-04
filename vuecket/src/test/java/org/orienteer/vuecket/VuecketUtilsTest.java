package org.orienteer.vuecket;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.orienteer.vuecket.util.VuecketUtils.*;

import org.apache.wicket.util.tester.WicketTester;

public class VuecketUtilsTest {

	private WicketTester tester = new WicketTester(new TestWicketApplication());
	
	@Test
	public void testToJsonNode() {
		assertTrue(toJsonNode("{ \"test\" : \"test\"}") instanceof ObjectNode);
		assertTrue(toJsonNode("{ test : \"test\"}") instanceof ObjectNode);
		assertTrue(toJsonNode("{ test : 'test'}") instanceof ObjectNode);
		assertTrue(toJsonNode("[{ \"test\" : \"test\"}]") instanceof ArrayNode);
		assertTrue(toJsonNode("just test") instanceof TextNode);
		assertEquals("just test", toJsonNode("just test").asText());
		assertTrue(toJsonNode("\"just test\"") instanceof TextNode);
		assertEquals("just test", toJsonNode("\"just test\"").asText());
		
	}
}
