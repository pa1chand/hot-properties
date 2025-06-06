package edu.depaul.hot_properties.services;


import edu.depaul.hot_properties.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    @PreAuthorize("isAuthenticated()")
    void prepareDashboardModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareProfileModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareSettingsModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void updateUserSettings(User updatedUser, String password, List<Long> addIds, List<Long> removeIds);

    @PreAuthorize("hasRole('ADMIN')")
    List<User> getAllUsers();

    @PreAuthorize("hasRole('MANAGER')")
    List<User> getTeamForCurrentManager();

    String storeProfilePicture(Long userId, MultipartFile file);

    User registerNewUser(User user, List<String> roleNames);

    void updateUser(User savedUser);

    @PreAuthorize("isAuthenticated()")
    User getCurrentUser();
}
