package com.rneto.thymeleaf_equipamentos.dto;

import com.rneto.thymeleaf_equipamentos.model.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipmentRequest(
        @NotBlank String name,
        @NotBlank String category,
        @NotBlank String serialNumber,
        @NotNull EquipmentStatus status
) {}
