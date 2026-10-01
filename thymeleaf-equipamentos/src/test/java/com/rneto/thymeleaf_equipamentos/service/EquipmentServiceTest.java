package com.rneto.thymeleaf_equipamentos.service;

import com.rneto.thymeleaf_equipamentos.dto.EquipmentRequest;
import com.rneto.thymeleaf_equipamentos.dto.EquipmentResponse;
import com.rneto.thymeleaf_equipamentos.exception.SerialNumberAlreadyExistsException;
import com.rneto.thymeleaf_equipamentos.model.Equipment;
import com.rneto.thymeleaf_equipamentos.model.EquipmentStatus;
import com.rneto.thymeleaf_equipamentos.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository repository;

    @InjectMocks
    private EquipmentService service;

    @Test
    void deve_retornar_response_quando_cadastro_valido() {
        EquipmentRequest request = new EquipmentRequest("Notebook Dell", "notebook", "SN-001", EquipmentStatus.DISPONIVEL);

        Equipment saved = Equipment.builder()
                .id(1L)
                .name("Notebook Dell")
                .category("notebook")
                .serialNumber("SN-001")
                .status(EquipmentStatus.DISPONIVEL)
                .build();

        when(repository.save(any(Equipment.class))).thenReturn(saved);

        EquipmentResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.serialNumber()).isEqualTo("SN-001");
        assertThat(response.status()).isEqualTo(EquipmentStatus.DISPONIVEL);
    }

    @Test
    void deve_lancar_excecao_quando_numero_serie_duplicado() {
        EquipmentRequest request = new EquipmentRequest("Notebook Dell", "notebook", "SN-001", EquipmentStatus.DISPONIVEL);

        when(repository.save(any(Equipment.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(SerialNumberAlreadyExistsException.class)
                .hasMessageContaining("SN-001");
    }
}
