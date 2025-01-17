package kea.dronepizza.config;

import kea.dronepizza.model.Delivery;
import kea.dronepizza.model.Drone;
import kea.dronepizza.model.Pizza;
import kea.dronepizza.model.Station;
import kea.dronepizza.repository.DroneRepository;
import kea.dronepizza.repository.DeliveryRepository;
import kea.dronepizza.repository.PizzaRepository;
import kea.dronepizza.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class InitData implements CommandLineRunner {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create and save pizzas
        Pizza pizza1 = new Pizza("Margherita", 69);
        Pizza pizza2 = new Pizza("Pepperoni", 79);
        Pizza pizza3 = new Pizza("Vegetarian", 75);
        Pizza pizza4 = new Pizza("Marco Polo", 85);
        Pizza pizza5 = new Pizza("Quattro Formaggi", 89);
        Pizza pizza6 = new Pizza("Calzone", 65);
        pizzaRepository.saveAll(Arrays.asList(pizza1, pizza2, pizza3, pizza4, pizza5, pizza6));

        // Create and save stations
        Station station1 = new Station(55.676098, 12.568337, "KEA Guldbergsgade");
        Station station2 = new Station(55.673034, 12.557388, "Rigshospitalet");
        Station station3 = new Station(55.681589, 12.575281, "Kartoffelrækkerne");
        stationRepository.saveAll(Arrays.asList(station1, station2, station3));

        // Create and save drones
        Drone drone1 = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.I_DRIFT, station1);
        Drone drone2 = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.I_DRIFT, station1);
        Drone drone3 = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.UDE_AF_DRIFT, station2);
        Drone drone4 = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.UDE_AF_DRIFT, station2);
        Drone drone6 = new Drone(UUID.randomUUID().toString(), Drone.DriftsStatus.UDFASET, station3);
        droneRepository.saveAll(Arrays.asList(drone1, drone2, drone3, drone4, drone6));

        // Create and save leveringer
        Delivery levering1 = new Delivery("Teglholmsgade 39, 2450 København SV", LocalDateTime.now().minusMinutes(30), pizza1);
        Delivery levering2 = new Delivery("Teglværksgade 11, 2100 København Ø", LocalDateTime.now().minusMinutes(40), pizza2);
        Delivery levering3 = new Delivery("Nørrebrogade 180, 2200 København N", LocalDateTime.now().minusMinutes(45), pizza5);
        Delivery levering4 = new Delivery("Gl. Kongevej 44, 2000 Frederiksberg", LocalDateTime.now().minusMinutes(60), pizza4);

        deliveryRepository.saveAll(Arrays.asList(levering1, levering2, levering3, levering4));
    }

}
