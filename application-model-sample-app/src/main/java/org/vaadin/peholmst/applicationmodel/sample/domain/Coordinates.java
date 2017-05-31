package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.io.Serializable;

/**
 * TODO Document me
 */
public class Coordinates implements Serializable {

    private final double latitude;
    private final double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Coordinates that = (Coordinates) o;

        if (Double.compare(that.latitude, latitude) != 0)
            return false;
        return Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     *
     * @param point
     * @return http://www.mapanet.eu/EN/Resources/Script_Distance.htm
     */
    public double distanceToPointInMeters(Coordinates point) {
        final double latA = latitude * Math.PI / 180.0;
        final double lngA = longitude * Math.PI / 180.0;
        final double latB = point.latitude * Math.PI / 180.0;
        final double lngB = point.longitude * Math.PI / 180.0;
        return 6378137
                * Math.acos(Math.cos(latA) * Math.cos(latB) * Math.cos(lngB - lngA) + Math.sin(latA) * Math.sin(latB));
    }
}
