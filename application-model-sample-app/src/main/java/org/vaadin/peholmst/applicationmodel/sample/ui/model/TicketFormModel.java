package org.vaadin.peholmst.applicationmodel.sample.ui.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.vaadin.peholmst.applicationmodel.framework.Property;
import org.vaadin.peholmst.applicationmodel.framework.WritableProperty;
import org.vaadin.peholmst.applicationmodel.sample.domain.Coordinates;
import org.vaadin.peholmst.applicationmodel.sample.domain.TicketType;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.Accuracy;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.GeoService;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.NamedPlace;

import com.vaadin.server.SerializableSupplier;

/**
 * TODO Document me
 */
public class TicketFormModel implements Serializable {

    // All properties are writable, but some of them are exposed as read-only by
    // the accessor methods.

    // TODO Date, time and ID

    private final WritableProperty<String> address = new WritableProperty<>();
    private final WritableProperty<List<NamedPlace>> places = new WritableProperty<>();
    private final WritableProperty<NamedPlace> placeSelection = new WritableProperty<>();
    private final WritableProperty<Coordinates> coordinates = new WritableProperty<>();
    private final WritableProperty<Accuracy> coordinateAccuracy = new WritableProperty<>();
    private final WritableProperty<String> addressDetails = new WritableProperty<>();
    private final WritableProperty<TicketType> type = new WritableProperty<>();
    private final WritableProperty<String> details = new WritableProperty<>();
    private final WritableProperty<String> caller = new WritableProperty<>();
    private final WritableProperty<String> callerPhone = new WritableProperty<>();

    private final SerializableSupplier<GeoService> geoService;

    public TicketFormModel(SerializableSupplier<GeoService> geoService) {
        this.geoService = Objects.requireNonNull(geoService);
        coordinates.addValueChangeListener(this::onCoordinatesChanged);
        address.addValueChangeListener(this::onAddressChanged);
        placeSelection.addValueChangeListener(this::onPlaceSelectionChanged);
    }

    private void onCoordinatesChanged(Property.ValueChangeEvent<Coordinates> event) {
        if (event.isUserOriginated()) {
            event.getNewValue().ifPresent(coordinates -> {
                // If the user has changed the coordinates, we assume they are
                // correct and indicate this with a special accuracy class in
                // the UI.
                coordinateAccuracy.setValue(Accuracy.USER_ENTERED);
                // However, we also need a textual address. If Google can find
                // one, we'll update the UI. Otherwise, we'll use whatever the
                // user has entered.
                geoService.get().findPlace(coordinates).ifPresent(placeSelection::setValue);
            });
        }
    }

    private void onAddressChanged(Property.ValueChangeEvent<String> event) {
        placeSelection.setValue(null);
        places.setValue(Collections.emptyList());
        if (event.isUserOriginated()) {
            // If the user has changed the address field, we do a search for
            // places. These will show up in the UI for the user to choose from.
            event.getNewValue().ifPresent(searchTerm -> places.setValue(geoService.get().findPlaces(searchTerm)));
        }
    }

    private void onPlaceSelectionChanged(Property.ValueChangeEvent<NamedPlace> event) {
        event.getNewValue().ifPresent(selection -> {
            // The place can be set programmatically (by onCoordinatesChanged)
            // or by the user (through the UI). Regardless of how the value is
            // changed, the address field should always be updated since the
            // list of places might not even be visible in the UI.
            address.setValue(selection.getName());
            if (event.isUserOriginated()) {
                // However, if the user has picked a location, then we need to
                // figure out the coordinates for the location. Fortunately,
                // Google can help us with this.
                geoService.get().findCoordinates(selection).ifPresent(p -> {
                    coordinates.setValue(p.getLeft());
                    coordinateAccuracy.setValue(p.getRight());
                });
            }
        });
    }

    public WritableProperty<Coordinates> coordinates() {
        return coordinates;
    }

    public Property<Accuracy> coordinateAccuracy() {
        return coordinateAccuracy;
    }

    public WritableProperty<String> address() {
        return address;
    }

    public Property<List<NamedPlace>> places() {
        return places;
    }

    public WritableProperty<NamedPlace> placeSelection() {
        return placeSelection;
    }

    public WritableProperty<String> addressDetails() {
        return addressDetails;
    }

    public WritableProperty<TicketType> type() {
        return type;
    }

    public WritableProperty<String> details() {
        return details;
    }

    public WritableProperty<String> caller() {
        return caller;
    }

    public WritableProperty<String> callerPhone() {
        return callerPhone;
    }

    // TODO Add connection to backend
}
