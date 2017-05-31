package org.vaadin.peholmst.applicationmodel.framework.eventbus;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

import com.vaadin.shared.Registration;

/**
 * TODO Document me
 */
public class EventBus {

    private final Collection<ContextualEventBusListener> eventBusListeners = new ConcurrentSkipListSet<>();
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
        eventBusListeners.forEach(listener -> eventProcessor.submit(() -> listener.onEvent(event)));
    }

    /**
     *
     * @param eventBusListener
     * @return
     */
    public Registration subscribe(EventBusListener eventBusListener) {
        ContextualEventBusListener listener = new ContextualEventBusListener(eventBusListener);
        eventBusListeners.add(listener);
        return (Registration) () -> eventBusListeners.remove(listener);
    }
}
