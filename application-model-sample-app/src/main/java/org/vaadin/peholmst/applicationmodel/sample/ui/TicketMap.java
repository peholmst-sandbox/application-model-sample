package org.vaadin.peholmst.applicationmodel.sample.ui;

import java.util.Objects;

import org.vaadin.peholmst.applicationmodel.framework.Property;
import org.vaadin.peholmst.applicationmodel.sample.domain.Coordinates;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.Accuracy;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.TicketFormModel;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CustomComponent;

/**
 * A Google map wrapper that currently only shows the location of the ticket
 * being edited inside the {@link TicketFormModel}. Eventually, all other open
 * tickets will also show up in the map.
 */
class TicketMap extends CustomComponent {

    private final TicketFormModel ticketFormModel;
    private final GoogleMap googleMap;

    private GoogleMapMarker currentTicketMarker;

    /**
     * Creates a new {@code TicketMap}. The map and the model are expected to be
     * in the same scope, which is why the map cannot be unbound from the model.
     * 
     * @param apiKey
     *            the Google API key to use for the Google map.
     * @param ticketFormModel
     *            the model that the map is to be bound to (not {@code null}).
     */
    TicketMap(String apiKey, TicketFormModel ticketFormModel) {
        this.ticketFormModel = Objects.requireNonNull(ticketFormModel);

        googleMap = new GoogleMap(apiKey, null, null);
        googleMap.setSizeFull();
        googleMap.addMarkerDragListener(this::onMarkerDragged);
        setCompositionRoot(googleMap);
        setSizeFull();

        ticketFormModel.coordinates().addValueChangeListener(this::onCoordinatesChange);
        ticketFormModel.coordinateAccuracy().addValueChangeListener(this::onCoordinateAccuracyChange);
    }

    /**
     * When the coordinates of the ticket model changes, the current ticker
     * marker will be changed and the map will center on the new location.
     */
    private void onCoordinatesChange(Property.ValueChangeEvent<Coordinates> event) {
        event.getNewValue().map(coordinates -> new LatLon(coordinates.getLatitude(), coordinates.getLongitude()))
                .ifPresent(this::updateCurrentTicketMarker);
    }

    /**
     * When the current ticker marker is dragged to a new location, the model
     * will be updated with the new coordinates. In other words, the map can
     * also be used to edit the ticket.
     */
    private void onMarkerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
        if (draggedMarker.equals(currentTicketMarker)) {
            ticketFormModel.coordinates().setValue(
                    new Coordinates(draggedMarker.getPosition().getLat(), draggedMarker.getPosition().getLon()), true);
        }
    }

    private void updateCurrentTicketMarker(LatLon latLon) {
        googleMap.setCenter(latLon);
        if (currentTicketMarker != null) {
            if (currentTicketMarker.getPosition().equals(latLon)) {
                return; // The marker has not moved
            }
            googleMap.removeMarker(currentTicketMarker);
        }
        currentTicketMarker = new GoogleMapMarker("Current ticket", latLon, true);
        googleMap.addMarker(currentTicketMarker);
    }

    /**
     * Depending on the accuracy of the ticket coordinates, the map will
     * automatically zoom in or zoom out.
     */
    private void onCoordinateAccuracyChange(Property.ValueChangeEvent<Accuracy> event) {
        event.getNewValue().map(this::toZoomLevel).ifPresent(googleMap::setZoom);
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
}
