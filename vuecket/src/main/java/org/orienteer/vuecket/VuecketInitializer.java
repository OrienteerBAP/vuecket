package org.orienteer.vuecket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.orienteer.vuecket.descriptor.IVueDescriptor;
import org.orienteer.vuecket.descriptor.VueJsonDescriptor;
import org.orienteer.vuecket.util.VuecketUtils;

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
		
		//Automatically add VueBehavior if component was correspondingly annotated
		app.getComponentInitializationListeners().add((comp) -> {
			IVueDescriptor vueDescriptor = IVueDescriptor.lookupDescriptor(comp);
			if(vueDescriptor!=null && VuecketUtils.findVueBehavior(comp)==null) {
				comp.add(new VueBehavior(vueDescriptor));
			}
		});
	}

	@Override
	public void destroy(Application app) {
		
	}

}
