package org.vaadin.peholmst.applicationmodel.framework;

import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;

/**
 * TODO Document me
 */
public final class ActionBinder {

    private ActionBinder() {
    }

    public static ActionBinding<Button> bind(Action action, Button button) {
        final ButtonActionBinding binding = new ButtonActionBinding(action, button);
        binding.bind();
        return binding;
    }

    public static ActionBinding<MenuBar.MenuItem> bind(Action action, MenuBar.MenuItem menuItem) {
        return null;// TODO
    }
}
