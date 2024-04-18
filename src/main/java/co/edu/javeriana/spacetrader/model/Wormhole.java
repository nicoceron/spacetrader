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
        this.travelTime = calculateDistance(sourceStar, destinationStar);
    }

    public Wormhole() {

    }

    private double calculateDistance(Star source, Star destination) {
        return Math.sqrt(Math.pow(destination.getX() - source.getX(), 2) +
                Math.pow(destination.getY() - source.getY(), 2) +
                Math.pow(destination.getZ() - source.getZ(), 2));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Star getSourceStar() {
        return sourceStar;
    }

    public void setSourceStar(Star sourceStar) {
        this.sourceStar = sourceStar;
    }

    public Star getDestinationStar() {
        return destinationStar;
    }

    public void setDestinationStar(Star destinationStar) {
        this.destinationStar = destinationStar;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }
}
