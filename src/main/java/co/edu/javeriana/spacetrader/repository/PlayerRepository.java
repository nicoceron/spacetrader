package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Player p SET p.name = :Name WHERE p.id = :id")
    int updatePlayerName(@Param("id") Long id, @Param("Name") String Name);

    Page<Player> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);
}
