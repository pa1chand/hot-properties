package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {
    public Property addProperty(Property property, MultipartFile[] imageFiles);




    public List<Property> getAllProperties();




    public Property getPropertyById(Long id);
    public void deleteProperty(Long id);
    public void updateProperty(Property property);
    public void updatePropertyImages(Property property, MultipartFile[] imageFiles);
    List<Property> getPropertyByAgentId(Long agentId);
}
