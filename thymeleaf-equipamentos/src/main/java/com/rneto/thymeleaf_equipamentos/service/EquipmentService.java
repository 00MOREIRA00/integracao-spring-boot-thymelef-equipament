package com.rneto.thymeleaf_equipamentos.service;

import com.rneto.thymeleaf_equipamentos.dto.EquipmentRequest;
import com.rneto.thymeleaf_equipamentos.dto.EquipmentResponse;
import com.rneto.thymeleaf_equipamentos.exception.EquipmentNotFoundException;
import com.rneto.thymeleaf_equipamentos.exception.SerialNumberAlreadyExistsException;
import com.rneto.thymeleaf_equipamentos.model.Equipment;
import com.rneto.thymeleaf_equipamentos.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository repository;

    public List<EquipmentResponse> findAll() {
        return repository.findAll().stream()
                .map(EquipmentResponse::from)
                .toList();
    }

    public EquipmentResponse findById(Long id) {
        return repository.findById(id)
                .map(EquipmentResponse::from)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
    }

    public EquipmentResponse create(EquipmentRequest request) {
        Equipment equipment = Equipment.builder()
                .name(request.name())
                .category(request.category())
                .serialNumber(request.serialNumber())
                .status(request.status())
                .build();
        try {
            return EquipmentResponse.from(repository.save(equipment));
        } catch (DataIntegrityViolationException e) {
            throw new SerialNumberAlreadyExistsException(request.serialNumber());
        }
    }

    public EquipmentResponse update(Long id, EquipmentRequest request) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));

        equipment.setName(request.name());
        equipment.setCategory(request.category());
        equipment.setSerialNumber(request.serialNumber());
        equipment.setStatus(request.status());

        try {
            return EquipmentResponse.from(repository.save(equipment));
        } catch (DataIntegrityViolationException e) {
            throw new SerialNumberAlreadyExistsException(request.serialNumber());
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EquipmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
