package kea.dronepizza.repository;

import kea.dronepizza.model.Delivery;

import kea.dronepizza.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Find all deliveries where faktiskLevering is null (undelivered)
    List<Delivery> findByActualDeliveryIsNull();

    // Find all deliveries where drone is null (not assigned to a drone)
    List<Delivery> findByDroneIsNull();

    Optional<Delivery> findByDeliveryId(Long deliveryId);

    int countByDroneAndActualDeliveryIsNotNull(Drone drone);
    int countByDroneAndActualDeliveryIsNull(Drone drone);
}
