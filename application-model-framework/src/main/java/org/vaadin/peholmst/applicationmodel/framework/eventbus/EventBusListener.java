package org.vaadin.peholmst.applicationmodel.framework.eventbus;

import com.vaadin.event.SerializableEventListener;

/**
 * TODO Document me!
 */
@FunctionalInterface
public interface EventBusListener extends SerializableEventListener {

    /**
     * 
     * @param event
     */
    void onEvent(Object event);
}
