package org.vaadin.peholmst.applicationmodel.sample.ui;

import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.peholmst.applicationmodel.framework.ActionBinder;
import org.vaadin.peholmst.applicationmodel.sample.ApplicationProperties;
import org.vaadin.peholmst.applicationmodel.sample.ApplicationServices;
import org.vaadin.peholmst.applicationmodel.sample.domain.Ticket;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.OpenTicketsModel;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.TicketFormModel;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

/**
 * Main view that contains the ticket form, the map and eventually the list of
 * open tickets as well.
 */
class MainView extends HorizontalSplitPanel {

    private final TicketFormModel ticketFormModel;
    private final TicketForm ticketForm;
    private final OpenTicketsModel openTicketsModel;
    private final TicketMap ticketMap;
    private final Grid<Ticket> openTicketsList;

    MainView() {
        openTicketsModel = new OpenTicketsModel(ApplicationServices.getInstance()::getTicketRepository,
                ApplicationServices.getInstance()::getEventBus);
        ticketFormModel = new TicketFormModel(ApplicationServices.getInstance()::getGeoService, openTicketsModel.currentTicket());
        ticketForm = new TicketForm(ticketFormModel);
        ticketMap = new TicketMap(ApplicationProperties.getInstance().getProperty("google.api-key"), ticketFormModel);

        openTicketsList = new Grid<>(Ticket.class);
        openTicketsList.setDataProvider(openTicketsModel.openTickets());
        openTicketsList.setSizeFull();

        setSplitPosition(550, Unit.PIXELS);

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setMargin(new MarginInfo(true, true, false, true));
        Button openTicket = new Button();
        openTicket.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        ActionBinder.bind(openTicketsModel.openTicket(), openTicket);
        buttonBar.addComponent(openTicket);

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(false);
        vl.setSpacing(false);
        vl.addComponent(buttonBar);
        vl.addComponent(ticketForm);
        setFirstComponent(vl);

        VerticalSplitPanel vsplit = new VerticalSplitPanel(ticketMap, openTicketsList);
        vsplit.setSplitPosition(300, Unit.PIXELS, true);
        setSecondComponent(vsplit);
    }

    @Override
    public void detach() {
        openTicketsModel.dispose();
        super.detach();
    }
}
