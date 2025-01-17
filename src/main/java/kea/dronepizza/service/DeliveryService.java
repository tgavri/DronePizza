package kea.dronepizza.service;

import kea.dronepizza.DTO.DeliveryDTO;
import kea.dronepizza.controller.DeliveryController;
import kea.dronepizza.model.Delivery;
import kea.dronepizza.model.Drone;
import kea.dronepizza.model.Pizza;
import kea.dronepizza.repository.DroneRepository;
import kea.dronepizza.repository.DeliveryRepository;
import kea.dronepizza.repository.PizzaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DroneRepository droneRepository;

    public List<DeliveryDTO> getUndeliveredDeliveries() {
        return deliveryRepository.findByActualDeliveryIsNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DeliveryDTO> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public DeliveryDTO addDelivery(Long pizzaId) {
        if (pizzaId <= 0) {
            throw new IllegalArgumentException("Invalid pizza ID");
        }
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pizza ID: " + pizzaId));

        Delivery newDelivery = new Delivery();
        newDelivery.setPizza(pizza);
        newDelivery.setExpectedDelivery(LocalDateTime.now().plusMinutes(30));

        return convertToDTO(deliveryRepository.save(newDelivery));
    }

    public List<DeliveryDTO> getDeliveriesWithoutDrones() {
        return deliveryRepository.findByDroneIsNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeliveryDTO scheduleDelivery(Long deliveryId, Long droneId) {
        logger.info("Scheduling delivery: deliveryId={}, droneId={}", deliveryId, droneId);

        if (deliveryId == null || deliveryId <= 0) {
            logger.error("Invalid delivery ID: {}", deliveryId);
            throw new IllegalArgumentException("Invalid delivery ID");
        }
        if (droneId != null && droneId <= 0) {
            throw new IllegalArgumentException("Invalid drone ID");
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));

        Drone drone;
        if (droneId != null) {
            drone = droneRepository.findById(droneId)
                    .orElseThrow(() -> new IllegalArgumentException("Drone not found"));
        } else {
            // valg af drone med færreste orders
            List<Drone> availableDrones = droneRepository.findByDriftsstatus(Drone.DriftsStatus.I_DRIFT);
            if (availableDrones.isEmpty()) {
                throw new IllegalStateException("No available drones");
            }

            drone = availableDrones.stream()
                    .min(Comparator.comparingInt(d -> countCurrentOrders(d)))
                    .orElseThrow(() -> new IllegalStateException("Error selecting drone"));
        }

        if (delivery.getDrone() != null) {
            throw new IllegalStateException("Delivery already assigned to a drone");
        }

        if (drone.getDriftsstatus() != Drone.DriftsStatus.I_DRIFT) {
            throw new IllegalStateException("Drone is not in service");
        }

        delivery.setDrone(drone);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return convertToDTO(updatedDelivery);
    }

    private int countCurrentOrders(Drone drone) {
        return deliveryRepository.countByDroneAndActualDeliveryIsNull(drone);
    }


    public DeliveryDTO finishDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with id: " + deliveryId));

        if (delivery.getDrone() == null) {
            throw new IllegalStateException("Delivery not assigned to a drone for: " + deliveryId);
        }

        if (delivery.getActualDelivery() != null) {
            throw new IllegalStateException("Delivery already completed for: " + deliveryId);
        }

        delivery.setActualDelivery(LocalDateTime.now());
        return convertToDTO(deliveryRepository.save(delivery));
    }

    private Drone getRandomAvailableDrone() {
        List<Drone> availableDrones = droneRepository.findByDriftsstatus(Drone.DriftsStatus.I_DRIFT);
        if (availableDrones.isEmpty()) {
            throw new IllegalStateException("Drone not available");
        }
        return availableDrones.get(new Random().nextInt(availableDrones.size()));
    }

    public DeliveryDTO convertToDTO(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getDeliveryId(),
                delivery.getAddress(),
                delivery.getExpectedDelivery(),
                delivery.getActualDelivery(),
                delivery.getPizza() != null ? delivery.getPizza().getPizza_id() : null,
                delivery.getPizza() != null ? delivery.getPizza().getTitle() : null,
                delivery.getDrone() != null ? delivery.getDrone().getDrone_id() : null
        );
    }
    public DeliveryDTO simulateOrder() {
        // random pizza
        List<Pizza> pizzas = pizzaRepository.findAll();
        Pizza randomPizza = pizzas.get(new Random().nextInt(pizzas.size()));

        // ny delivery
        Delivery newDelivery = new Delivery();
        newDelivery.setPizza(randomPizza);
        newDelivery.setAddress(generateRandomAddress());
        newDelivery.setExpectedDelivery(LocalDateTime.now().plusMinutes(30));

        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        return convertToDTO(savedDelivery);
    }

    private String generateRandomAddress() {
        String[] streets = {"Absalonsgade", "Vestergade", "H.C. Andersens Blvd. ", "Alsgade", "Vester Voldgade"};
        String[] cities = {"1171 København K", "1765 København V", "1658 København V", "1790 København V",  "1149 København K"};
        int houseNumber = new Random().nextInt(100) + 1;
        String street = streets[new Random().nextInt(streets.length)];
        String city = cities[new Random().nextInt(cities.length)];
        return street + " " + houseNumber + ", " + city;
    }


}
