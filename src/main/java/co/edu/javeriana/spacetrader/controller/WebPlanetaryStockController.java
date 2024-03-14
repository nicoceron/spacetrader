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
@RequestMapping("/planetary-stock")
public class WebPlanetaryStockController {

    @Autowired
    private PlanetaryStockService planetaryStockService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private ProductService productService;

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
        PlanetaryStock planetaryStock;
        if (id == null || id == 0) {
            planetaryStock = new PlanetaryStock();
        } else {
            planetaryStock = planetaryStockService.findPlanetaryStockById(id);
        }

        List<Planet> planets = planetService.findAllPlanets();
        List<Product> products = productService.findAllProducts();

        model.addAttribute("planetaryStock", planetaryStock);
        model.addAttribute("allPlanets", planets);
        model.addAttribute("allProducts", products);

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
