package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface FavoriteService {
    @PreAuthorize("hasRole('BUYER')")
    void addFavorite(Long propertyId);

    List<Property> getFavoriteProperties();
    void deleteFavorite(Long propertyId);
}
