package com.specialistapp.service;

import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.SpecialistRepository;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistService {

    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    private UserRepository userRepository;

    public Specialist findByEmail(String email) {
        return (Specialist) userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Specialist not found"));
    }

    public List<Specialist> findAll() {
        return specialistRepository.findAll();
    }

    public List<Specialist> findAllApprovedSpecialists() {
        return specialistRepository.findByApprovedOrganizations();
    }

    public List<Specialist> findByProfessionTypeName(String professionName) {
        return specialistRepository.findByProfessionTypeNameAndOrganizationApprovedTrue(professionName);
    }

    public Specialist findById(Long id) {
        return specialistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialist not found"));
    }

    public List<Specialist> searchSpecialists(String name, Long professionId) {
        return specialistRepository.searchSpecialists(
                name == null || name.isEmpty() ? null : name,
                professionId == null || professionId == 0 ? null : professionId
        );
    }
}