package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.util.Objects;
import java.util.UUID;

import org.vaadin.peholmst.applicationmodel.sample.domain.base.Entity;

/**
 * TODO document me
 */
public class TicketType extends Entity {

    private String description = "";

    public TicketType(UUID uuid) {
        super(uuid);
    }

    public TicketType() {
    }

    public TicketType(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public TicketType(TicketType source) {
        super(source);
        description = source.description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }
}
