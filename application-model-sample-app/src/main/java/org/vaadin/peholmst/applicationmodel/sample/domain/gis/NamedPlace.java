package org.vaadin.peholmst.applicationmodel.sample.domain.gis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TODO document me
 */
public class NamedPlace implements Serializable {

    private final String name;
    private final List<String> parts;
    private final String googlePlaceId;

    public NamedPlace(String name, String googlePlaceId, List<String> parts) {
        this.name = Objects.requireNonNull(name);
        this.googlePlaceId = Objects.requireNonNull(googlePlaceId);
        this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
    }

    public String getName() {
        return name;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public List<String> getParts() {
        return parts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        NamedPlace that = (NamedPlace) o;

        return googlePlaceId.equals(that.googlePlaceId);
    }

    @Override
    public int hashCode() {
        return googlePlaceId.hashCode();
    }
}
