package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.model.Wormhole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WormholeRepository {
    Wormhole[] findBySourceStar(Star currentStar);

    Optional<Object> findBySourceStarAndDestinationStar(Star source, Star destination);
}
