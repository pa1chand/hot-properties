package edu.depaul.hot_properties.repositories;

import edu.depaul.hot_properties.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByUser_Id(Long id);

