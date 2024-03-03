package co.edu.javeriana.spacetrader.model;

import jakarta.persistence.*;

@Entity
public class Wormhole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "source_star_id", nullable = false)
    private Star sourceStar;
    @ManyToOne
    @JoinColumn(name = "destination_star_id", nullable = false)
    private Star destinationStar;

    @Column(name ="travel_time", nullable = false)
    private double travelTime;

    public Wormhole(Star sourceStar, Star destinationStar) {
        this.sourceStar = sourceStar;
        this.destinationStar = destinationStar;
    }

}
