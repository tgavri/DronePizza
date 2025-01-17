package kea.dronepizza.controller;

import kea.dronepizza.DTO.DroneDTO;
import kea.dronepizza.model.Drone;
import kea.dronepizza.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")

public class DroneController {
    @Autowired
    private DroneService droneService;

    @GetMapping
    public List<DroneDTO> getAllDrones() {
        return droneService.getAllDrones();
    }

    @PostMapping("/add")
    public ResponseEntity<DroneDTO> addDrone() {
        try {
            return ResponseEntity.ok(droneService.addDrone());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/enable/{id}")
    public ResponseEntity<DroneDTO> enableDrone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(droneService.changeDroneStatus(id, Drone.DriftsStatus.I_DRIFT));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/disable/{id}")
    public ResponseEntity<DroneDTO> disableDrone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(droneService.changeDroneStatus(id, Drone.DriftsStatus.UDE_AF_DRIFT));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/retire/{id}")
    public ResponseEntity<DroneDTO> retireDrone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(droneService.changeDroneStatus(id, Drone.DriftsStatus.UDFASET));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}