package kea.dronepizza.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drone_id;

    @Column(unique = true, nullable = false)
    private String serial_uuid;

    @Enumerated(EnumType.STRING)
    private DriftsStatus driftsstatus;

    @OneToMany(mappedBy = "drone")
    private List<Delivery> deliveries;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public enum DriftsStatus {
        I_DRIFT, UDE_AF_DRIFT, UDFASET
    }
    public Drone() {}

    public Drone(String serial_uuid, DriftsStatus driftsstatus, Station station) {
        this.serial_uuid = serial_uuid;
        this.driftsstatus = driftsstatus;
        this.station = station;
    }

    public Long getDrone_id() { return drone_id; }
    public void setDrone_id(Long drone_id) { this.drone_id = drone_id; }
    public String getSerial_uuid() { return serial_uuid; }
    public void setSerial_uuid(String serial_uuid) { this.serial_uuid = serial_uuid; }
    public DriftsStatus getDriftsstatus() { return driftsstatus; }
    public void setDriftsstatus(DriftsStatus driftsstatus) { this.driftsstatus = driftsstatus; }
    public List<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(List<Delivery> deliveries) { this.deliveries = deliveries; }
    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }
}
