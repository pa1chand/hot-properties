package edu.depaul.hot_properties.controllers;


import edu.depaul.hot_properties.entities.Property;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("property")
public class PropertyController {

    // === AGENT, ADMIN + ADDING PROPERTY FORM
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping("/add")
    public String showAddProperty(Model model) {
        model.addAttribute("property", new Property());
        return "property";
        }
    // add property

    @PostMapping("/add")
    public String addProperty(@ModelAttribute("property") Property property,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              RedirectAttributes redirectAttributes
                              ) {
        try {
            Property property = propertyService.save
        }
    }
    }




}
