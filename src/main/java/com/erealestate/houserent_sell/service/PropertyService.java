package com.erealestate.houserent_sell.service;

import java.util.List;
import java.util.Optional;

import com.erealestate.houserent_sell.model.Property;

public interface PropertyService {

    Property createProperty(Property property);

    List<Property> getAllProperties();

    Optional<Property> getPropertyById(Long id);

    void deleteProperty(Long id);

    // Add the search method to the service interface
    List<Property> searchProperties(String location, double priceFrom, double priceTo, double surfaceFrom,
            double surfaceTo);
}
