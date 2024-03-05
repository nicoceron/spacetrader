package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.repository.PlayerRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    // Find all players
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    // Find a player by ID
    public Player findPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found for this id :: " + id));
    }

    // Save or update a player
    public Player saveOrUpdatePlayer(Player player) {
        return playerRepository.save(player);
    }

    // Delete a player by ID
    @Transactional
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id " + id));

        // Remove the player from each spaceship's crew list
        for (Spaceship spaceship : player.getSpaceships()) {
            spaceship.getCrew().remove(player);
            // Save the updated spaceship entity
            spaceshipRepository.save(spaceship);
        }
        playerRepository.delete(player);
    }

}
