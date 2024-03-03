package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {
}
