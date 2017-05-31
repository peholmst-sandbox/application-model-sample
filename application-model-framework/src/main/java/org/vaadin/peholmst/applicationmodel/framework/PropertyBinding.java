package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.shared.Registration;

/**
 * TODO document me
 */
public interface PropertyBinding<V, T, P extends Property<V>> extends Registration {

    T getTarget();

    P getProperty();
}
