package org.orienteer.vuecket;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class MainVuecketTest {
	
	private WicketTester tester = new WicketTester(new TestWicketApplication());
	
	@Test
	public void testHomePageRendering() {
		assertTrue(tester.getApplication() instanceof TestWicketApplication);
		tester.startPage(tester.getApplication().getHomePage());
		tester.assertRenderedPage(HomePage.class);
	}
}
