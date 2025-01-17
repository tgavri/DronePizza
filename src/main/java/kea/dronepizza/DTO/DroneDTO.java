package kea.dronepizza.DTO;

import kea.dronepizza.model.Drone;

public class DroneDTO {
    private Long droneId;
    private String uuid;
    private Drone.DriftsStatus status;
    private StationDTO station;
    private int ordersFulfilled;
    private int currentOrders;


    public DroneDTO(Long droneId, String uuid, Drone.DriftsStatus status, StationDTO station) {
        this.droneId = droneId;
        this.uuid = uuid;
        this.status = status;
        this.station = station;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Drone.DriftsStatus getStatus() {
        return status;
    }

    public void setStatus(Drone.DriftsStatus status) {
        this.status = status;
    }

    public StationDTO getStation() {
        return station;
    }

    public void setStation(StationDTO station) {
        this.station = station;
    }
    @Override
    public String toString() {
        return "DroneDTO{" +
                "id=" + droneId +
                ", uuid='" + uuid + '\'' +
                ", status=" + status +
                ", station=" + station +
                '}';
    }

    public int getOrdersFulfilled() {
        return ordersFulfilled;
    }

    public void setOrdersFulfilled(int ordersFulfilled) {
        this.ordersFulfilled = ordersFulfilled;
    }

    public int getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(int currentOrders) {
        this.currentOrders = currentOrders;
    }
}