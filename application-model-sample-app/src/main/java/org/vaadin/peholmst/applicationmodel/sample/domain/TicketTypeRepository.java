package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.util.Comparator;

import org.vaadin.peholmst.applicationmodel.sample.domain.base.Repository;

/**
 * Created by petterwork on 01/06/2017.
 */
public class TicketTypeRepository extends Repository<TicketType> {

    public TicketTypeRepository() {
        super(TicketType.class);
        save(new TicketType("Automatic fire alarm"));
        save(new TicketType("Structure fire"));
        save(new TicketType("Terrain fire"));
        save(new TicketType("Vehicle fire"));
        save(new TicketType("Other fire"));
        save(new TicketType("Motor vehicle accident"));
        save(new TicketType("Medical emergency"));
        save(new TicketType("Water rescue"));
        save(new TicketType("Trench rescue"));
        save(new TicketType("High angle rescue"));
        save(new TicketType("Other rescue"));
        save(new TicketType("HazMat"));
        save(new TicketType("Oil spill"));
        save(new TicketType("Salvage"));
    }

    @Override
    protected Comparator<TicketType> getDefaultComparator() {
        return Comparator.comparing(TicketType::getDescription);
    }
}
