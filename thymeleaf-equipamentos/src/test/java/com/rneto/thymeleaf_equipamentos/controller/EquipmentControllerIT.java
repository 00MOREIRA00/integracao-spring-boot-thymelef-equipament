package com.rneto.thymeleaf_equipamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rneto.thymeleaf_equipamentos.model.Equipment;
import com.rneto.thymeleaf_equipamentos.model.EquipmentStatus;
import com.rneto.thymeleaf_equipamentos.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EquipmentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipmentRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void get_equipments_deve_retornar_200_com_lista_vazia() throws Exception {
        mockMvc.perform(get("/api/equipments"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void post_equipment_deve_retornar_201_com_body() throws Exception {
        Map<String, String> body = Map.of(
                "name", "Notebook Dell",
                "category", "notebook",
                "serialNumber", "SN-001",
                "status", "DISPONIVEL"
        );

        mockMvc.perform(post("/api/equipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.serialNumber").value("SN-001"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void post_equipment_deve_retornar_409_quando_serial_duplicado() throws Exception {
        repository.save(Equipment.builder()
                .name("Notebook Dell")
                .category("notebook")
                .serialNumber("SN-001")
                .status(EquipmentStatus.DISPONIVEL)
                .build());

        Map<String, String> body = Map.of(
                "name", "Outro Notebook",
                "category", "notebook",
                "serialNumber", "SN-001",
                "status", "DISPONIVEL"
        );

        mockMvc.perform(post("/api/equipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void delete_equipment_deve_retornar_204() throws Exception {
        Equipment saved = repository.save(Equipment.builder()
                .name("Notebook Dell")
                .category("notebook")
                .serialNumber("SN-001")
                .status(EquipmentStatus.DISPONIVEL)
                .build());

        mockMvc.perform(delete("/api/equipments/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
