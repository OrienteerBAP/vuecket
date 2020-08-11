package org.orienteer.vuecket.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.util.parse.metapattern.OptionalMetaPattern;
import org.apache.wicket.util.parse.metapattern.parsers.VariableAssignmentParser;

public class FixWICKET6815 {
	private FixWICKET6815() {
		
	}
	
	private static void setFinalField(Object object, Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		
		field.set(object, newValue);
	}
	
	private static final String _FIXED_XML_ATTRIBUTE_NAME = "[A-Za-z_@:][A-Za-z0-9_.-]*";
	private static final MetaPattern FIXED_XML_ATTRIBUTE_NAME = new MetaPattern(_FIXED_XML_ATTRIBUTE_NAME);
	
	private static void hackParser() throws Exception {
		Field namespaceField = VariableAssignmentParser.class.getDeclaredField("namespace");
		setFinalField(null, namespaceField, new OptionalMetaPattern(new MetaPattern[] {
				FIXED_XML_ATTRIBUTE_NAME, MetaPattern.COLON, new OptionalMetaPattern(new MetaPattern[] {FIXED_XML_ATTRIBUTE_NAME, MetaPattern.COLON })}));
		
		Field xmlField = MetaPattern.class.getDeclaredField("XML_ATTRIBUTE_NAME"); 
		setFinalField(null, xmlField, FIXED_XML_ATTRIBUTE_NAME);
	}
	
	public static void fix() {
		try {
			hackParser();
		} catch (Exception e) {
			throw new WicketRuntimeException("Can't fix WICKET-6815", e);
		}
	}
}
