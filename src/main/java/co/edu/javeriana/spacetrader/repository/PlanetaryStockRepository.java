package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetaryStockRepository extends JpaRepository<PlanetaryStock, Long> {
}
