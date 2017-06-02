package org.vaadin.peholmst.applicationmodel.sample.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * Main UI for the sample application.
 */
@Theme("mytheme")
@Push
public class SampleUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        // Currently our application consists of only one view, so there is no
        // point in setting up a navigator. Just show the view.
        setContent(new MainView());
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = SampleUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }
}
