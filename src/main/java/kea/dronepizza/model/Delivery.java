package kea.dronepizza.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    private String address;
    private LocalDateTime expectedDelivery;
    private LocalDateTime actualDelivery;

    @ManyToOne
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone drone;

    public Delivery() {}

    public Delivery(String address, LocalDateTime expectedDelivery, Pizza pizza) {
        this.address = address;
        this.expectedDelivery = expectedDelivery;
        this.pizza = pizza;
        // drone & actual delivery time er null
    }

    public Delivery(Long deliveryId, String address, LocalDateTime expectedDelivery, LocalDateTime actualDelivery, Pizza pizza, Drone drone) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.expectedDelivery = expectedDelivery;
        this.actualDelivery = actualDelivery;
        this.pizza = pizza;
        this.drone = drone;
    }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public String getAddress() { return address; }
    public void setAddress(String adresse) { this.address = adresse; }
    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime forventet_levering) { this.expectedDelivery = forventet_levering; }
    public LocalDateTime getActualDelivery() { return actualDelivery; }
    public void setActualDelivery(LocalDateTime faktisk_levering) { this.actualDelivery = faktisk_levering; }
    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }
    public Drone getDrone() { return drone; }
    public void setDrone(Drone drone) { this.drone = drone; }
}
