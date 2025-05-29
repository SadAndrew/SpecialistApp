package com.specialistapp.service;

import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.SpecialistRepository;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Specialist> findByProfessionTypeName(String professionName) {
        return specialistRepository.findByProfessionTypeNameAndOrganizationApprovedTrue(professionName)
                .stream()
                .filter(this::isSpecialistAvailable)
                .collect(Collectors.toList());
    }

    public Specialist findById(Long id) {
        return specialistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialist not found"));
    }

    public List<Specialist> searchSpecialists(String name, Long professionId) {
        List<Specialist> specialists = specialistRepository.searchSpecialists(
                name == null ? "" : name,
                professionId
        );
        return specialists.stream()
                .filter(this::isSpecialistAvailable)
                .collect(Collectors.toList());
    }

    public Specialist save(Specialist specialist) {
        return specialistRepository.save(specialist);
    }

    public List<Specialist> findByProfessionTypeNameContainingIgnoreCase(String profession) {
        return specialistRepository.findByProfessionTypeNameContainingIgnoreCase(profession)
                .stream()
                .filter(this::isSpecialistAvailable)
                .collect(Collectors.toList());
    }

    public List<Specialist> findAllApprovedSpecialists() {
        return specialistRepository.findByApprovedTrue()
                .stream()
                .filter(this::isSpecialistAvailable)
                .collect(Collectors.toList());
    }

    private boolean isSpecialistAvailable(Specialist specialist) {
        // Если специалист заблокирован, сразу возвращаем false
        if (specialist.isBlocked()) {
            return false;
        }

        // Проверяем, одобрен ли специалист
        if (!specialist.isApproved()) {
            return false;
        }

        // Проверяем основную организацию (если есть)
        if (specialist.getOrganization() != null && specialist.getOrganization().isBlocked()) {
            return false;
        }

        // Проверяем все организации из списка organizations
        return specialist.getOrganizations().stream()
                .noneMatch(organization -> organization.isBlocked());
    }
}