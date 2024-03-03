package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;

@Entity
public class CargoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name ="spaceship_id", nullable = false)
    private Spaceship spaceship;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name="quantity", nullable = false)
    private int quantity;

    public CargoItem(Spaceship spaceship, Product product, int quantity) {
        this.spaceship = spaceship;
        this.product = product;
        this.quantity = quantity;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public void setSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
