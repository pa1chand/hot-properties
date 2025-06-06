package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.web.multipart.MultipartFile;

public interface PropertyService {
    Property addProperty(Property property);

    String storeProfilePicture(Long propertyId, MultipartFile file);

    void updateProperty(Property savedProperty);

}
