package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.CargoItem;
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
public class SpaceshipService {

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    private final PlayerRepository playerRepository;

    public SpaceshipService(SpaceshipRepository spaceshipRepository, PlayerRepository playerRepository) {
        this.spaceshipRepository = spaceshipRepository;
        this.playerRepository = playerRepository;
    }


    // Retrieve all spaceships
    public List<Spaceship> findAllSpaceships() {
        return spaceshipRepository.findAll();
    }

    // Find a spaceship by ID
    public Spaceship findSpaceshipById(Long id) {
        return spaceshipRepository.findById(id).orElseThrow(() -> new RuntimeException("Spaceship not found for this id :: " + id));
    }

    // Save or update a spaceship
    public Spaceship saveOrUpdateSpaceship(Spaceship spaceship) {
        return spaceshipRepository.save(spaceship);
    }

    // Delete a spaceship by ID
    @Transactional
    public void deleteSpaceship(Long id) {
        Spaceship spaceship = findSpaceshipById(id); // Ensures the spaceship exists before attempting to delete
        spaceshipRepository.delete(spaceship);
    }

    @Transactional
    public void assignPlayerToSpaceship(Long playerId, Long spaceshipId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
                .orElseThrow(() -> new EntityNotFoundException("Spaceship not found with id: " + spaceshipId));

        // Associate player with spaceship
        spaceship.addCrewMember(player);
        player.getSpaceships().add(spaceship);

        // Save both player and spaceship in one transaction
        playerRepository.save(player);
        spaceshipRepository.save(spaceship);
    }

//    @Transactional
//    public void loadCargoToSpaceship(Long cargoItemId, Long spaceshipId) {
//        CargoItem cargoItem = CargoItemRepository.findById(cargoItemId)
//                .orElseThrow(() -> new EntityNotFoundException("Cargo item not found with id: " + cargoItemId));
//
//        Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
//                .orElseThrow(() -> new EntityNotFoundException("Spaceship not found with id: " + spaceshipId));
//
//        // Use the addCargoItem method to load cargo to the spaceship
//        spaceship.addCargoItem(cargoItem);
//
//        // Save the spaceship (cargo item will be cascaded automatically due to the relationship)
//        spaceshipRepository.save(spaceship);
//    }

}
