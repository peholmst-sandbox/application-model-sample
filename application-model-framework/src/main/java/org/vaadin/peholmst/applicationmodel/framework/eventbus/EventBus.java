package org.vaadin.peholmst.applicationmodel.framework.eventbus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.vaadin.shared.Registration;

/**
 * TODO Document me
 */
public class EventBus {

    private final Collection<ContextualEventBusListener> eventBusListeners = new HashSet<>();
    private final ExecutorService eventProcessor;

    /**
     *
     * @param eventProcessor
     */
    public EventBus(ExecutorService eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    /**
     *
     * @param event
     */
    public void publish(Object event) {
        Set<EventBusListener> listeners;
        synchronized (eventBusListeners) {
            listeners = new HashSet<>(eventBusListeners);
        }
        listeners.forEach(listener -> eventProcessor.submit(() -> listener.onEvent(event)));
    }

    /**
     *
     * @param eventBusListener
     * @return
     */
    public Registration subscribe(EventBusListener eventBusListener) {
        ContextualEventBusListener listener = new ContextualEventBusListener(eventBusListener);
        synchronized (eventBusListeners) {
            eventBusListeners.add(listener);
        }
        return (Registration) () -> {
            synchronized (eventBusListeners) {
                eventBusListeners.remove(listener);
            }
        };
    }
}
