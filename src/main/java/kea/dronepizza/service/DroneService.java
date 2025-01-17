package kea.dronepizza.service;

import kea.dronepizza.DTO.DroneDTO;
import kea.dronepizza.DTO.StationDTO;
import kea.dronepizza.model.Drone;
import kea.dronepizza.model.Station;
import kea.dronepizza.repository.DeliveryRepository;
import kea.dronepizza.repository.DroneRepository;
import kea.dronepizza.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class DroneService {
    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<DroneDTO> getAllDrones() {
        List<Drone> drones = droneRepository.findAll();
        logger.info("Found {} drones", drones.size());
        return drones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DroneDTO addDrone() {
        List<Station> stations = stationRepository.findStationsOrderedByDroneCount();
        if (stations.isEmpty()) {
            throw new IllegalStateException("No stations available");
        }

        // Find nr 1 i listen, den der har mindst droner tilknyttet.
        Station station = stations.get(0);

        Drone drone = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.I_DRIFT, station);
        return convertToDTO(droneRepository.save(drone));
    }

    public DroneDTO changeDroneStatus(Long id, Drone.DriftsStatus status) {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drone Id:" + id));
        drone.setDriftsstatus(status);
        return convertToDTO(droneRepository.save(drone));
    }

    private DroneDTO convertToDTO(Drone drone) {
        logger.info("Converting drone with ID: {}", drone.getDrone_id());

        StationDTO stationDTO = null;
        if (drone.getStation() != null) {
            Station station = drone.getStation();
            logger.info("Drone {} has station: {}", drone.getDrone_id(), station);
            logger.info("Station details - ID: {}, Latitude: {}, Longitude: {}",
                    station.getStation_id(), station.getLatitude(), station.getLongitude());

            stationDTO = new StationDTO(
                    station.getStation_id(),
                    station.getLatitude(),
                    station.getLongitude(),
                    station.getName()
            );
            logger.info("Created StationDTO: {}", stationDTO);
        } else {
            logger.warn("Drone {} has no associated station", drone.getDrone_id());
        }

        DroneDTO droneDTO = new DroneDTO(
                drone.getDrone_id(),
                drone.getSerial_uuid(),
                drone.getDriftsstatus(),
                stationDTO
        );

        // Bruges til frontend
        // orders fulfilled
        int ordersFulfilled = deliveryRepository.countByDroneAndActualDeliveryIsNotNull(drone);
        droneDTO.setOrdersFulfilled(ordersFulfilled);
        // current orders
        int currentOrders = deliveryRepository.countByDroneAndActualDeliveryIsNull(drone);
        droneDTO.setCurrentOrders(currentOrders);


        logger.info("Converted DroneDTO: {}", droneDTO);
        return droneDTO;
    }
}
