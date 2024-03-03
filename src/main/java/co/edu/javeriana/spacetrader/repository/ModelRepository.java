package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {
}
