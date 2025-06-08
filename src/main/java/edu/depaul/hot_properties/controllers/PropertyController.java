package edu.depaul.hot_properties.controllers;


import edu.depaul.hot_properties.services.AuthService;
import edu.depaul.hot_properties.services.PropertyService;
import edu.depaul.hot_properties.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
//@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public PropertyController(PropertyService propertyService, UserService userService, AuthService authService) {
        this.propertyService = propertyService;
        this.userService = userService;
        this.authService = authService;
    }
//    // === AGENT, ADMIN + ADDING PROPERTY FORM
//    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
//    @GetMapping("/add")
//    public String showAddProperty(Model model) {
//        model.addAttribute("property", new Property());
//        return "property";
//        }
//    // add property
//
//    @PostMapping("/add")
//    public String addProperty(@ModelAttribute Property property,
//                              @RequestParam(value = "file", required = false) List<MultipartFile> files,
//                              RedirectAttributes redirectAttributes
//                              ) {
//        try {
//            Property savedProperty = propertyService.addProperty(property);
//
//            // Then, store the profile picture (if uploaded) and update the property
//            if (files != null && !files.isEmpty()) {
//                propertyService.storeProfilePictures(savedProperty.getId(), files);
//            }
//        } catch (Exception e) {
//
//        }
//        return "testing";
//    }
//    @PreAuthorize("hasRole('AGENT')")
//    @GetMapping("/managelistings")
//    public String manageListing(Model model) {
//        User currentUser = userService.getCurrentUser();
//        //Property property = propertyService.
//        model.addAttribute("listings", propertyService.getPropertyByAgentId(currentUser.getId()));
//
//        return "my_team";
//    }
}

