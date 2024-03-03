package co.edu.javeriana.spacetrader.model;


import jakarta.persistence.*;

@Entity
public class Product {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
@Column(name = "name", nullable = false)
private String name;
@Column(name = "volume", nullable = false)
private double volume;

    public Product() {
    }

    public Product(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
