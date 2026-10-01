package com.rneto.thymeleaf_equipamentos.dto;

import com.rneto.thymeleaf_equipamentos.model.Equipment;
import com.rneto.thymeleaf_equipamentos.model.EquipmentStatus;

public record EquipmentResponse(
        Long id,
        String name,
        String category,
        String serialNumber,
        EquipmentStatus status
) {
    public static EquipmentResponse from(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getCategory(),
                equipment.getSerialNumber(),
                equipment.getStatus()
        );
    }
}
