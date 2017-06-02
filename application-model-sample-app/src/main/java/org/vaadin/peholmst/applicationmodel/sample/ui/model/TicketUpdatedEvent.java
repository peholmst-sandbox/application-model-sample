package org.vaadin.peholmst.applicationmodel.sample.ui.model;

import org.vaadin.peholmst.applicationmodel.sample.domain.AbstractTicketEvent;
import org.vaadin.peholmst.applicationmodel.sample.domain.Ticket;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketUpdatedEvent {

    private final AbstractTicketEvent domainEvent;

    public TicketUpdatedEvent(AbstractTicketEvent domainEvent) {
        this.domainEvent = domainEvent;
    }

    public AbstractTicketEvent getDomainEvent() {
        return domainEvent;
    }

    public Ticket getTicket() {
        return domainEvent.getTicket();
    }
}
