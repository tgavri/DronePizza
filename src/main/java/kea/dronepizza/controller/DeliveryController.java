package kea.dronepizza.controller;

import kea.dronepizza.DTO.DeliveryDTO;
import kea.dronepizza.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getUndeliveredDeliveries() {
        List<DeliveryDTO> undeliveredDeliveries = deliveryService.getUndeliveredDeliveries();
        return ResponseEntity.ok(undeliveredDeliveries);
    }

    // bruges til frontend udelukkende.
    @GetMapping("/all")
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        List<DeliveryDTO> allDeliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(allDeliveries);
    }
    // bruges til frontend udelukkende.
    @PostMapping("/simulate")
    public ResponseEntity<?> simulateOrder() {
        try {
            DeliveryDTO newDelivery = deliveryService.simulateOrder();
            return ResponseEntity.ok(newDelivery);
        } catch (Exception e) {
            //alternativ
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failed to simulate order: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to simulate order: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDelivery(@RequestParam Long pizzaId) {
        try {
            DeliveryDTO newDelivery = deliveryService.addDelivery(pizzaId);
            return ResponseEntity.ok(newDelivery);
        } catch (IllegalArgumentException e) {
            //alternativ
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid pizza ID: " + pizzaId);
            return ResponseEntity.badRequest().body("Invalid pizza ID: " + pizzaId);
        }
    }

    @GetMapping("/queue")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesWithoutDrones() {
        List<DeliveryDTO> queuedDeliveries = deliveryService.getDeliveriesWithoutDrones();
        return ResponseEntity.ok(queuedDeliveries);
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleDelivery(@RequestParam Long deliveryId, @RequestParam(required = false) Long droneId) {
        try {
            DeliveryDTO scheduledDelivery = deliveryService.scheduleDelivery(deliveryId, droneId);
            if (scheduledDelivery == null) {
                //alternativ
                //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid delivery ID: " + deliveryId;
                return ResponseEntity.badRequest().body("Invalid delivery ID: " + deliveryId);
            }
            return ResponseEntity.ok(scheduledDelivery);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                //alternativ
                //throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid delivery ID: " + deliveryId;
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishDelivery(@RequestParam Long deliveryId) {
        try {
            DeliveryDTO finishedDelivery = deliveryService.finishDelivery(deliveryId);
            return ResponseEntity.ok(finishedDelivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
