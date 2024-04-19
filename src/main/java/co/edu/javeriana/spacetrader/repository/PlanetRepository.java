package co.edu.javeriana.spacetrader.repository;
import java.util.List;


import co.edu.javeriana.spacetrader.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
