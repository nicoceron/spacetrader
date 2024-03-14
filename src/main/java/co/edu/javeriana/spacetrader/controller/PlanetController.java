package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planet")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @GetMapping("/list")
    public List<Planet> listPlanets(){
        return planetService.findAllPlanets();
    }
}
