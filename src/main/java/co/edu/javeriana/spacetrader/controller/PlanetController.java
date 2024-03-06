package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.model.Product;
import co.edu.javeriana.spacetrader.service.PlanetService;
import co.edu.javeriana.spacetrader.service.PlanetaryStockService;
import co.edu.javeriana.spacetrader.service.ProductService;
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

    @Autowired
    private ProductService productService;

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
    public String editPlanetForm(@PathVariable long id, Model model) {
        Planet planet;
        if (id == 0) {
            planet = new Planet(); // Create a new Planet object
        } else {
            planet = planetService.findPlanetById(id);
            if (planet == null) {
                planet = new Planet(); // In case no planet is found
            }
        }
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

    //ALREADY BEING ADDED IN PLANETARY-STOCK CONTROLLER

//    @GetMapping("/add-stock/{planetId}")
//    public String addPlanetaryStockForm(@PathVariable Long planetId, Model model) {
//        Planet planet = planetService.findPlanetById(planetId);
//        List<Product> products = productService.findAllProducts();
//
//        PlanetaryStock newStock = new PlanetaryStock();
//        newStock.setPlanet(planet);
//
//        model.addAttribute("planet", planet);
//        model.addAttribute("newStock", newStock);
//        model.addAttribute("allProducts", products);
//
//        return "planet-add-stock";
//    }
//
//    @PostMapping("/add-stock/{planetId}")
//    public String addPlanetaryStockToPlanet(
//            @PathVariable Long planetId,
//            @ModelAttribute PlanetaryStock planetaryStock) {
//        planetService.addPlanetaryStockToPlanet(planetId, planetaryStock);
//        return "redirect:/planet/detail/" + planetId;
//    }
}

