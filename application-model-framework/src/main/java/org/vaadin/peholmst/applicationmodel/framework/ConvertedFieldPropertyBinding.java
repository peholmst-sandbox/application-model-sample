package org.vaadin.peholmst.applicationmodel.framework;

import java.util.Objects;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.server.SerializableSupplier;
import com.vaadin.server.UserError;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * TODO Document me!
 */
class ConvertedFieldPropertyBinding<V, T extends HasValue<O>, O>
        extends AbstractPropertyBinding<V, T, WritableProperty<V>> {

    private final Converter<O, V> converter;
    private final SerializableSupplier<O> nullValueSupplier;
    private Registration targetRegistration;

    ConvertedFieldPropertyBinding(WritableProperty<V> property, T target, Converter<O, V> converter,
            SerializableSupplier<O> nullValueSupplier) {
        super(property, target);
        this.converter = Objects.requireNonNull(converter);
        this.nullValueSupplier = Objects.requireNonNull(nullValueSupplier);
        targetRegistration = target.addValueChangeListener(this::onTargetValueChange);
    }

    ConvertedFieldPropertyBinding(WritableProperty<V> property, T target, Converter<O, V> converter, O nullValue) {
        this(property, target, converter, () -> nullValue);
    }

    ConvertedFieldPropertyBinding(WritableProperty<V> property, T target, Converter<O, V> converter) {
        this(property, target, converter, (O) null);
    }

    private void onTargetValueChange(HasValue.ValueChangeEvent<O> event) {
        propertyRegistration.remove();
        try {
            clearFieldError();
            Result<V> result = converter.convertToModel(event.getValue(), getValueContext());
            result.handle(v -> property.setValue(v, event.isUserOriginated()), this::setFieldError);
        } finally {
            propertyRegistration = property.addValueChangeListener(this::onPropertyValueChange);
        }
    }

    private void setFieldError(String message) {
        if (target instanceof AbstractComponent) {
            ((AbstractComponent) target).setComponentError(new UserError(message));
        }
    }

    private void clearFieldError() {
        if (target instanceof AbstractComponent) {
            ((AbstractComponent) target).setComponentError(null);
        }
    }

    @Override
    protected void onPropertyValueChange(Property.ValueChangeEvent<V> event) {
        targetRegistration.remove();
        try {
            clearFieldError();
            target.setValue(event.getNewValue().map(v -> converter.convertToPresentation(v, getValueContext()))
                    .orElseGet(nullValueSupplier));
        } finally {
            targetRegistration = target.addValueChangeListener(this::onTargetValueChange);
        }
    }

    private ValueContext getValueContext() {
        if (target instanceof Component) {
            return new ValueContext((Component) target);
        } else {
            return new ValueContext();
        }
    }
}
