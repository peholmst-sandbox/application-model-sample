package org.vaadin.peholmst.applicationmodel.sample.domain.base;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO document me
 */
public class Repository<E extends Entity> {

    private final Map<UUID, E> entityStore = new HashMap<>();
    private final Class<E> entityClass;

    /**
     *
     * @param entityClass
     */
    public Repository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     *
     * @param entity
     */
    public synchronized void save(E entity) {
        Long expectedVersion = findByUuid(entity.getUuid()).map(Entity::getVersion).orElse(null);
        entity.checkAndIncrementVersion(expectedVersion);
        entityStore.put(entity.getUuid(), copy(entity));
        entity.commitDomainEvents();
    }

    /**
     * 
     * @return
     */
    public synchronized List<E> findAll() {
        return stream().map(this::copy).collect(Collectors.toList());
    }

    /**
     * 
     * @return
     */
    protected Stream<E> stream() {
        return entityStore.values().stream().sorted(getDefaultComparator());
    }

    protected Comparator<E> getDefaultComparator() {
        return Comparator.comparing(e -> e.getUuid().toString());
    }

    /**
     * 
     * @param entity
     * @return
     */
    protected E copy(E entity) {
        try {
            return entityClass.getConstructor(entityClass).newInstance(entity);
        } catch (Exception ex) {
            throw new RuntimeException("Could not invoke copy constructor", ex);
        }
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public synchronized Optional<E> findByUuid(UUID uuid) {
        return Optional.ofNullable(entityStore.get(uuid)).map(this::copy);
    }
}
