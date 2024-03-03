package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Spaceship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank(message = "This field is required, please provide a value.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @Column(name = "credit", nullable = false)
    private BigDecimal credit;

    @ManyToMany
    @JoinTable(name="spaceship_crew", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "spaceship_id"))
    private List<Player> crew = new ArrayList<>();

    @OneToMany(mappedBy = "spaceship", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CargoItem> cargo = new ArrayList<>();

    public Spaceship() {
    }

    public Spaceship(Long id, String name, Model model, BigDecimal credit) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.credit = credit;
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


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

//    Model variables
    public double getStorage(){
        return model.getStorage();
    }

    public double getVelocity(){
        return model.getVelocity();
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public List<Player> getCrew() {
        return crew;
    }

    public void setCrew(List<Player> crew) {
        this.crew = crew;
    }

    public List<CargoItem> getCargo() {
        return cargo;
    }

    public void setCargo(List<CargoItem> cargo) {
        this.cargo = cargo;
    }


    public void addCrewMember(Player player) {
        crew.add(player);
        player.getSpaceships().add(this);
    }

    public void removeCrewMember(Player player) {
        crew.remove(player);
        player.getSpaceships().remove(this);
    }

    public void addCargoItem(CargoItem item) {
        cargo.add(item);
        item.setSpaceship(this);
    }

    public void removeCargoItem(CargoItem item) {
        cargo.remove(item);
        item.setSpaceship(null);
    }


}

