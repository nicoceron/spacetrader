package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Wormhole;
import co.edu.javeriana.spacetrader.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WormholeRepository extends JpaRepository<Wormhole, Long> {

}
