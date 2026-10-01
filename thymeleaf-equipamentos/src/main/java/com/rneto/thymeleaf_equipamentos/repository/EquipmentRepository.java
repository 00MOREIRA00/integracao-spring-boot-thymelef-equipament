package com.rneto.thymeleaf_equipamentos.repository;

import com.rneto.thymeleaf_equipamentos.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
