package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.data.Converter;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.Label;

/**
 * TODO Document me!
 */
class LabelPropertyBinding<V, T extends Label> extends AbstractPropertyBinding<V, T, Property<V>> {

    private final Converter<String, V> converter;

    LabelPropertyBinding(Property<V> property, T target, Converter<String, V> converter) {
        super(property, target);
        this.converter = converter;
    }

    LabelPropertyBinding(Property<V> property, T target) {
        this(property, target, null);
    }

    @Override
    protected void onPropertyValueChange(Property.ValueChangeEvent<V> event) {
        String stringValue;
        if (converter == null) {
            stringValue = event.getNewValue().map(Object::toString).orElse(null);
        } else {
            stringValue = converter.convertToPresentation(event.getNewValue().orElse(null),
                    new ValueContext(getTarget()));
        }
        getTarget().setValue(stringValue);
    }
}
