package org.vaadin.peholmst.applicationmodel.sample.domain;

/**
 * Created by petterwork on 01/06/2017.
 */
public abstract class AbstractTicketEvent {

    private final Ticket ticket;

    public AbstractTicketEvent(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
