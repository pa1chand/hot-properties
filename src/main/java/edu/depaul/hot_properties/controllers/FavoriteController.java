package edu.depaul.hot_properties.controllers;

import edu.depaul.hot_properties.entities.Property;
import edu.depaul.hot_properties.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {



    private final FavoriteService favoriteService;;


    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/add")
    public String addFavorite(@RequestParam Long propertyId) {
        favoriteService.addFavorite(propertyId);
        return "redirect:/properties/list";
    }


    @GetMapping("/favorites")
    public String favorites(Model model) {
        // get current user
        List<Property> properties = favoriteService.getFavoriteProperties();
        model.addAttribute("properties", properties);
        return "favorites";
    }

    @PostMapping("/remove")
    public String removeFavorite(@RequestParam Long propertyId) {
        favoriteService.deleteFavorite(propertyId);
        return "redirect:/properties/list";
    }

    @PostMapping("/remove/favorites")
    public String removeFavorite1(@RequestParam Long propertyId) {
        favoriteService.deleteFavorite(propertyId);
        return "redirect:/favorites/favorites";
    }




}
