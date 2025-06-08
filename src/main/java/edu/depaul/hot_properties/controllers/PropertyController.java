package edu.depaul.hot_properties.controllers;


import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.repositories.PropertyRepository;
import edu.depaul.hot_properties.services.PropertyService;
import edu.depaul.hot_properties.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    @Autowired
    public PropertyController(PropertyService propertyService, PropertyRepository propertyRepository, UserService userService) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }
    // === AGENT, ADMIN + ADDING PROPERTY FORM
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping("/add")
    public String showAddProperty(Model model) {
        model.addAttribute("property", new Property());
        return "property";
        }
    // add property

    @PostMapping("/add")
    public String addProperty(@ModelAttribute Property property,
                              @RequestParam(value = "file", required = false) List<MultipartFile> files
                              ) {
        try {
            Property savedProperty = propertyService.addProperty(property);

            // Then, store the profile picture (if uploaded) and update the property
            if (files != null && !files.isEmpty()) {
                propertyService.storeProfilePictures(savedProperty.getId(), files);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "testing";
    }
    @GetMapping("/list")
    public String browseProperties(@RequestParam(required = false) String location, Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);

        // get the user's favorited property ids Set<Long> to render out in the html
        List<Long> favoritedIds = userService.getFavoritedPropertyIdsForCurrentUser();
        model.addAttribute("favoritedIds", favoritedIds);

        return "list";
    }

    @GetMapping("/view/{id}")
    public String viewProperty(@PathVariable Long id, Model model) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("view property controller is not working"));
        model.addAttribute("viewProperty", property);
        return "view";
    }
}

