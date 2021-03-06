package org.orienteer.vuecket.demo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see org.orienteer.vuecket.demo.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		/*WebjarsSettings settings = new WebjarsSettings();

        WicketWebjars.install(this, settings);
        VueSettings.get().setNpmPackageProvider(WebJarsNPMPackageProvider.WEBJARS_PROVIDER);*/

		// add your configuration here
	}
}
