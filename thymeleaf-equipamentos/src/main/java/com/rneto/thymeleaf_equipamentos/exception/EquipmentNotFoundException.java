package com.rneto.thymeleaf_equipamentos.exception;

public class EquipmentNotFoundException extends RuntimeException {
    public EquipmentNotFoundException(Long id) {
        super("Equipamento não encontrado: " + id);
    }
}
