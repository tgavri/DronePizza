package kea.dronepizza.repository;

import kea.dronepizza.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone,Long>{
    List<Drone> findByDriftsstatus(Drone.DriftsStatus status);
    Optional<Drone> findFirstByDriftsstatus(Drone.DriftsStatus driftsstatus);
}
