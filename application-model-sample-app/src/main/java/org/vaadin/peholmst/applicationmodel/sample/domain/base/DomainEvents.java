package org.vaadin.peholmst.applicationmodel.sample.domain.base;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Document me!
 */
public class DomainEvents {

    private static final DomainEvents INSTANCE = new DomainEvents();

    private final Set<ListenerRegistrationImpl> listeners = new HashSet<>();

    /**
     *
     * @param listener
     * @return
     */
    public ListenerRegistration subscribe(Listener<Object> listener) {
        return subscribe(listener, Object.class);
    }

    /**
     *
     * @param listener
     * @param domainEventType
     * @param <T>
     * @return
     */
    public <T> ListenerRegistration subscribe(Listener<T> listener, Class<T> domainEventType) {
        ListenerRegistrationImpl registration = new ListenerRegistrationImpl(listener, domainEventType);
        listeners.add(registration);
        return registration;
    }

    /**
     *
     * @param domainEvent
     */
    public void publish(Object domainEvent) {
        listeners.stream().filter(r -> r.supports(domainEvent)).forEach(r -> r.publish(domainEvent));
    }

    /**
     *
     * @param <T>
     */
    interface Listener<T> {

        /**
         *
         * @param event
         */
        void onDomainEvent(T event);
    }

    /**
     *
     */
    interface ListenerRegistration {

        /**
         *
         */
        void remove();
    }

    @SuppressWarnings("unchecked")
    private class ListenerRegistrationImpl implements ListenerRegistration {

        private final Listener listener;
        private final Class domainEventType;

        ListenerRegistrationImpl(Listener listener, Class domainEventType) {
            this.listener = listener;
            this.domainEventType = domainEventType;
        }

        boolean supports(Object domainEvent) {
            return domainEventType.isInstance(domainEvent);
        }

        void publish(Object domainEvent) {
            listener.onDomainEvent(domainEvent);
        }

        @Override
        public void remove() {
            listeners.remove(this);
        }
    }

    /**
     * 
     * @return
     */
    public static DomainEvents getInstance() {
        return INSTANCE;
    }
}
