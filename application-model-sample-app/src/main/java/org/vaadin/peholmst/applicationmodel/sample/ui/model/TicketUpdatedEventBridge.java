package org.vaadin.peholmst.applicationmodel.sample.ui.model;

import org.vaadin.peholmst.applicationmodel.framework.eventbus.EventBus;
import org.vaadin.peholmst.applicationmodel.sample.domain.AbstractTicketEvent;
import org.vaadin.peholmst.applicationmodel.sample.domain.base.DomainEvents;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketUpdatedEventBridge {

    private final EventBus eventBus;

    public TicketUpdatedEventBridge(EventBus eventBus) {
        this.eventBus = eventBus;
        DomainEvents.getInstance().subscribe(this::onAbstractTicketEvent, AbstractTicketEvent.class);
    }

    private void onAbstractTicketEvent(AbstractTicketEvent event) {
        eventBus.publish(new TicketUpdatedEvent(event));
    }
}
