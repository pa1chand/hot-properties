package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {
    Property addProperty(Property property);

    String storeProfilePictures(Long propertyId, List<MultipartFile> file);

    void updateProperty(Property savedProperty);


    List<Property> getPropertyByAgentId(Long agentId);





}
