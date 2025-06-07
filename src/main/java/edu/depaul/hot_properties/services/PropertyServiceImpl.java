package edu.depaul.hot_properties.services;

import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.entities.PropertyImage;
import edu.depaul.hot_properties.entities.User;
import edu.depaul.hot_properties.repositories.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UserService userService) {

        this.propertyRepository = propertyRepository;
        this.userService = userService;;

    }
    @Override
    public Property addProperty(Property property) {
        // Many-to-one: the user who listed the property.
        User currentUser = userService.getCurrentUser();
        property.setUser(currentUser);

        propertyRepository.save(property);

        return property;
    }

    @Override
    public String storeProfilePictures(Long propertyId, List<MultipartFile> files) {

        try{
            // find property
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("storeProfilePictures in PropertyServiceImpl is not working"));
            // Resolve absolute path relative to the project directory
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profile-pictures");
            Files.createDirectories(uploadPath);

            // locate and remove previous image (if any) skipped no needed, will keep adding this

            // avoid image overlapped
            for (MultipartFile file : files) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                file.transferTo(filePath.toFile());
                // save image to property images.add(propertyImage); using addImage method
                PropertyImage image = new PropertyImage();
                image.setImageFileName(filename);
                property.addImage(image);
            }
            propertyRepository.save(property);
            return "many files uploaded";
        } catch (IOException ex) {
            System.out.println("Failed to save file: " + ex.getMessage());
            throw new RuntimeException("Failed to store profile picture", ex);
        }
    }


    @Override
    public void updateProperty(Property savedProperty) {

    }

}
