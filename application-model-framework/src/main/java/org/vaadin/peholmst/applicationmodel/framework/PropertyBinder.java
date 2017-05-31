package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.server.SerializableSupplier;
import com.vaadin.ui.Label;

/**
 * TODO Document me
 */
public final class PropertyBinder {

    private PropertyBinder() {
    }

    public static <V, T extends HasValue<V>> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target) {
        return new FieldPropertyBinding<>(property, target);
    }

    public static <V, T extends HasValue<V>> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target, V nullValue) {
        return new FieldPropertyBinding<>(property, target, nullValue);
    }

    public static <V, T extends HasValue<V>> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target, SerializableSupplier<V> nullValueSupplier) {
        return new FieldPropertyBinding<>(property, target, nullValueSupplier);
    }

    public static <V, T extends HasValue<O>, O> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target, Converter<O, V> converter) {
        return new ConvertedFieldPropertyBinding<>(property, target, converter);
    }

    public static <V, T extends HasValue<O>, O> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target, Converter<O, V> converter, O nullValue) {
        return new ConvertedFieldPropertyBinding<>(property, target, converter, nullValue);
    }

    public static <V, T extends HasValue<O>, O> PropertyBinding<V, T, WritableProperty<V>> bind(
            WritableProperty<V> property, T target, Converter<O, V> converter,
            SerializableSupplier<O> nullValueSupplier) {
        return new ConvertedFieldPropertyBinding<>(property, target, converter, nullValueSupplier);
    }

    public static PropertyBinding<String, Label, Property<String>> bind(Property<String> property, Label target) {
        return new LabelPropertyBinding<>(property, target);
    }

    public static <V> PropertyBinding<V, Label, Property<V>> bind(Property<V> property, Label target,
            Converter<String, V> converter) {
        return new LabelPropertyBinding<>(property, target, converter);
    }
}
