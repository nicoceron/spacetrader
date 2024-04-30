package co.edu.javeriana.spacetrader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    @OneToMany(mappedBy = "sourceStar")
    private List<Wormhole> outgoingWormholes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "destinationStar")
    private List<Wormhole> incomingWormholes = new ArrayList<>();

    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Star star = (Star) o;
        return Double.compare(x, star.x) == 0 && Double.compare(y, star.y) == 0 && Double.compare(z, star.z) == 0 && inhabited == star.inhabited && Objects.equals(id, star.id) && Objects.equals(name, star.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, x, y, z, inhabited);
    }

    public List<Wormhole> getOutgoingWormholes() {
        return outgoingWormholes;
    }

    public void setOutgoingWormholes(List<Wormhole> outgoingWormholes) {
        this.outgoingWormholes = outgoingWormholes;
    }

    public List<Wormhole> getIncomingWormholes() {
        return incomingWormholes;
    }

    public void setIncomingWormholes(List<Wormhole> incomingWormholes) {
        this.incomingWormholes = incomingWormholes;
    }
}