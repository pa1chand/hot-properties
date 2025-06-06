package edu.depaul.hot_properties.services;

import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.exceptions.InvalidPropertyParameterException;
import edu.depaul.hot_properties.repositories.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    @Override
    public Property addProperty(Property property) {
        if (propertyRepository.existsById(property.getId())) {
            throw new InvalidPropertyParameterException("property id exists already");
        }

        return null;
    }

    // to store image then use update method it in the controller
    @Override
    public String storeProfilePicture(Long propertyId, MultipartFile file) {
        try {
           String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

           // Resolve absolute path relative to the project directory
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profile-pictures");
            Files.createDirectories(uploadPath);

            // Locate property and remove previous image (if any)
            Property property = propertyRepository.findById(propertyId).orElseThrow();

            if (property.get)
        }
    }

}
