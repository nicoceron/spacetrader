package co.edu.javeriana.spacetrader.repository;

import co.edu.javeriana.spacetrader.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
