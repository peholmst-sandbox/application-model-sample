package org.vaadin.peholmst.applicationmodel.sample.ui;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.vaadin.peholmst.applicationmodel.framework.PropertyBinder;
import org.vaadin.peholmst.applicationmodel.sample.domain.TicketType;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.NamedPlace;
import org.vaadin.peholmst.applicationmodel.sample.ui.components.CoordinatesField;
import org.vaadin.peholmst.applicationmodel.sample.ui.converters.AccuracyConverter;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.TicketFormModel;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A form for editing a ticket. This is essentially the UI bound to the
 * {@link TicketFormModel}. It does not contain any application logic at all.
 */
class TicketForm extends VerticalLayout {

    private final CoordinatesField coordinates;
    private final Label coordinateAccuracy;
    private TextField address;
    private ListSelect<NamedPlace> places;
    private TextArea additionalAddressDetails;
    private ComboBox<TicketType> ticketType;
    private TextArea ticketDetails;
    private TextField callerName;
    private TextField callerPhone;
    private Button dispatch;
    private Button discard;
    private Button putOnHold;

    /**
     * Creates a new {@code TicketForm}. The form and the model are expected to
     * be in the same scope, which is why the form cannot be unbound from the
     * model.
     * 
     * @param ticketFormModel
     *            the model that this form is to be bound to (not {@code null}).
     */
    TicketForm(TicketFormModel ticketFormModel) {
        Objects.requireNonNull(ticketFormModel);

        Label locationHeader = new Label("Incident location");
        locationHeader.addStyleName(ValoTheme.LABEL_H4);
        addComponent(locationHeader);

        address = new TextField();
        address.setWidth(100, Unit.PERCENTAGE);
        address.setPlaceholder("Start typing to select an address");
        address.setDescription("Address");
        PropertyBinder.bind(ticketFormModel.address(), address);
        addComponent(address);

        places = new ListSelect<>();
        places.setRows(4);
        places.setWidth(100, Unit.PERCENTAGE);
        places.setItemCaptionGenerator(NamedPlace::getName);
        places.setVisible(false);

        // Manually bind this property/field since it is a one-way binding and
        // requires some simple conversion on the way. Using a converter also
        // works but is overkill.
        places.addValueChangeListener(event -> ticketFormModel.placeSelection()
                .setValue(event.getValue().stream().findFirst().orElse(null), true));

        // Here we are binding to the items and not to the value. This could be
        // done better with a property designed for items and not a single
        // value.
        ticketFormModel.places().addValueChangeListener(event -> {
            List<NamedPlace> items = event.getNewValue().orElse(Collections.emptyList());
            this.places.setItems(items);
            this.places.setVisible(items.size() > 0);
        });
        addComponent(places);

        coordinates = new CoordinatesField();
        coordinates.setCaption("Coordinates (WGS-84)");
        PropertyBinder.bind(ticketFormModel.coordinates(), coordinates);

        coordinateAccuracy = new Label();
        coordinateAccuracy.setCaption("Accuracy");
        PropertyBinder.bind(ticketFormModel.coordinateAccuracy(), coordinateAccuracy, new AccuracyConverter());

        HorizontalLayout coordinatesLine = new HorizontalLayout(coordinates, coordinateAccuracy);
        coordinatesLine.setMargin(false);
        addComponent(coordinatesLine);

        additionalAddressDetails = new TextArea();
        additionalAddressDetails.setRows(3);
        additionalAddressDetails.setPlaceholder("Enter any additional address details");
        additionalAddressDetails.setWidth(100, Unit.PERCENTAGE);
        PropertyBinder.bind(ticketFormModel.addressDetails(), additionalAddressDetails);
        addComponent(additionalAddressDetails);

        Label incidentDetailsHeader = new Label("Incident details");
        incidentDetailsHeader.addStyleName(ValoTheme.LABEL_H4);
        addComponent(incidentDetailsHeader);

        ticketType = new ComboBox<>();
        ticketType.setPlaceholder("Select an incident type");
        ticketType.setWidth(100, Unit.PERCENTAGE);
        PropertyBinder.bind(ticketFormModel.type(), ticketType);
        addComponent(ticketType);

        ticketDetails = new TextArea();
        ticketDetails.setRows(4);
        ticketDetails.setPlaceholder("Describe what has happened");
        ticketDetails.setWidth(100, Unit.PERCENTAGE);
        PropertyBinder.bind(ticketFormModel.details(), ticketDetails);
        addComponent(ticketDetails);

        Label callerHeader = new Label("Caller information");
        callerHeader.addStyleName(ValoTheme.LABEL_H4);
        addComponent(callerHeader);

        callerName = new TextField();
        callerName.setWidth(100, Unit.PERCENTAGE);
        callerName.setPlaceholder("Name of caller");
        PropertyBinder.bind(ticketFormModel.caller(), callerName);

        callerPhone = new TextField();
        callerPhone.setWidth(120, Unit.PIXELS);
        callerPhone.setPlaceholder("Phone");
        PropertyBinder.bind(ticketFormModel.callerPhone(), callerPhone);

        HorizontalLayout callerLine = new HorizontalLayout(callerName, callerPhone);
        callerLine.setMargin(false);
        callerLine.setExpandRatio(callerName, 1);
        callerLine.setWidth(100, Unit.PERCENTAGE);
        addComponent(callerLine);

        dispatch = new Button("Dispatch");
        dispatch.addStyleName(ValoTheme.BUTTON_PRIMARY);

        putOnHold = new Button("Put on hold");

        discard = new Button("Discard");
        discard.addStyleName(ValoTheme.BUTTON_DANGER);

        HorizontalLayout buttonLine = new HorizontalLayout(dispatch, putOnHold, discard);
        buttonLine.setMargin(false);
        addComponent(buttonLine);
        setComponentAlignment(buttonLine, Alignment.MIDDLE_RIGHT);

        // TODO Bindings for the buttons
    }
}
