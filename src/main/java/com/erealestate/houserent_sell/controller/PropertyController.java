package com.erealestate.houserent_sell.controller;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erealestate.houserent_sell.dto.PropertyDTO;
import com.erealestate.houserent_sell.model.Property;
import com.erealestate.houserent_sell.service.PropertyService;

@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for the specified origin
@RestController // Use RestController for API endpoints
@RequestMapping("/api/properties") // Adjusting the mapping for API calls
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // API endpoint to get all properties in JSON format
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties(); // This will automatically be converted to JSON
    }

    // Show properties on the home page
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "home"; // Ensure there is a home.html in the templates directory
    }

    // Show a specific property by ID
    @GetMapping("/{id}")
    public String getPropertyById(@PathVariable Long id, Model model) {
        Optional<Property> property = propertyService.getPropertyById(id);
        property.ifPresent(p -> model.addAttribute("property", p)); // Use ifPresent to add property to the model
        return "property-details"; // Ensure there's a property-details.html template
    }

    // Show the property posting page
    @GetMapping("/post")
    public String showPostingPage() {
        return "post"; // Ensure there's a post.html template for posting properties
    }

    // API endpoint to post a new property
    @PostMapping("/upload")
    public ResponseEntity<String> postProperty(@ModelAttribute PropertyDTO propertyDTO,
            @RequestParam("file") MultipartFile file) {
        try {
            // Define where to save the file (you may need to modify the file path as per
            // your needs)
            String filePath = "path/to/your/uploads/directory/" + file.getOriginalFilename();

            // Save the file to the specified location
            file.transferTo(new File(filePath));

            // Create a new Property object
            Property property = new Property();
            property.setTitle(propertyDTO.getTitle());
            property.setPrice(propertyDTO.getPrice());
            property.setOwnerName(propertyDTO.getOwnerName());
            property.setContactInfo(propertyDTO.getContactInfo());
            property.setBuildingType(propertyDTO.getBuildingType());
            property.setFloor(propertyDTO.getFloor());
            property.setTotalFloors(propertyDTO.getTotalFloors());
            property.setBuildingMaterial(propertyDTO.getBuildingMaterial());
            property.setHeating(propertyDTO.getHeating());
            property.setAvailableFrom(propertyDTO.getAvailableFrom());
            property.setRentToStudents(propertyDTO.isRentToStudents()); // Corrected this line
            property.setEquipment(propertyDTO.getEquipment()); // List of equipment
            property.setSecurity(propertyDTO.getSecurity()); // List of security features
            property.setDescription(propertyDTO.getDescription());
            property.setLocation(propertyDTO.getLocation());
            property.setFilePath(filePath); // Save the file path in the Property object

            // Save the property to the database
            propertyService.createProperty(property);

            return ResponseEntity.ok("Property posted successfully"); // Return success response
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to post property");
        }
    }

    @GetMapping("/search")
    public List<Property> searchProperties(
            @RequestParam String location,
            @RequestParam double priceFrom,
            @RequestParam double priceTo,
            @RequestParam double surfaceFrom,
            @RequestParam double surfaceTo) {
        return propertyService.searchProperties(location, priceFrom, priceTo, surfaceFrom, surfaceTo);
    }
}
