package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
import co.edu.javeriana.spacetrader.service.ModelService;
import co.edu.javeriana.spacetrader.service.SpaceshipService;
import co.edu.javeriana.spacetrader.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spaceship")
public class SpaceshipController {

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ModelService modelService;

    @GetMapping("/list")
    public String listSpaceships(ModelMap model) {
        List<Spaceship> spaceships = spaceshipService.findAllSpaceships();
        model.addAttribute("spaceships", spaceships);
        return "spaceship-list";
    }

    @GetMapping("/detail/{id}")
    public String spaceshipDetail(@PathVariable Long id, ModelMap model) {
        Spaceship spaceship = spaceshipService.findSpaceshipById(id);
        model.addAttribute("spaceship", spaceship);
        return "spaceship-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editSpaceshipForm(@PathVariable Long id, ModelMap model) {
        Spaceship spaceship;
        if (id == 0) {
            // ID of 0 indicates a request to add a new Spaceship
            spaceship = new Spaceship();
        } else {
            spaceship = spaceshipService.findSpaceshipById(id);
        }
        model.addAttribute("spaceship", spaceship);
        model.addAttribute("models", modelService.findAllModels());
        return "spaceship-edit";
    }


    @PostMapping("/save")
    public String saveOrUpdateSpaceship(@ModelAttribute Spaceship spaceship, BindingResult result, ModelMap modelMap) {
        Model model = modelService.findModelByID(spaceship.getModel().getId());
        spaceship.setModel(model);

        spaceshipService.saveOrUpdateSpaceship(spaceship);
        return "redirect:/spaceship/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSpaceship(@PathVariable Long id) {
        spaceshipService.deleteSpaceship(id);
        return "redirect:/spaceship/list";
    }

    @GetMapping("/assign-player/{spaceshipId}")
    public String showAssignPlayerForm(@PathVariable Long spaceshipId, ModelMap model) {
        Spaceship spaceship = spaceshipService.findSpaceshipById(spaceshipId);
        List<Player> allPlayers = playerService.findAllPlayers();
        List<Player> availablePlayers = allPlayers.stream()
                .filter(player -> !spaceship.getCrew().contains(player)) // Exclude already assigned players
                .collect(Collectors.toList());
        model.addAttribute("spaceship", spaceship);
        model.addAttribute("availablePlayers", availablePlayers); // Provide the filtered list
        return "spaceship-add-player";
    }

    @GetMapping("/assign-player/{spaceshipId}/to/{playerId}")
    public String assignPlayerToSpaceship(
            @PathVariable Long spaceshipId,
            @PathVariable Long playerId,
            RedirectAttributes redirectAttributes) {
        spaceshipService.assignPlayerToSpaceship(playerId, spaceshipId);
        redirectAttributes.addAttribute("id", spaceshipId);
        return "redirect:/spaceship/detail/{id}";
    }


}
