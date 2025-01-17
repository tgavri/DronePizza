package kea.dronepizza.DTO;

import jakarta.persistence.*;
import kea.dronepizza.model.Drone;
import kea.dronepizza.model.Pizza;

import java.time.LocalDateTime;

public class DeliveryDTO {
    private Long deliveryId;
    private String adresse;
    private LocalDateTime expectedDelivery;
    private LocalDateTime actualDelivery;
    private Long pizzaId;
    private String pizzaName;
    private Long droneId;

    public DeliveryDTO() {}
    public DeliveryDTO(Long deliveryId, String adresse, LocalDateTime expectedDelivery,
                       LocalDateTime actualDelivery, Long pizzaId, String pizzaName, Long droneId) {
        this.deliveryId = deliveryId;
        this.adresse = adresse;
        this.expectedDelivery = expectedDelivery;
        this.actualDelivery = actualDelivery;
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
        this.droneId = droneId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDateTime expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public LocalDateTime getActualDelivery() {
        return actualDelivery;
    }

    public void setActualDelivery(LocalDateTime actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }
}

