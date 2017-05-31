package org.vaadin.peholmst.applicationmodel.framework;

import java.beans.PropertyChangeEvent;

/**
 * TODO document me
 */
abstract class AbstractActionBinding<T> implements ActionBinding<T> {
    private final T target;
    private final Action action;

    AbstractActionBinding(Action action, T target) {
        this.action = action;
        this.target = target;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public T getTarget() {
        return target;
    }

    public void bind() {
        updateCaption();
        updateDescription();
        updateEnabled();
        updateVisible();
        action.addPropertyChangeListener(this::onActionPropertyChange);
    }

    @Override
    public void remove() {
        action.removePropertyChangeListener(this::onActionPropertyChange);
    }

    private void onActionPropertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
        case Action.PROP_CAPTION:
            updateCaption();
            break;
        case Action.PROP_DESCRIPTION:
            updateDescription();
            break;
        case Action.PROP_ENABLED:
            updateEnabled();
            break;
        case Action.PROP_VISIBLE:
            updateVisible();
            break;
        }
    }

    protected abstract void updateCaption();

    protected abstract void updateDescription();

    protected abstract void updateEnabled();

    protected abstract void updateVisible();
}
