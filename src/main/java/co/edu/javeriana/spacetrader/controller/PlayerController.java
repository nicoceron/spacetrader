package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @GetMapping("list-page")
    public Page<Player> listPlayers(Pageable pageable){
        return playerService.listPlayersPageable(pageable);
    }
    @GetMapping("/search")
    public Page<Player> searchPlayer(@RequestParam String name, Pageable pageable){
        return playerService.searchPlayer(name, pageable);
    }
    @PostMapping("")
    public Player createPlayer(@RequestBody Player player){
        return playerService.saveOrUpdatePlayer(player);
    }
    @DeleteMapping("/{idPlayer")
    public void deletePlayer(@PathVariable Long idPlayer){
        playerService.deletePlayer(idPlayer);
    }

    @PatchMapping("{idPlayer}/name")
    public Map<String, Object> modifyName(@PathVariable Long idPlayer, @RequestBody String newName){
        int numberOfModifiedRegisters = playerService.updateNamePlayer(idPlayer, newName);
        Map<String, Object> response = new HashMap<>();
        response.put("quantityOfModifiedRows", numberOfModifiedRegisters);
        return response;
    }
}
