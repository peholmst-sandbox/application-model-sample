package org.vaadin.peholmst.applicationmodel.sample.ui.components;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import org.vaadin.peholmst.applicationmodel.framework.PropertyBinder;
import org.vaadin.peholmst.applicationmodel.framework.WritableProperty;
import org.vaadin.peholmst.applicationmodel.sample.domain.Coordinates;

import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * A custom field for editing {@link Coordinates}.
 */
public class CoordinatesField extends CustomField<Coordinates> {

    private final TextField latitudeField;
    private final TextField longitudeField;

    private final WritableProperty<Double> latitude = new WritableProperty<>();
    private final WritableProperty<Double> longitude = new WritableProperty<>();

    private Coordinates value;

    private boolean updatingValue = false;

    public CoordinatesField() {
        latitudeField = new TextField();
        latitudeField.setPlaceholder("Lat (DD)");
        latitudeField.setDescription("Latitude (Decimal Degrees)");
        latitudeField.setWidth(100, Unit.PIXELS);
        PropertyBinder.bind(latitude, latitudeField,
                new CoordinateConverter("Please enter a valid latitude coordinate"), "");

        longitudeField = new TextField();
        longitudeField.setPlaceholder("Lng (DD)");
        longitudeField.setDescription("Longitude (Decimal Degrees)");
        longitudeField.setWidth(100, Unit.PIXELS);
        PropertyBinder.bind(longitude, longitudeField,
                new CoordinateConverter("Please enter a valid longitude coordinate"), "");

        latitude.addValueChangeListener(event -> updateValue());
        longitude.addValueChangeListener(event -> updateValue());
    }

    @Override
    protected Component initContent() {
        return new HorizontalLayout(latitudeField, longitudeField);
    }

    private void updateValue() {
        updatingValue = true;
        try {
            Optional<Double> lat = latitude.getValue();
            Optional<Double> lng = longitude.getValue();
            if (lat.isPresent() && lng.isPresent()) {
                setValue(new Coordinates(lat.get(), lng.get()), true);
            } else {
                setValue(null, true);
            }
        } finally {
            updatingValue = false;
        }
    }

    @Override
    protected void doSetValue(Coordinates value) {
        this.value = value;
        if (!updatingValue) {
            latitude.setValue(value == null ? null : value.getLatitude());
            longitude.setValue(value == null ? null : value.getLongitude());
        }
    }

    @Override
    public Coordinates getValue() {
        return value;
    }

    private static class CoordinateConverter extends StringToDoubleConverter {

        CoordinateConverter(String errorMessage) {
            super(errorMessage);
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            nf.setMaximumFractionDigits(6);
            return nf;
        }
    }
}
