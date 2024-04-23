package co.edu.javeriana.spacetrader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class PlanetaryStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "planet_id", nullable = false)
    private Planet planet;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private long stock; // S

    private double demandFactor; // FD

    private double supplyFactor; // FO

    private double SellingPrice; // Calculated in the constructor

    private double BuyingPrice; // Calculated in the constructor

    public PlanetaryStock() {
        this.SellingPrice = this.demandFactor / (1 + this.stock);
        this.BuyingPrice = this.supplyFactor / (1 + this.stock);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public double getDemandFactor() {
        return demandFactor;
    }

    public void setDemandFactor(double demandFactor) {
        this.demandFactor = demandFactor;
    }

    public double getSupplyFactor() {
        return supplyFactor;
    }

    public void setSupplyFactor(double supplyFactor) {
        this.supplyFactor = supplyFactor;
    }

    public double getSellingPrice() {
        return demandFactor / (1 + stock);
    }

    public double getBuyingPrice() {
        return supplyFactor / (1 + stock);
    }
}
