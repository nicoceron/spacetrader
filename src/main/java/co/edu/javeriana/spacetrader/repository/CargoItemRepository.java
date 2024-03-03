package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.CargoItem;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoItemRepository extends JpaRepository<CargoItem, Long> {
}
