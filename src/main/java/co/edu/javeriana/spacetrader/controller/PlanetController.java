package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.service.PlanetService;
import co.edu.javeriana.spacetrader.service.PlanetaryStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/planet")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetaryStockService planetaryStockService;

    @GetMapping("/list")
    public String listPlanets(Model model) {
        List<Planet> planets = planetService.findAllPlanets();
        model.addAttribute("planets", planets);
        return "planet-list";
    }

    @GetMapping("/detail/{id}")
    public String planetDetail(@PathVariable Long id, Model model) {
        Planet planet = planetService.findPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editPlanetForm(@PathVariable Long id, Model model) {
        Planet planet = planetService.findPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-edit";
    }

    @PostMapping("/save")
    public String saveOrUpdatePlanet(@ModelAttribute Planet planet) {
        planetService.saveOrUpdatePlanet(planet);
        return "redirect:/planet/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePlanet(@PathVariable Long id) {
        planetService.deletePlanet(id);
        return "redirect:/planet/list";
    }

    @GetMapping("/add-stock/{planetId}")
    public String addPlanetaryStockForm(@PathVariable Long planetId, Model model) {
        Planet planet = planetService.findPlanetById(planetId);
        model.addAttribute("planet", planet);
        return "planet-add-stock";
    }

    @PostMapping("/add-stock/{planetId}")
    public String addPlanetaryStockToPlanet(@PathVariable Long planetId, @ModelAttribute PlanetaryStock planetaryStock) {
        planetService.addPlanetaryStockToPlanet(planetId, planetaryStock);
        return "redirect:/planet/detail/" + planetId;
    }

}
