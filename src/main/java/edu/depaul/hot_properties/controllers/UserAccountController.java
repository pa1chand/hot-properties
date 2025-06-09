package edu.depaul.hot_properties.controllers;


import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.entities.PropertyImage;
import edu.depaul.hot_properties.entities.User;
import edu.depaul.hot_properties.services.AuthService;
import edu.depaul.hot_properties.services.FileStorageService;
import edu.depaul.hot_properties.services.PropertyService;
import edu.depaul.hot_properties.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class UserAccountController {

    private final AuthService authService;
    private final UserService userService;
    private final PropertyService propertyService;

    @Autowired
    private FileStorageService fileStorageService;


    public UserAccountController(AuthService authService, UserService userService, PropertyService propertyService) {
        this.authService = authService;
        this.userService = userService;
        this.propertyService = propertyService;
    }
    // 🫡app version
    @Value("${foo.app.version}")
    private String applicationVersion;

    @ModelAttribute("applicationVersion")
    public String getApplicationVersion() {
        return applicationVersion;
    }
    @GetMapping({"/", "/index"})
    public String showIndex() {
        return "index";
    }

    // === LOGIN ===
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user,
                               HttpServletResponse response,
                               Model model) {
        try {
            Cookie jwtCookie = authService.loginAndCreateJwtCookie(user);
            response.addCookie(jwtCookie);
            return "redirect:/dashboard";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String logout(HttpServletResponse response) {
        authService.clearJwtCookie(response);
        return "redirect:/login";
    }

    // === DASHBOARD / PROFILE / SETTINGS ===
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showDashboard(Model model) {
        userService.prepareDashboardModel(model);
        System.out.println("from dashboard controller");
        return "dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showProfile(Model model) {
        userService.prepareProfileModel(model);
        return "profile";
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showSettings(Model model) {
        userService.prepareSettingsModel(model);
        return "account_settings";
    }

    @PostMapping("/settings")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String updateSettings(@ModelAttribute("user") User updatedUser,
                                        @RequestParam(required = false) String password,
                                        @RequestParam(required = false) List<Long> addIds,
                                        @RequestParam(required = false) List<Long> removeIds,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Look up the real user so we get the correct ID
            User actualUser = userService.getCurrentUser();

            // Copy updates from form-bound user
            actualUser.setFirstName(updatedUser.getFirstName());
            actualUser.setLastName(updatedUser.getLastName());
            actualUser.setEmail(updatedUser.getEmail());

            userService.updateUserSettings(actualUser, password, addIds, removeIds);

            // Save profile picture if provided
            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(actualUser.getId(), file);
                actualUser.setProfilePicture(filename);
                userService.updateUser(actualUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update account: " + ex.getMessage());
        }
        return "redirect:/settings";
    }

    // === ADMIN + MANAGER VIEWS ===
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all_users";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/createagent")
    public String showCreateAgentForm(Model model) {
        model.addAttribute("user", new User() );
        return "createagent";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/createagent")
    public String createAgent(@ModelAttribute("user") User user,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        try {
            // First, register the user (this will assign them an ID)
            List<String> roleNames = List.of("ROLE_AGENT");
            User savedUser = userService.registerNewUser(user, roleNames);

            // Then, store the profile picture (if uploaded) and update the user record
            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(savedUser.getId(), file);
                savedUser.setProfilePicture(filename);
                // Save again to persist the filename
                userService.updateUser(savedUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Agent created successfully.");
            return "redirect:/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    //not working will finish after I sync to get properties code


    // === AGENT, ADMIN + ADDING PROPERTY FORM
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping("/property/add")
    public String showAddProperty(Model model) {
        model.addAttribute("property", new Property());
        return "propertyadd";
    }
    // add property
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @PostMapping("/property/add")
    public String addProperty(@ModelAttribute Property property,
                              @RequestParam("imageFiles") MultipartFile[] imageFiles,
                              RedirectAttributes redirectAttributes) {
        try {
            // attach user
            User currentUser = userService.getCurrentUser();
            property.setUser(currentUser);


            Property savedProperty = propertyService.addProperty(property, imageFiles);
            redirectAttributes.addFlashAttribute("message", "Property '" + savedProperty.getTitle() + "' added successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");


        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error adding property: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");

        }

        return "redirect:/property/add";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/admin")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manage_users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/users/admin";
    }


    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/property/managelistings")
    public String manageListing(Model model) {
        User currentUser = userService.getCurrentUser();
        // Ensure propertyService.getPropertyByAgentId returns a list of properties for the current user
        List<Property> agentProperties = propertyService.getPropertyByAgentId(currentUser.getId());
        model.addAttribute("properties", agentProperties);
        return "manage-properties";
    }
    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/property/edit/{id}")
    public String editProperty(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Property property = propertyService.getPropertyById(id);
        if (property == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Property not found.");
            return "redirect:/property/managelistings";
        }

        User currentUser = userService.getCurrentUser();
        if (!property.getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this property.");
            return "redirect:/property/managelistings";
        }

        model.addAttribute("property", property);
        return "propertyadd";
    }
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/property/edit/{id}")
    public String updateProperty(@PathVariable Long id,
                                 @ModelAttribute Property updatedProperty,
                                 @RequestParam("imageFiles") MultipartFile[] imageFiles,
                                 RedirectAttributes redirectAttributes) {
        try {
            Property existingProperty = propertyService.getPropertyById(id);
            if (existingProperty == null) {
                redirectAttributes.addFlashAttribute("message", "Property not found.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/property/managelistings";
            }

            User currentUser = userService.getCurrentUser();
            if (!existingProperty.getUser().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("message", "Unauthorized update attempt.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/property/managelistings";
            }

            // Update basic fields
            existingProperty.setTitle(updatedProperty.getTitle());
            existingProperty.setPrice(updatedProperty.getPrice());
            existingProperty.setLocation(updatedProperty.getLocation());
            existingProperty.setSize(updatedProperty.getSize());

            // Optional: Handle new image uploads
            if (imageFiles != null && imageFiles.length > 0) {
                propertyService.updatePropertyImages(existingProperty, imageFiles); // create this method in service
            }

            propertyService.updateProperty(existingProperty);

            redirectAttributes.addFlashAttribute("message", "Property updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating property: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/property/managelistings";
    }
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/property/delete/{id}")
    public String deleteProperty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propertyService.deleteProperty(id);
            redirectAttributes.addFlashAttribute("message", "Property deleted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting property: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/property/managelistings";
    }


    // === REGISTRATION ===
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        try {
            // First, register the user (this will assign them an ID)
            List<String> roleNames = List.of("ROLE_BUYER");
            User savedUser = userService.registerNewUser(user, roleNames);

            // Then, store the profile picture (if uploaded) and update the user record
            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(savedUser.getId(), file);
                savedUser.setProfilePicture(filename);
                // Save again to persist the filename
                userService.updateUser(savedUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }



    // === PROFILE PICTURE UPLOAD ===
    @PostMapping("/users/{id}/upload-profile-picture")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String uploadProfilePicture(@PathVariable Long id,
                                       @RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        try {
            String filename = userService.storeProfilePicture(id, file);
            redirectAttributes.addFlashAttribute("message", "Profile picture uploaded: " + filename);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile-pictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveProfilePicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/profile-pictures/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
