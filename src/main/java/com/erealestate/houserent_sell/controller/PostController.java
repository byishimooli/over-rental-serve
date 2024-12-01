package com.erealestate.houserent_sell.controller;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.erealestate.houserent_sell.dto.PropertyDTO;
import com.erealestate.houserent_sell.model.Property;
import com.erealestate.houserent_sell.service.PropertyService;

@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for the specified origin
@RestController
@RequestMapping("/api/property") // Adjusting the mapping for API calls
public class PostController {

    private final PropertyService propertyService;

    @Autowired
    public PostController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // Method to create a property using JSON data
    @PostMapping("/post")
    public ResponseEntity<Property> createProperty(@RequestBody PropertyDTO propertyDTO) {
        try {
            // Create a new Property object from PropertyDTO
            Property property = new Property();
            property.setTitle(propertyDTO.getTitle());
            property.setLocation(propertyDTO.getLocation()); // Use correct field
            property.setDescription(propertyDTO.getDescription());
            property.setPrice(propertyDTO.getPrice());
            property.setBuildingType(propertyDTO.getBuildingType());
            property.setOwnerName(propertyDTO.getOwnerName());
            property.setContactInfo(propertyDTO.getContactInfo());
            property.setBuildingType(propertyDTO.getBuildingType());
            property.setFloor(propertyDTO.getFloor());
            property.setTotalFloors(propertyDTO.getTotalFloors());
            property.setBuildingMaterial(propertyDTO.getBuildingMaterial());
            property.setHeating(propertyDTO.getHeating());
            property.setAvailableFrom(propertyDTO.getAvailableFrom());
            property.setRentToStudents(propertyDTO.isRentToStudents());
            property.setEquipment(propertyDTO.getEquipment()); // List of equipment
            property.setSecurity(propertyDTO.getSecurity()); // List of security features

            // Save the property to the database
            Property savedProperty = propertyService.createProperty(property);
            return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to create a property with a file upload
    @PostMapping("/postWithFile")
    public ResponseEntity<String> createPropertyWithFile(
            @RequestParam("title") String title,
            @RequestParam("location") String location, // Corrected to use location
            @RequestParam("price") double price,
            @RequestParam("bedrooms") int bedrooms,
            @RequestParam("bathrooms") int bathrooms,
            @RequestParam("propertyType") String propertyType,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {

        // Check if the file is empty
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        // Validate file type (optional)
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
            return new ResponseEntity<>("Unsupported file type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        try {
            // Define where to save the file (consider using UUID for unique file names)
            String filePath = "path/to/your/uploads/directory/" + System.currentTimeMillis() + "_"
                    + file.getOriginalFilename();

            // Save the file to the specified location
            file.transferTo(new File(filePath));

            // Create a new Property object from the form data
            Property property = new Property();
            property.setTitle(title);
            property.setLocation(location); // Corrected to set location
            property.setPrice(price);
            property.setBuildingType(contentType);
            property.setDescription(description);
            property.setFilePath(filePath); // Save the file path in the Property object

            // Save the property to the database
            Property savedProperty = propertyService.createProperty(property);

            return new ResponseEntity<>("Property posted successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace for debugging
            return new ResponseEntity<>("Failed to post property", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
