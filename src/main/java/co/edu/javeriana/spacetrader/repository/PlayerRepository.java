package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
