package kea.dronepizza.DTO;

public class StationDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public StationDTO(Long id, double latitude, double longitude, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}