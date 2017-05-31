package org.vaadin.peholmst.applicationmodel.framework;

import java.io.Serializable;
import java.util.*;

import com.vaadin.shared.Registration;

/**
 * TODO document me
 */
public class Property<V> implements Serializable {

    private final Set<ValueChangeListenerRegistration<V>> valueChangeListeners = new HashSet<>();
    private V value;

    protected void setValue(V value) {
        setValue(value, false);
    }

    protected void setValue(V value, boolean userOriginated) {
        if (!Objects.equals(this.value, value)) {
            final V old = this.value;
            this.value = value;
            ValueChangeEvent<V> event = new ValueChangeEvent<>(this, old, value, userOriginated);
            new LinkedList<>(valueChangeListeners).forEach(r -> r.fire(event));
        }
    }

    public Optional<V> getValue() {
        return Optional.ofNullable(value);
    }

    public Registration addValueChangeListener(ValueChangeListener<V> valueChangeListener) {
        ValueChangeListenerRegistration<V> registration = new ValueChangeListenerRegistration<>(valueChangeListener);
        valueChangeListeners.add(registration);
        return registration;
    }

    private class ValueChangeListenerRegistration<V> implements Registration {

        private final ValueChangeListener<V> valueChangeListener;

        private ValueChangeListenerRegistration(ValueChangeListener<V> valueChangeListener) {
            this.valueChangeListener = valueChangeListener;
        }

        private void fire(ValueChangeEvent<V> event) {
            valueChangeListener.onPropertyValueChange(event);
        }

        @Override
        public void remove() {
            valueChangeListeners.remove(this);
        }
    }

    public static class ValueChangeEvent<V> implements Serializable {

        private final Property<V> property;
        private final V oldValue;
        private final V newValue;
        private final boolean userOriginated;

        public ValueChangeEvent(Property<V> property, V oldValue, V newValue, boolean userOriginated) {
            this.property = property;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.userOriginated = userOriginated;
        }

        public Property<V> getProperty() {
            return property;
        }

        public Optional<V> getOldValue() {
            return Optional.ofNullable(oldValue);
        }

        public Optional<V> getNewValue() {
            return Optional.ofNullable(newValue);
        }

        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    public interface ValueChangeListener<V> extends Serializable {
        void onPropertyValueChange(ValueChangeEvent<V> event);
    }
}
