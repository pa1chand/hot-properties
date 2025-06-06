package edu.depaul.hot_properties.utils;


import edu.depaul.hot_properties.entities.User;
import org.springframework.security.core.Authentication;

public record CurrentUserContext(User user, Authentication auth) {}
