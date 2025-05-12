package com.specialistapp.service;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.OrganizationRepository;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModerationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Organization> getPendingOrganizations() {
        return organizationRepository.findByApprovedFalse();
    }

    public void approveOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        organization.setApproved(true);
        organizationRepository.save(organization);
    }

    public void rejectOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}