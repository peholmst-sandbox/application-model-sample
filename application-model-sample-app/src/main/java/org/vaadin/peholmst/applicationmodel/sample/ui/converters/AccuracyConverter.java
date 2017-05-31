package org.vaadin.peholmst.applicationmodel.sample.ui.converters;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.Accuracy;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * A one-way converter that converts a {@link Accuracy} enum constant to a human
 * readable String.
 */
public class AccuracyConverter implements Converter<String, Accuracy> {

    @Override
    public Result<Accuracy> convertToModel(String value, ValueContext context) {
        return Result.error("This is a one-way converter");
    }

    @Override
    public String convertToPresentation(Accuracy value, ValueContext context) {
        return value == null ? null : StringUtils.capitalize(value.name().toLowerCase().replace('_', ' '));
    }
}
