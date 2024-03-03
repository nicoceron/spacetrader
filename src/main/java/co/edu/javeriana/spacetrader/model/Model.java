package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Model name is required")
    private String modelName;
    @Column(name = "velocity", nullable = false)
    private double velocity;
    @Column(name = "storage", nullable = false)
    private double storage;


    public Model() {
    }

    public Model(String modelName, double velocity, double storage) {
        this.modelName = modelName;
        this.velocity = velocity;
        this.storage = storage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getStorage() {
        return storage;
    }

    public void setStorage(double storage) {
        this.storage = storage;
    }
}
