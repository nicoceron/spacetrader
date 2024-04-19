package co.edu.javeriana.spacetrader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "This field is required, please provide a value.")
    String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "star_id", nullable = false)
    private Star star;

    @JsonIgnore
    @OneToMany(mappedBy = "planet", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<PlanetaryStock> productsAvailable = new ArrayList<>();

    public Planet() {
    }

    public Planet(String name, Star star) {
        this.name = name;
        this.star = star;
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

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public List<PlanetaryStock> getProductsAvailable() {
        return productsAvailable;
    }

    public void setProductsAvailable(List<PlanetaryStock> productsAvailable) {
        this.productsAvailable = productsAvailable;
    }
}
