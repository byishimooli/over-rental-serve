package com.erealestate.houserent_sell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erealestate.houserent_sell.model.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByLocationContainingAndPriceBetweenAndSurfaceBetween(
            String location, double minPrice, double maxPrice, double minSurface, double maxSurface);
}
