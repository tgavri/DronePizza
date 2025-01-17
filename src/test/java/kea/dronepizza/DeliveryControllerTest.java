package kea.dronepizza;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.dronepizza.DTO.DeliveryDTO;
import kea.dronepizza.controller.DeliveryController;
import kea.dronepizza.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(DeliveryController.class)
public class DeliveryControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;


    private DeliveryDTO testDelivery;

    @BeforeEach
    void setUp() {
        testDelivery = new DeliveryDTO(1L, "Rådhuspladsen 1", LocalDateTime.now().plusMinutes(30),
                null, null, null, null);
    }

    @Test
    void testGetUndeliveredDeliveries() throws Exception {
        List<DeliveryDTO> deliveries = Arrays.asList(testDelivery);
        when(deliveryService.getUndeliveredDeliveries()).thenReturn(deliveries);

        mockMvc.perform(get("/deliveries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].deliveryId").value(1))
                .andExpect(jsonPath("$[0].adresse").value("Rådhuspladsen 1"));
    }

    @Test
    void testAddDelivery() throws Exception {
        DeliveryDTO pepDelivery = new DeliveryDTO(1L, "Test Address", LocalDateTime.now(),
                null, 3L, "Pepperoni", null);
        when(deliveryService.addDelivery(any(Long.class))).thenReturn(pepDelivery);

        mockMvc.perform(post("/deliveries/add")
                        .param("pizzaId", "3") // 3 = Pepperonni <3
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.pizzaId").value(3));
    }

    @Test
    void testScheduleDelivery() throws Exception {
        DeliveryDTO scheduledDelivery = new DeliveryDTO(1L, "Rådhuspladsen 1", LocalDateTime.now().plusMinutes(30),
                null, 1L, "Margherita", 1L);
        when(deliveryService.scheduleDelivery(1L, 1L)).thenReturn(scheduledDelivery);

        mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "1")
                        .param("droneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.droneId").value(1));
    }

    @Test
    void testScheduleDeliveryWithoutDroneId() throws Exception {
        DeliveryDTO scheduledDelivery = new DeliveryDTO(1L, "Rådhuspladsen 1", LocalDateTime.now().plusMinutes(30),
                null, 1L, "Margherita", 2L);
        when(deliveryService.scheduleDelivery(1L, null)).thenReturn(scheduledDelivery);

        mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.droneId").value(2));
    }

    @Test
    void testScheduleDeliveryNotFound() throws Exception {
        when(deliveryService.scheduleDelivery(99L, 1L)).thenThrow(new IllegalArgumentException("Delivery not found"));

        mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "99")
                        .param("droneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testScheduleDeliveryAlreadyAssigned() throws Exception {
        when(deliveryService.scheduleDelivery(1L, 1L)).thenThrow(new IllegalStateException("Delivery already assigned to a drone"));

        mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "1")
                        .param("droneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Delivery already assigned to a drone"));
    }

    @Test
    void testFinishDelivery() throws Exception {
        DeliveryDTO finishedDelivery = new DeliveryDTO(1L, "Rådhuspladsen 1", LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now(), 1L, "Margherita", 1L);
        when(deliveryService.finishDelivery(1L)).thenReturn(finishedDelivery);

        mockMvc.perform(post("/deliveries/finish")
                        .param("deliveryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.actualDelivery").isNotEmpty());
    }

    @Test
    void testFinishDeliveryNotAssigned() throws Exception {
        when(deliveryService.finishDelivery(1L)).thenThrow(new IllegalStateException("Delivery not assigned to a drone"));

        mockMvc.perform(post("/deliveries/finish")
                        .param("deliveryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Delivery not assigned to a drone"));
    }
    @Test
    void testScheduleDeliveryWithNegativeId() throws Exception {
        MvcResult result = mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "-1")
                        .param("droneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid delivery ID")))
                .andReturn();

        System.out.println("Response Status: " + result.getResponse().getStatus());
        System.out.println("Response Content: " + result.getResponse().getContentAsString());
    }
    @Test
    void testScheduleDeliveryWithNegativeDroneId() throws Exception {
        when(deliveryService.scheduleDelivery(1L, -1L)).thenThrow(new IllegalArgumentException("Invalid drone ID"));

        mockMvc.perform(post("/deliveries/schedule")
                        .param("deliveryId", "1")
                        .param("droneId", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid drone ID"));
    }
    @Test
    void testAddDeliveryWithInvalidPizzaId() throws Exception {
        when(deliveryService.addDelivery(-1L)).thenThrow(new IllegalArgumentException("Invalid pizza ID"));

        mockMvc.perform(post("/deliveries/add")
                        .param("pizzaId", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid pizza ID")));
    }

    @Test
    public void testSimulateOrder() throws Exception {
        DeliveryDTO mockDelivery = new DeliveryDTO();
        mockDelivery.setDeliveryId(1L);
        mockDelivery.setAdresse("123 Test St");
        mockDelivery.setPizzaName("Margherita");
        when(deliveryService.simulateOrder()).thenReturn(mockDelivery);

        mockMvc.perform(post("/deliveries/simulate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.adresse").value("123 Test St"))
                .andExpect(jsonPath("$.pizzaName").value("Margherita"));
    }
    @Test
    public void testSimulateOrderFailure() throws Exception {
        when(deliveryService.simulateOrder()).thenThrow(new RuntimeException("Simulation failed"));

        mockMvc.perform(post("/deliveries/simulate"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Failed to simulate order")));
    }
}
