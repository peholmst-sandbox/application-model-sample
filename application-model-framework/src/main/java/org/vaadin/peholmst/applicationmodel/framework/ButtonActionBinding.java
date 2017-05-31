package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;

/**
 * TODO document me
 */
class ButtonActionBinding extends AbstractActionBinding<Button> {

    private Registration clickListenerRegistration;

    ButtonActionBinding(Action action, Button target) {
        super(action, target);
    }

    @Override
    public void bind() {
        super.bind();
        clickListenerRegistration = getTarget().addClickListener(event -> getAction().run());
    }

    @Override
    public void remove() {
        clickListenerRegistration.remove();
        super.remove();
    }

    @Override
    protected void updateCaption() {
        getTarget().setCaption(getAction().getCaption());
    }

    @Override
    protected void updateDescription() {
        getTarget().setDescription(getAction().getDescription());
    }

    @Override
    protected void updateEnabled() {
        getTarget().setEnabled(getAction().isEnabled());
    }

    @Override
    protected void updateVisible() {
        getTarget().setVisible(getAction().isVisible());
    }
}
