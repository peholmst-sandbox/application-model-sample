package org.vaadin.peholmst.applicationmodel.sample.ui;

import java.io.Serializable;
import java.util.*;

import org.vaadin.peholmst.applicationmodel.sample.domain.Coordinates;
import org.vaadin.peholmst.applicationmodel.sample.domain.Ticket;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.Accuracy;

import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.Registration;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CustomComponent;

/**
 * TODO Document me!
 */
class TicketMap extends CustomComponent {

    private final GoogleMap googleMap;

    private ListDataProvider<Ticket> openTickets;
    private Registration openTicketsRegistration;

    private final Map<Ticket, GoogleMapMarker> ticketToMarkerMap = new HashMap<>();

    private CurrentTicketDragCallback currentTicketDragCallback;
    private Ticket currentTicket;
    private TicketClickedCallback ticketClickedCallback;

    /**
     * Creates a new {@code TicketMap}. You also want to specify a
     * {@link #setOpenTickets(ListDataProvider) data provider} for open tickets
     * and callbacks for
     * {@link #setCurrentTicketDragCallback(CurrentTicketDragCallback) moving
     * the current ticket} and
     * {@link #setTicketClickedCallback(TicketClickedCallback) clicking on any
     * ticket}. You can change the current ticket by calling
     * {@link #setCurrentTicket(Ticket)}.
     * 
     * @param apiKey
     *            the Google API key to use for the Google map.
     */
    TicketMap(String apiKey) {
        googleMap = new GoogleMap(apiKey, null, null);
        googleMap.setSizeFull();
        googleMap.addMarkerDragListener(this::onMarkerDragged);
        googleMap.addMarkerClickListener(this::onMarkerClicked);
        setCompositionRoot(googleMap);
        setSizeFull();
    }

    /**
     * Sets the data provider that contains all the currently open tickets. The
     * tickets will show up on the map if they have their location set. The data
     * provider can also be {@code null}, removing all the markers.
     */
    public void setOpenTickets(ListDataProvider<Ticket> openTickets) {
        if (openTicketsRegistration != null) {
            openTicketsRegistration.remove();
            openTicketsRegistration = null;
        }
        this.openTickets = openTickets;
        if (openTickets != null) {
            openTicketsRegistration = openTickets.addDataProviderListener(this::onOpenTicketsChange);
        }
        updateAllTickets();
    }

    private void onOpenTicketsChange(DataChangeEvent<Ticket> event) {
        if (event instanceof DataChangeEvent.DataRefreshEvent) {
            updateSingleTicket(((DataChangeEvent.DataRefreshEvent<Ticket>) event).getItem());
        } else {
            updateAllTickets();
        }
    }

    private void updateAllTickets() {
        googleMap.clearMarkers();
        ticketToMarkerMap.clear();
        if (openTickets != null) {
            openTickets.getItems().forEach(this::updateSingleTicket);
        }
    }

    private void updateSingleTicket(Ticket ticket) {
        GoogleMapMarker marker = ticketToMarkerMap.get(ticket);
        if (marker != null) {
            ticketToMarkerMap.remove(ticket);
            googleMap.removeMarker(marker);
        }
        createMarker(ticket).ifPresent(m -> {
            ticketToMarkerMap.put(ticket, m);
            googleMap.addMarker(m);
            if (isCurrentTicket(ticket)) {
                if (ticket.getLocationAccuracy() != null) {
                    googleMap.setZoom(toZoomLevel(ticket.getLocationAccuracy()));
                }
                googleMap.setCenter(m.getPosition());
            }
        });
    }

    /**
     * Creates and returns a new marker for the given ticket, if the ticket has
     * a location. Otherwise, an empty Optional is returned. If the ticket also
     * happens to be the currently selected ticket, it will be draggable and
     * have a different icon than the other ticket markers.
     */
    private Optional<GoogleMapMarker> createMarker(Ticket ticket) {
        final boolean current = isCurrentTicket(ticket);
        String iconUrl = "http://maps.google.com/mapfiles/ms/icons/blue.png";
        if (current) {
            iconUrl = "http://maps.google.com/mapfiles/ms/icons/red.png";
        }
        if (ticket.getLocation() != null) {
            LatLon latLon = new LatLon(ticket.getLocation().getLatitude(), ticket.getLocation().getLongitude());
            String description = ticket.getType() != null ? ticket.getType().getDescription() : "(No ticket type)";
            return Optional.of(new GoogleMapMarker(description, latLon, current, iconUrl));
        }
        return Optional.empty();
    }

    /**
     * Returns the data provider that contains all the currently open tickets or
     * {@code null} if no provider has been set.
     */
    public ListDataProvider<Ticket> getOpenTickets() {
        return openTickets;
    }

    private boolean isCurrentTicket(Ticket ticket) {
        return Objects.equals(currentTicket, ticket);
    }

    /**
     * Returns the currently selected ticket or {@code null} if no ticket is
     * currently selected.
     */
    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    /**
     * Sets the currently selected ticket, or {@code null} to clear the
     * selection.
     */
    public void setCurrentTicket(Ticket currentTicket) {
        Ticket old = this.currentTicket;
        this.currentTicket = currentTicket;
        if (currentTicket != null) {
            updateSingleTicket(currentTicket);
        }
        if (old != null) {
            updateSingleTicket(old);
        }
    }

    private void onMarkerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
        if (currentTicketDragCallback != null) {
            findTicketByMarker(draggedMarker).filter(this::isCurrentTicket)
                    .ifPresent(ticket -> currentTicketDragCallback.onCurrentTicketDragged(ticket, new Coordinates(
                            draggedMarker.getPosition().getLat(), draggedMarker.getPosition().getLon())));
        }
    }

    private void onMarkerClicked(GoogleMapMarker clickedMarker) {
        if (ticketClickedCallback != null) {
            findTicketByMarker(clickedMarker).ifPresent(ticketClickedCallback::onTicketClicked);
        }
    }

    private Optional<Ticket> findTicketByMarker(GoogleMapMarker marker) {
        // TODO Replace with a reverse lookup map
        Set<Map.Entry<Ticket, GoogleMapMarker>> entries = ticketToMarkerMap.entrySet();
        for (Map.Entry<Ticket, GoogleMapMarker> entry : entries) {
            if (marker.equals(entry.getValue())) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    private int toZoomLevel(Accuracy accuracy) {
        switch (accuracy) {
        case CITY:
            return 13;
        case ROAD:
            return 15;
        case ADDRESS:
        case USER_ENTERED:
            return 18;
        default:
            return 10;
        }
    }

    /**
     * TODO Document me
     */
    public CurrentTicketDragCallback getCurrentTicketDragCallback() {
        return currentTicketDragCallback;
    }

    /**
     * TODO Document me
     */
    public void setCurrentTicketDragCallback(CurrentTicketDragCallback currentTicketDragCallback) {
        this.currentTicketDragCallback = currentTicketDragCallback;
    }

    /**
     * TODO Document me
     */
    public TicketClickedCallback getTicketClickedCallback() {
        return ticketClickedCallback;
    }

    /**
     * TODO Document me
     */
    public void setTicketClickedCallback(TicketClickedCallback ticketClickedCallback) {
        this.ticketClickedCallback = ticketClickedCallback;
    }

    /**
     * TODO Document me
     */
    @FunctionalInterface
    public interface CurrentTicketDragCallback extends Serializable {

        void onCurrentTicketDragged(Ticket ticket, Coordinates newPosition);
    }

    /**
     * TODO Document me
     */
    @FunctionalInterface
    public interface TicketClickedCallback extends Serializable {

        void onTicketClicked(Ticket ticket);
    }
}
