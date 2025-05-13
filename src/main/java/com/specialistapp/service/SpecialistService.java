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

    public List<Specialist> findByProfessionType(Long professionTypeId) {
        return specialistRepository.findByProfessionTypeId(professionTypeId);
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
    public List<Specialist> findAllApprovedSpecialists() {
        return specialistRepository.findByOrganization_ApprovedTrue();
    }

    public List<Specialist> findByProfessionTypeName(String professionName) {
        return specialistRepository.findByProfessionType_NameAndOrganization_ApprovedTrue(professionName);
    }



    public List<Specialist> findAllApproved() {
        return specialistRepository.findAll(); // или фильтровать, если нужна проверка
    }

}