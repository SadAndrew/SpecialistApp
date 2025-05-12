package com.specialistapp.service;

import com.specialistapp.model.entity.ProfessionType;
import com.specialistapp.model.repository.ProfessionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionTypeService {

    @Autowired
    private ProfessionTypeRepository professionTypeRepository;

    public List<ProfessionType> findAll() {
        return professionTypeRepository.findAll();
    }

    public ProfessionType findById(Long id) {
        return professionTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Профессия не найдена"));
    }
}


