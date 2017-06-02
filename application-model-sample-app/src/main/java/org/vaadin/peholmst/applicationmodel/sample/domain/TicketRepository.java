package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.peholmst.applicationmodel.sample.domain.base.Repository;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketRepository extends Repository<Ticket> {

    public TicketRepository() {
        super(Ticket.class);
    }

    public List<Ticket> findOpenTickets() {
        return stream().filter(t -> t.getState() != Ticket.State.CLOSED).map(this::copy).collect(Collectors.toList());
    }
}
