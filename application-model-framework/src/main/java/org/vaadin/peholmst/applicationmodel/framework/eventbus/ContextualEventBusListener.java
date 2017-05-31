package org.vaadin.peholmst.applicationmodel.framework.eventbus;

import java.util.Map;
import java.util.Objects;

import com.vaadin.util.CurrentInstance;

/**
 * TODO Document me!
 */
class ContextualEventBusListener implements EventBusListener {

    private final EventBusListener delegateListener;
    private final Map<Class<?>, CurrentInstance> context;

    ContextualEventBusListener(EventBusListener delegateListener) {
        this.delegateListener = Objects.requireNonNull(delegateListener, "delegateListener must not be null");
        this.context = CurrentInstance.getInstances();
    }

    @Override
    public void onEvent(Object event) {
        final Map<Class<?>, CurrentInstance> oldContext = CurrentInstance.getInstances();
        CurrentInstance.restoreInstances(context);
        try {
            delegateListener.onEvent(event);
        } finally {
            CurrentInstance.restoreInstances(oldContext);
        }
    }
}
