package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/list")
    public String listPlayers(Model model) {
        List<Player> players = playerService.findAllPlayers();
        model.addAttribute("players", players);
        return "player-list";
    }

    @GetMapping("/detail/{id}")
    public String playerDetail(@PathVariable Long id, Model model) {
        Player player = playerService.findPlayerById(id);
        model.addAttribute("player", player);
        return "player-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editPlayerForm(@PathVariable Long id, Model model) {
        Player player;
        if (id == 0) {
            player = new Player(); // Create a new Player object
        } else {
            player = playerService.findPlayerById(id);
        }
        model.addAttribute("player", player);
        return "player-edit";
    }


    @PostMapping("/save")
    public String saveOrUpdatePlayer(@ModelAttribute Player player) {
        playerService.saveOrUpdatePlayer(player);
        return "redirect:/player/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return "redirect:/player/list";
    }

}
