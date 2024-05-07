package co.edu.javeriana.spacetrader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "This field is required, please provide a value.")
    private String name;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "This field is required, please provide a value.")
    private String password;

    @Column(name = "role", nullable = false)
    @NotBlank(message = "This field is required, please provide a value.")
    private String role; // Roles: Pilot, Trader, Captain

    @ManyToMany(mappedBy = "crew")
    @JsonIgnore
    private List<Spaceship> spaceships = new ArrayList<>();

    public Player() {
    }

    public Player(String name, String password, String role, Role authRole) {
        this.name = name;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Spaceship> getSpaceships() {
        return spaceships;
    }

    public void setSpaceships(List<Spaceship> spaceships) {
        this.spaceships = spaceships;
    }

    public boolean AddSpaceship(Spaceship spaceship){
        return this.spaceships.add(spaceship);
    }

    public boolean canPilot(){
        return this.role.contains("pilot") || this.role.contains("captain");
    }

    public boolean canTrade(){
        return this.role.contains("pilot") || this.role.contains("captain");
    }
}
