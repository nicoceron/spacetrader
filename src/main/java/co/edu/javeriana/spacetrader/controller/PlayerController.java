package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

//    Use json Ignore and separate requesting
    @GetMapping("/list")
    public List<Player> listPlayers(){
        return playerService.findAllPlayers();
    }

    @GetMapping("/{idPlayer}")
    public Player findPlayer(@PathVariable Long idPlayer){
        return playerService.findPlayerById(idPlayer);
    }

}
