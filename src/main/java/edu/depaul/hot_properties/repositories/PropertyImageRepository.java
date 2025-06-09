package edu.depaul.hot_properties.repositories;

import edu.depaul.hot_properties.entities.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    void deleteAllByPropertyId(Long propertyId);

    // Optional: Fetch images by property if needed elsewhere
    List<PropertyImage> findAllByPropertyId(Long propertyId);
}
