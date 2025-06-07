package edu.depaul.hot_properties.controllers;


import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
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
                              @RequestParam(value = "file", required = false) List<MultipartFile> files,
                              RedirectAttributes redirectAttributes
                              ) {
        try {
            Property savedProperty = propertyService.addProperty(property);

            // Then, store the profile picture (if uploaded) and update the property
            if (files != null && !files.isEmpty()) {
                propertyService.storeProfilePictures(savedProperty.getId(), files);
            }
        } catch (Exception e) {

        }
        return "testing";
    }





}
