package org.vaadin.peholmst.applicationmodel.framework;

import java.util.Objects;

import com.vaadin.data.HasValue;
import com.vaadin.server.SerializableSupplier;
import com.vaadin.shared.Registration;

/**
 * TODO Document me!
 */
class FieldPropertyBinding<V, T extends HasValue<V>> extends AbstractPropertyBinding<V, T, WritableProperty<V>> {

    private final SerializableSupplier<V> nullValueSupplier;
    private Registration targetRegistration;

    FieldPropertyBinding(WritableProperty<V> property, T target, SerializableSupplier<V> nullValueSupplier) {
        super(property, target);
        this.nullValueSupplier = Objects.requireNonNull(nullValueSupplier);
        targetRegistration = target.addValueChangeListener(this::onTargetValueChange);
    }

    FieldPropertyBinding(WritableProperty<V> property, T target, V nullValue) {
        this(property, target, () -> nullValue);
    }

    FieldPropertyBinding(WritableProperty<V> property, T target) {
        this(property, target, (V) null);
    }

    private void onTargetValueChange(HasValue.ValueChangeEvent<V> event) {
        propertyRegistration.remove();
        try {
            property.setValue(event.getValue(), event.isUserOriginated());
        } finally {
            propertyRegistration = property.addValueChangeListener(this::onPropertyValueChange);
        }
    }

    @Override
    protected void onPropertyValueChange(Property.ValueChangeEvent<V> event) {
        targetRegistration.remove();
        try {
            target.setValue(event.getNewValue().orElseGet(nullValueSupplier));
        } finally {
            targetRegistration = target.addValueChangeListener(this::onTargetValueChange);
        }
    }

    @Override
    public void remove() {
        super.remove();
        targetRegistration.remove();
    }
}
