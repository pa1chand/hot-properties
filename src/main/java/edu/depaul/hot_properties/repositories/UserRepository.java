package edu.depaul.hot_properties.repositories;

import edu.depaul.hot_properties.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByManager(User manager);

    List<User> findByManagerIsNull();

    List<User> findAllByOrderByLastNameAsc();
}
