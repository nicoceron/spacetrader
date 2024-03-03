package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceshipRepository  extends JpaRepository<Spaceship, Long> {
}
