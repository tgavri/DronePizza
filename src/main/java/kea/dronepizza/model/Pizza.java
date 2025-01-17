package kea.dronepizza.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizza_id;

    private String title;

    @Column(precision = 10, scale = 0)
    private int price; // heltal

    @JsonIgnore //nødvendig ellers er der infinite loop pga referencer mellem delivery, pizza gennem leveringer liste.
    @OneToMany(mappedBy = "pizza")
    private List<Delivery> deliveries;

    public Pizza() {}

    public Pizza(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public Long getPizza_id() { return pizza_id; }
    public void setPizza_id(Long pizza_id) { this.pizza_id = pizza_id; }
    public String getTitle() { return title; }
    public void setTitle(String titel) { this.title = titel; }
    public int getPrice() { return price; }
    public void setPrice(int pris) { this.price = pris; }
    public List<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(List<Delivery> deliveries) { this.deliveries = deliveries; }
}


