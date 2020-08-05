package org.orienteer.vuecket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;

public class VuecketInitializer implements IInitializer{

	@Override
	public void init(Application app) {
		IPackageResourceGuard packageResourceGuard = app.getResourceSettings() 
                .getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard)
		{
			SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
			guard.addPattern("+*.vue");
		}
	}

	@Override
	public void destroy(Application app) {
		
	}

}
