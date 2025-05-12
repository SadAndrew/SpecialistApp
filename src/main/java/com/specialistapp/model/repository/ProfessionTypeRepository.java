package com.specialistapp.model.repository;

import com.specialistapp.model.entity.ProfessionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionTypeRepository extends JpaRepository<ProfessionType, Long> {
}
