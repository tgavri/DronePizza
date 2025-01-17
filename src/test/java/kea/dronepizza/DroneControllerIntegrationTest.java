package kea.dronepizza;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DroneControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetAllDrones_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].droneId", notNullValue()))
                .andExpect(jsonPath("$[0].uuid", notNullValue()))
                .andExpect(jsonPath("$[0].status", notNullValue()))
                .andExpect(jsonPath("$[0].station", notNullValue()))
                .andExpect(jsonPath("$[0].station.id", notNullValue()))
                .andExpect(jsonPath("$[0].station.latitude", notNullValue()))
                .andExpect(jsonPath("$[0].station.longitude", notNullValue()));
    }

    @Test
    public void testAddDrone_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/drones/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.droneId", notNullValue()))
                .andExpect(jsonPath("$.uuid", notNullValue()))
                .andExpect(jsonPath("$.status", is("I_DRIFT")))
                .andExpect(jsonPath("$.station", notNullValue()));
    }

    @Test
    public void testChangeDroneStatus_Enable_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/drones/enable/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.droneId", is(1)))
                .andExpect(jsonPath("$.status", is("I_DRIFT")));
    }

    @Test
    public void testChangeDroneStatus_Disable_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/drones/disable/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.droneId", is(1)))
                .andExpect(jsonPath("$.status", is("UDE_AF_DRIFT")));
    }

    @Test
    public void testChangeDroneStatus_Retire_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/drones/retire/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.droneId", is(1)))
                .andExpect(jsonPath("$.status", is("UDFASET")));
    }
    @Test
    public void testChangeDroneStatus_NonexistentDrone_Failure() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/drones/enable/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
