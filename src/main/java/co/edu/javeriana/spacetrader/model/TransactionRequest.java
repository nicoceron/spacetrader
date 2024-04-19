package co.edu.javeriana.spacetrader.model;

public class TransactionRequest {
    private Long spaceshipId;
    private Long planetaryStockId;
    private int quantity;

    // Getters and Setters
    public Long getSpaceshipId() {
        return spaceshipId;
    }

    public void setSpaceshipId(Long spaceshipId) {
        this.spaceshipId = spaceshipId;
    }

    public Long getPlanetaryStockId() {
        return planetaryStockId;
    }

    public void setPlanetaryStockId(Long planetaryStockId) {
        this.planetaryStockId = planetaryStockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}