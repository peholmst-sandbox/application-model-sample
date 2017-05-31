package org.vaadin.peholmst.applicationmodel.framework;

import java.util.Objects;

import com.vaadin.shared.Registration;

/**
 * TODO Document me!
 */
abstract class AbstractPropertyBinding<V, T, P extends Property<V>> implements PropertyBinding<V, T, P> {

    final T target;
    final P property;
    Registration propertyRegistration;

    AbstractPropertyBinding(P property, T target) {
        this.property = Objects.requireNonNull(property);
        this.target = Objects.requireNonNull(target);

        propertyRegistration = property.addValueChangeListener(this::onPropertyValueChange);
    }

    protected abstract void onPropertyValueChange(Property.ValueChangeEvent<V> event);

    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public P getProperty() {
        return property;
    }

    @Override
    public void remove() {
        propertyRegistration.remove();
    }
}