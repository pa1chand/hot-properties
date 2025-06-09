package edu.depaul.hot_properties.services;

import edu.depaul.hot_properties.entities.Favorite;
import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.entities.User;
import edu.depaul.hot_properties.exceptions.InvalidUserParameterException;
import edu.depaul.hot_properties.repositories.FavoriteRepository;
import edu.depaul.hot_properties.repositories.PropertyRepository;
import edu.depaul.hot_properties.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    /*
    Many-to-one: the user who listed the property.
    One-to-many: images of the property.
    Many-to-many: users who have favorited this property.

    favorite.setBuyer(User user);
    favorite.setProperty(Property property) set property.images

     */
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(UserRepository userRepository, PropertyRepository propertyRepository, UserService userService, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void addFavorite(Long propertyId) {
        User user =  userService.getCurrentUser();
        if (user == null) {throw new InvalidUserParameterException("User is null");
        }
        // will get property id from the properties/list
        Property property = propertyRepository.findById(propertyId).orElseThrow();

        Favorite favorite = new Favorite();
        favorite.setBuyer(user);
        favorite.setProperty(property);
        favoriteRepository.save(favorite);

    }

    @Override
    public List<Property> getFavoriteProperties() {

        User user = userService.getCurrentUser();
        // get user all favorite , favorite also have properties
        List<Favorite> favorites = favoriteRepository.findByBuyer(user);

        List<Property> properties = new ArrayList<>();
        for (Favorite favorite : favorites) {
            properties.add(favorite.getProperty());
        }
        return properties;

    }

    @Override
    public void deleteFavorite(Long propertyId) {
        User user = userService.getCurrentUser();

        List<Favorite> favorites = favoriteRepository.findByBuyer(user);
        // favorite property id = property id
        for (Favorite favorite : favorites) {
            if (Objects.equals(favorite.getProperty().getId(), propertyId)) {
                favoriteRepository.delete(favorite);
            }
        }
    }


}
