package com.rneto.thymeleaf_equipamentos.exception;

public class SerialNumberAlreadyExistsException extends RuntimeException {
    public SerialNumberAlreadyExistsException(String serialNumber) {
        super("Número de série já cadastrado: " + serialNumber);
    }
}
