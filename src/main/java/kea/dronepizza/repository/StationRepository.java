package kea.dronepizza.repository;

import kea.dronepizza.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s LEFT JOIN s.drones d GROUP BY s ORDER BY COUNT(d) ASC")
    List<Station> findStationsOrderedByDroneCount();
}
