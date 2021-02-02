package org.orienteer.vuecket;

/**
 * Interface to unify approach for classes which are aware about particular {@link VueBehavior}
 */

public interface IVueBehaviorLocator {
	public VueBehavior getVueBehavior();
}
