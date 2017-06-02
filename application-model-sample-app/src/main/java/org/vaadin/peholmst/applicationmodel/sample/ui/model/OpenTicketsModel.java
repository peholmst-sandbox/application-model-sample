package org.vaadin.peholmst.applicationmodel.sample.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.peholmst.applicationmodel.framework.Action;
import org.vaadin.peholmst.applicationmodel.framework.WritableProperty;
import org.vaadin.peholmst.applicationmodel.framework.eventbus.EventBus;
import org.vaadin.peholmst.applicationmodel.sample.domain.Ticket;
import org.vaadin.peholmst.applicationmodel.sample.domain.TicketRepository;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.SerializableSupplier;
import com.vaadin.shared.Registration;
import com.vaadin.ui.UI;

/**
 * Created by petterwork on 01/06/2017.
 */
public class OpenTicketsModel implements Serializable {

    private final SerializableSupplier<TicketRepository> ticketRepository;
    private final List<Ticket> openTickets;
    private final ListDataProvider<Ticket> openTicketsProvider;
    private final WritableProperty<Ticket> currentTicket = new WritableProperty<>();
    private final Action openTicket;
    private transient Registration eventBusRegistration;

    public OpenTicketsModel(SerializableSupplier<TicketRepository> ticketRepository,
            SerializableSupplier<EventBus> eventBus) {
        this.ticketRepository = ticketRepository;
        openTickets = new ArrayList<>(ticketRepository.get().findOpenTickets());
        openTicketsProvider = new ListDataProvider<>(Collections.unmodifiableList(openTickets));
        openTicket = new Action("Open ticket", "Open a new incident ticket", this::onOpenTicket);
        // This is a transient field. To make your app fully serializable, you
        // should deal with this when your component is deserialized.
        eventBusRegistration = eventBus.get().subscribe(this::onEvent);
    }

    /**
     * Removes the registration to the event bus. This is important since the
     * event bus and the model live in different scopes. Forgetting to remove
     * the registration causes a memory leak.
     */
    public void dispose() {
        if (eventBusRegistration != null) {
            eventBusRegistration.remove();
        }
    }

    private void onEvent(Object event) {
        // This is a bit ugly. It would be better to specify what type of event
        // we are interested in at the subscription step. Maybe in a future
        // revision.
        if (event instanceof TicketUpdatedEvent) {
            UI.getCurrent().access(() -> {
                // This is also a bit quick and dirty. A more sophisticated way
                // would be to only update the item that has changed and leave
                // the rest. In this case it does not matter since we're dealing
                // with very small amounts of data.
                openTickets.clear();
                openTickets.addAll(ticketRepository.get().findOpenTickets());
                // If the current ticket is no longer in the list, deselect it.
                currentTicket.getValue().filter(t -> !openTickets.contains(t))
                        .ifPresent(t -> currentTicket.setValue(null));
                // Finally refresh the views that show open tickets
                openTicketsProvider.refreshAll();
            });
        }
    }

    private void onOpenTicket() {
        Ticket ticket = new Ticket();
        ticketRepository.get().save(ticket);
        currentTicket.setValue(ticket);
    }

    public ListDataProvider<Ticket> openTickets() {
        return openTicketsProvider;
    }

    public WritableProperty<Ticket> currentTicket() {
        return currentTicket;
    }

    public Action openTicket() {
        return openTicket;
    }
}
