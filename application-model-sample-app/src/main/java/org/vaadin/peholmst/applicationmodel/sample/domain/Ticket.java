package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.vaadin.peholmst.applicationmodel.sample.domain.base.Entity;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.Accuracy;

/**
 * TODO document me
 */
public class Ticket extends Entity {

    private Instant opened;
    private Instant closed;
    private String closeReason;
    private TicketType type;
    private String details;
    private String caller;
    private String callerPhone;
    private Coordinates location;
    private Accuracy locationAccuracy;
    private String address;
    private String addressDetails;
    private State state = State.NEW;

    public Ticket(UUID uuid, Instant opened) {
        super(uuid);
        this.opened = Objects.requireNonNull(opened);
    }

    public Ticket() {
        opened = Instant.now();
        registerDomainEvent(new TicketOpenedEvent(this));
    }

    public Ticket(Ticket source) {
        super(source);
        opened = source.opened;
        closed = source.closed;
        type = source.type;
        details = source.details;
        caller = source.caller;
        callerPhone = source.callerPhone;
        location = source.location;
        locationAccuracy = source.locationAccuracy;
        address = source.address;
        addressDetails = source.addressDetails;
        state = source.state;
    }

    public void close(String reason) {
        if (opened != null && closed == null) {
            closed = Instant.now();
            closeReason = reason;
            state = State.CLOSED;
            registerDomainEvent(new TicketClosedEvent(this));
        }
    }

    public Instant getOpened() {
        return opened;
    }

    public Optional<String> getCloseReason() {
        return Optional.ofNullable(closeReason);
    }

    public Optional<Instant> getClosed() {
        return Optional.ofNullable(closed);
    }

    public State getState() {
        return state;
    }

    public TicketType getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallerPhone() {
        return callerPhone;
    }

    public Coordinates getLocation() {
        return location;
    }

    public Accuracy getLocationAccuracy() {
        return locationAccuracy;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public enum State {
        NEW, ON_HOLD, DISPATCHED, CLOSED
    }
}
