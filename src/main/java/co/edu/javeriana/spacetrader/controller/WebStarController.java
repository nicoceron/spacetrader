package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.service.PlanetService;
import co.edu.javeriana.spacetrader.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import javax.validation.Valid;

@Controller
@RequestMapping("/star")
public class WebStarController {

    @Autowired
    private StarService starService;

    @Autowired
    private PlanetService planetService;

    @GetMapping("/list")
    public String listStars(Model model) {
        List<Star> stars = starService.findAllStars();
        model.addAttribute("stars", stars);
        return "star-list";
    }

    @GetMapping("/detail/{id}")
    public String starDetail(@PathVariable Long id, Model model) {
        Star star = starService.findStarById(id);
        model.addAttribute("star", star);
        return "star-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editStarForm(@PathVariable Long id, Model model) {
        Star star;
        if (id == 0) {
            // ID of 0 indicates a request to add a new Star
            star = new Star(); // Create a new Star object
        } else {
            // Attempt to find an existing Star by ID
            star = starService.findStarById(id);
        }
        model.addAttribute("star", star);
        return "star-edit";
    }


    @PostMapping("/save")
    public String saveOrUpdateStar(@ModelAttribute Star star) {
        starService.saveOrUpdateStar(star);
        return "redirect:/star/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStar(@PathVariable Long id) {
        starService.deleteStar(id);
        return "redirect:/star/list";
    }

    // Add a planet to a star
    @GetMapping("/add-planet/{starId}")
    public String addPlanetForm(@PathVariable Long starId, Model model) {
        Star star = starService.findStarById(starId);
        model.addAttribute("star", star);
        model.addAttribute("planet", new Planet());
        return "star-add-planet";
    }


    @PostMapping("/add-planet/{starId}")
    public String addPlanetToStar(
            @PathVariable Long starId,
            @ModelAttribute("planet") Planet planet) {
        starService.addPlanetToStar(starId, planet);
        return "redirect:/star/detail/" + starId;
   }

}
