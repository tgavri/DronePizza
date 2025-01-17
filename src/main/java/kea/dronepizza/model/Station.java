package kea.dronepizza.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long station_id;
    private double latitude;
    private double longitude;
    private String name;
    @OneToMany(mappedBy = "station")
    private List<Drone> drones;
    public Station() {}

    public Station(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public Long getStation_id() { return station_id; }
    public void setStation_id(Long station_id) { this.station_id = station_id; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public List<Drone> getDrones() { return drones; }
    public void setDrones(List<Drone> drones) { this.drones = drones; }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}

