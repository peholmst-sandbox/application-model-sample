package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.ui.MenuBar;

/**
 * TODO document me
 */
class MenuItemActionBinding extends AbstractActionBinding<MenuBar.MenuItem> {

    public MenuItemActionBinding(Action action, MenuBar.MenuItem target) {
        super(action, target);
    }

    @Override
    public void bind() {
        super.bind();
        getTarget().setCommand(item -> getAction().run());
    }

    @Override
    public void remove() {
        getTarget().setCommand(null);
        super.remove();
    }

    @Override
    protected void updateCaption() {
        getTarget().setText(getAction().getCaption());
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
