package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.service.SpaceshipService;
import co.edu.javeriana.spacetrader.service.PlayerService; // Assuming PlayerService exists
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/spaceship")
public class SpaceshipController {

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/list")
    public String listSpaceships(Model model) {
        List<Spaceship> spaceships = spaceshipService.findAllSpaceships();
        model.addAttribute("spaceships", spaceships);
        return "spaceship-list";
    }

    @GetMapping("/detail/{id}")
    public String spaceshipDetail(@PathVariable Long id, Model model) {
        Spaceship spaceship = spaceshipService.findSpaceshipById(id);
        model.addAttribute("spaceship", spaceship);
        return "spaceship-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editSpaceshipForm(@PathVariable Long id, Model model) {
        Spaceship spaceship ;
        Star star;
        if (id == 0) {
            // ID of 0 indicates a request to add a new Spaceship
            spaceship = new Spaceship();
        } else {
            spaceship = spaceshipService.findSpaceshipById(id);
        }
        model.addAttribute("spaceship", spaceship);
        return "spaceship-edit";
    }

    @PostMapping("/save")
    public String saveOrUpdateSpaceship(@ModelAttribute Spaceship spaceship) {
        spaceshipService.saveOrUpdateSpaceship(spaceship);
        return "redirect:/spaceship/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSpaceship(@PathVariable Long id) {
        spaceshipService.deleteSpaceship(id);
        return "redirect:/spaceship/list";
    }

    @GetMapping("/assign-player/{spaceshipId}/{playerId}")
    public String assignPlayerToSpaceship(@PathVariable Long spaceshipId, @PathVariable Long playerId, Model model) {
        try {
            spaceshipService.assignPlayerToSpaceship(playerId, spaceshipId);
            return "redirect:/spaceship/detail/" + spaceshipId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

}
