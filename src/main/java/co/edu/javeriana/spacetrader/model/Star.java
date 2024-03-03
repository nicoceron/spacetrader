package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double x;
    private double y;
    private double z;
    private boolean inhabited;

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

    public double getxCoordinate() {
        return x;
    }

    public void setxCoordinate(double xCoordinate) {
        this.x = xCoordinate;
    }

    public double getyCoordinate() {
        return y;
    }

    public void setyCoordinate(double yCoordinate) {
        this.y = yCoordinate;
    }

    public double getzCoordinate() {
        return z;
    }

    public void setzCoordinate(double zCoordinate) {
        this.z = zCoordinate;
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
    }

    // Method to remove a planet from the star
    public void removePlanet(Planet planet) {
        planets.remove(planet);
        planet.setStar(null);
    }
}