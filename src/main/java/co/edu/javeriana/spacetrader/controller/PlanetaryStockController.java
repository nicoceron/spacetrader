package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.service.PlanetaryStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/planetary-stock")
public class PlanetaryStockController {

    @Autowired
    private PlanetaryStockService planetaryStockService;

    @GetMapping("/list")
    public String listPlanetaryStocks(Model model) {
        List<PlanetaryStock> planetaryStocks = planetaryStockService.findAllPlanetaryStocks();
        model.addAttribute("planetaryStocks", planetaryStocks);
        return "planetary-stock-list";
    }

    @GetMapping("/detail/{id}")
    public String planetaryStockDetail(@PathVariable Long id, Model model) {
        PlanetaryStock planetaryStock = planetaryStockService.findPlanetaryStockById(id);
        model.addAttribute("planetaryStock", planetaryStock);
        return "planetary-stock-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editPlanetaryStockForm(@PathVariable Long id, Model model) {
        PlanetaryStock planetaryStock = planetaryStockService.findPlanetaryStockById(id);
        model.addAttribute("planetaryStock", planetaryStock);
        return "planetary-stock-edit";
    }

    @PostMapping("/save")
    public String saveOrUpdatePlanetaryStock(@ModelAttribute PlanetaryStock planetaryStock) {
        planetaryStockService.saveOrUpdatePlanetaryStock(planetaryStock);
        return "redirect:/planetary-stock/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePlanetaryStock(@PathVariable Long id) {
        planetaryStockService.deletePlanetaryStock(id);
        return "redirect:/planetary-stock/list";
    }

    @GetMapping("/calculate-selling-price/{id}")
    @ResponseBody
    public double calculateSellingPrice(@PathVariable Long id) {
        return planetaryStockService.calculateSellingPrice(id);
    }

    @GetMapping("/calculate-buying-price/{id}")
    @ResponseBody
    public double calculateBuyingPrice(@PathVariable Long id) {
        return planetaryStockService.calculateBuyingPrice(id);
    }

}
