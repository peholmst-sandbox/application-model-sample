package org.vaadin.peholmst.applicationmodel.sample.ui;

import org.vaadin.peholmst.applicationmodel.sample.ApplicationProperties;
import org.vaadin.peholmst.applicationmodel.sample.ApplicationServices;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.TicketFormModel;

import com.vaadin.ui.HorizontalSplitPanel;

/**
 * Main view that contains the ticket form, the map and eventually the list of
 * open tickets as well.
 */
class MainView extends HorizontalSplitPanel {

    private TicketFormModel ticketFormModel;
    private TicketForm ticketForm;
    private TicketMap ticketMap;

    MainView() {
        ticketFormModel = new TicketFormModel(ApplicationServices.getInstance()::getGeoService);
        ticketForm = new TicketForm(ticketFormModel);
        ticketMap = new TicketMap(ApplicationProperties.getInstance().getProperty("google.api-key"), ticketFormModel);

        setFirstComponent(ticketForm);
        setSecondComponent(ticketMap);
        setSplitPosition(550, Unit.PIXELS);
    }
}
