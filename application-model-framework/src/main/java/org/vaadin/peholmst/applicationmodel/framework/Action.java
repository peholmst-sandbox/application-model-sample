package org.vaadin.peholmst.applicationmodel.framework;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * TODO document me
 */
public class Action implements Serializable {

    public static final String PROP_CAPTION = "caption";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_ENABLED = "enabled";
    public static final String PROP_VISIBLE = "visible";

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String caption;
    private String description;
    private boolean visible = true;
    private boolean enabled = true;
    private final ActionOperation operation;

    public Action() {
        operation = null;
    }

    public Action(String caption, String description) {
        this.caption = caption;
        this.description = description;
        this.operation = null;
    }

    public Action(ActionOperation operation) {
        this.operation = operation;
    }

    public Action(String caption, String description, ActionOperation operation) {
        this.caption = caption;
        this.description = description;
        this.operation = operation;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        final String old = this.caption;
        this.caption = caption;
        propertyChangeSupport.firePropertyChange(PROP_CAPTION, old, caption);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        final String old = this.description;
        this.description = description;
        propertyChangeSupport.firePropertyChange(PROP_DESCRIPTION, old, description);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        final boolean old = this.visible;
        this.visible = visible;
        propertyChangeSupport.firePropertyChange(PROP_VISIBLE, old, visible);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        final boolean old = this.enabled;
        this.enabled = enabled;
        propertyChangeSupport.firePropertyChange(PROP_ENABLED, old, enabled);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public boolean hasListeners(String propertyName) {
        return propertyChangeSupport.hasListeners(propertyName);
    }

    public void run() {
        if (operation != null) {
            operation.run();
        }
    }

    public interface ActionOperation extends Serializable, Runnable {
    }
}
