package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Wormhole;
import co.edu.javeriana.spacetrader.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WormholeRepository extends JpaRepository<Wormhole, Long> {
    // Find all wormholes with a given star as the source
    List<Wormhole> findBySourceStar(Star sourceStar);

    // Find a specific wormhole by its source and destination stars
    Optional<Wormhole> findBySourceStarAndDestinationStar(Star sourceStar, Star destinationStar);
}
