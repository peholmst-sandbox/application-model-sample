package org.vaadin.peholmst.applicationmodel.sample.domain.gis;

/**
 * Exception thrown by the {@link GeoService} when an unexpected error occurs.
 */
public class GeoServiceException extends RuntimeException {

    public GeoServiceException() {
    }

    public GeoServiceException(String message) {
        super(message);
    }

    public GeoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
