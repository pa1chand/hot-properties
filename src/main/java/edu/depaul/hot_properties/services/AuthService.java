package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.dtos.JwtResponse;
import edu.depaul.hot_properties.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthService {
    JwtResponse authenticateAndGenerateToken(User user);

    public Cookie loginAndCreateJwtCookie(User user) throws BadCredentialsException;

    void clearJwtCookie(HttpServletResponse response);

}
