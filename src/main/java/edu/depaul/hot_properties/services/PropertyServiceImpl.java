package edu.depaul.hot_properties.services;

import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.entities.PropertyImage;
import edu.depaul.hot_properties.entities.User;
import edu.depaul.hot_properties.repositories.PropertyImageRepository;
import edu.depaul.hot_properties.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertyImageRepository propertyImageRepository;
    @Value("${upload.dir}")
    private String uploadDir;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UserService userService,PropertyImageRepository propertyImageRepository) {

        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.propertyImageRepository = propertyImageRepository;

    }
    @Override
    public Property addProperty(Property property, MultipartFile[] imageFiles) {
        // Many-to-one: the user who listed the property.
        User currentUser = userService.getCurrentUser();
        property.setUser(currentUser);
        Property savedProperty = propertyRepository.save(property);
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (imageFiles != null && imageFiles.length > 0) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    // Generate a unique file name to avoid conflicts
                    String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
                    String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
                    Path filePath = uploadPath.resolve(fileName);

                    // Save the file to the file system
                    try {
                        Files.copy(file.getInputStream(), filePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create an Image entity and associate it with the property
                    PropertyImage image = new PropertyImage();
                    image.setFilePath("/uploads/" + fileName); // Store a relative path/URL
                    savedProperty.addImage(image); // Use helper to maintain bidirectional link
                }
            }
        }

        return propertyRepository.save(savedProperty);
    }

    @Transactional
    public List<Property> getPropertyByAgentId(Long agentId) {
        User currentUser = userService.getCurrentUser();
        return propertyRepository.findByUser_Id (currentUser.getId());
    }




    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }
    public void updateProperty(Property property) {
        propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void updatePropertyImages(Property property, MultipartFile[] imageFiles) {
        // Step 1: Delete old images from filesystem and database
        List<PropertyImage> existingImages = property.getImages();
        if (existingImages != null && !existingImages.isEmpty()) {
            for (PropertyImage image : existingImages) {
                Path imagePath = Paths.get(uploadDir).resolve(image.getFilePath().replace("/uploads/", ""));
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    System.err.println("Failed to delete old image: " + imagePath + " - " + e.getMessage());
                }
            }
            propertyImageRepository.deleteAll(existingImages);
            property.getImages().clear();
        }

        // Step 2: Upload and add new images
        if (imageFiles != null && imageFiles.length > 0) {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException("Could not create upload dir: " + uploadDir, e);
                }
            }

            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
                    String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
                    Path targetPath = uploadPath.resolve(uniqueFilename);

                    try {
                        Files.copy(file.getInputStream(), targetPath);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save new image: " + uniqueFilename, e);
                    }

                    PropertyImage newImage = new PropertyImage();
                    newImage.setFilePath("/uploads/" + uniqueFilename);
                    newImage.setProperty(property);
                    property.addImage(newImage);
                }
            }
        }

        propertyRepository.save(property);
    }
}
