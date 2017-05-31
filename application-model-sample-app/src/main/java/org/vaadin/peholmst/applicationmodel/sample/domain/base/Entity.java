package org.vaadin.peholmst.applicationmodel.sample.domain.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * TODO Document me
 */
public abstract class Entity implements Serializable {

    private final UUID uuid;
    private transient List<Object> domainEvents;
    private Long version;

    /**
     * 
     * @param uuid
     */
    public Entity(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "uuid must not be null");
    }

    /**
     *
     */
    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    /**
     *
     * @param source
     */
    public Entity(Entity source) {
        Objects.requireNonNull(source, "source must not be null");
        this.uuid = source.uuid;
        this.version = source.version;
    }

    /**
     *
     * @return
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     *
     * @return
     */
    public Long getVersion() {
        return version;
    }

    /**
     *
     * @param expectedVersion
     */
    protected void checkAndIncrementVersion(Long expectedVersion) {
        if (Objects.equals(version, expectedVersion)) {
            if (version == null) {
                version = 0L;
            } else {
                version = version + 1;
            }
        } else {
            throw new IllegalStateException("Optimistic locking failure");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Entity entity = (Entity) o;

        return uuid.equals(entity.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    /**
     *
     * @param domainEvent
     */
    protected void registerDomainEvent(Object domainEvent) {
        getDomainEvents().add(domainEvent);
    }

    /**
     * 
     */
    protected void commitDomainEvents() {
        getDomainEvents().forEach(DomainEvents.getInstance()::publish);
        getDomainEvents().clear();
    }

    private List<Object> getDomainEvents() {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        return domainEvents;
    }
}
