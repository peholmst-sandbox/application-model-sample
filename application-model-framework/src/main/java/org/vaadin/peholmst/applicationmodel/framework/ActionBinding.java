package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.shared.Registration;

/**
 * TODO document me
 */
public interface ActionBinding<T> extends Registration {

    T getTarget();

    Action getAction();

}
