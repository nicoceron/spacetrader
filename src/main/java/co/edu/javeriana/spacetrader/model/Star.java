package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double x;
    private double y;
    private double z;
    private boolean inhabited = false;

    private Wormhole wormhole;

    @OneToMany(mappedBy = "star", cascade = CascadeType.PERSIST)
    private List<Planet> planets = new ArrayList<>();

    public Star() {

    }

    public Star(String name, double x, double y, double z, boolean inhabited) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.inhabited = inhabited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isInhabited() {
        return inhabited;
    }

    public void setInhabited(boolean inhabited) {
        this.inhabited = inhabited;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    // Method to add a planet to the star
    public void addPlanet(Planet planet) {
        planets.add(planet);
        planet.setStar(this);
        this.inhabited = true;
    }

    // Method to remove a planet from the star
    public void removePlanet(Planet planet) {
        planets.remove(planet);
        planet.setStar(null);
    }

    public Wormhole getWormhole() {
        return wormhole;
    }

    public void setWormhole(Wormhole wormhole) {
        this.wormhole = wormhole;
    }
}