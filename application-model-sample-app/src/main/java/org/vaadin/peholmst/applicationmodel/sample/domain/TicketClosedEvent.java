package org.vaadin.peholmst.applicationmodel.sample.domain;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketClosedEvent extends AbstractTicketEvent {

    public TicketClosedEvent(Ticket ticket) {
        super(ticket);
    }
}
