package com.erealestate.houserent_sell.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List; // Add this for handling lists of equipment and security features

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDTO {
    private String title;
    private String Location;
    private String description;
    private double price;
    private int bedrooms;
    private int bathrooms;
    private String propertyType;
    private String ownerName;
    private String contactInfo;
    private String buildingType;
    private int floor;
    private int totalFloors;
    private String buildingMaterial;
    private String heating;
    private String availableFrom;
    private boolean rentToStudents;
    private List<String> equipment;
    private List<String> security;

}
