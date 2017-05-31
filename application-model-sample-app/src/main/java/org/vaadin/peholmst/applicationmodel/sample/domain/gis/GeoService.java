package org.vaadin.peholmst.applicationmodel.sample.domain.gis;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.vaadin.peholmst.applicationmodel.sample.domain.Coordinates;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.*;

/**
 * TODO Document me
 */
public class GeoService {

    private final GeoApiContext geoApiContext;
    private final String country;

    public GeoService(GeoApiContext geoApiContext, String country) {
        this.geoApiContext = Objects.requireNonNull(geoApiContext);
        this.country = Objects.requireNonNull(country);
    }

    public List<NamedPlace> findPlaces(String searchTerm) {
        if (searchTerm.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            AutocompletePrediction[] results = PlacesApi.placeAutocomplete(geoApiContext, searchTerm)
                    .type(PlaceAutocompleteType.GEOCODE).components(ComponentFilter.country(country)).await();
            return Arrays.stream(results)
                    .map(ap -> new NamedPlace(ap.description, ap.placeId,
                            Arrays.stream(ap.terms).map(t -> t.value).collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new GeoServiceException("Error finding place names", ex);
        }
    }

    public Optional<Pair<Coordinates, Accuracy>> findCoordinates(NamedPlace place) {
        try {
            GeocodingResult[] results = GeocodingApi.newRequest(geoApiContext).place(place.getGooglePlaceId()).await();
            if (results.length > 0) {
                GeocodingResult result = results[0];
                return Optional.of(Pair.of(new Coordinates(result.geometry.location.lat, result.geometry.location.lng),
                        toAccuracy(result.types)));
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            throw new GeoServiceException("Error finding coordinates", ex);
        }
    }

    public Optional<NamedPlace> findPlace(Coordinates coordinates) {
        try {
            GeocodingResult[] results = GeocodingApi
                    .reverseGeocode(geoApiContext, new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .await();
            if (results.length > 0) {
                GeocodingResult result = results[0];
                return Optional.of(new NamedPlace(result.formattedAddress, result.placeId,
                        Arrays.stream(result.addressComponents).map(ac -> ac.longName).collect(Collectors.toList())));
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            throw new GeoServiceException("Error finding address", ex);
        }
    }

    private static Accuracy toAccuracy(AddressType[] addressTypes) {
        Set<AddressType> addressTypeSet = new HashSet<>(Arrays.asList(addressTypes));
        System.out.println(addressTypeSet);
        if (addressTypeSet.contains(AddressType.STREET_ADDRESS)) {
            return Accuracy.ADDRESS;
        } else if (addressTypeSet.contains(AddressType.ROUTE)) {
            return Accuracy.ROAD;
        } else {
            return Accuracy.CITY;
        }
    }
}
