package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/star")
public class StarController {
    @Autowired
    private StarService starService;

    @GetMapping("/list")
    public List<Star> listStarts(){ return starService.findAllStars();}

    @GetMapping("/{idStar}")
    public Star findStar(@PathVariable Long idStar){
        return starService.findStarById(idStar);
    }
}
