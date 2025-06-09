package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface FavoriteService {
    @PreAuthorize("hasRole('BUYER')")
    void addFavorite(Long propertyId);

    List<Property> getFavoriteProperties();

    @PreAuthorize("hasAnyRole('AGENT','ADMIN', 'BUYER')")
    void deleteFavorite(Long propertyId);
}
