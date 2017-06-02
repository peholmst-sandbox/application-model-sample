package org.vaadin.peholmst.applicationmodel.sample.domain;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketOpenedEvent extends AbstractTicketEvent {

    public TicketOpenedEvent(Ticket ticket) {
        super(ticket);
    }
}
