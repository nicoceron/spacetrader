package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Product;
import co.edu.javeriana.spacetrader.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Retrieve all products
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // Find a product by ID
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found for this id :: " + id));
    }

    // Save or update a product
    public Product saveOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete a product by ID
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductById(id); // Ensures the product exists before attempting to delete
        productRepository.delete(product);
    }

}
