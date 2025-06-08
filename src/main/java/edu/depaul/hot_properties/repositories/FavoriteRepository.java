package edu.depaul.hot_properties.repositories;

import edu.depaul.hot_properties.entities.Favorite;
import edu.depaul.hot_properties.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByBuyer(User buyer);
}
