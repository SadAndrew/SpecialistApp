package com.specialistapp.service;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getOrganizationsBySpecialist(Specialist specialist) {
        return organizationRepository.findByCreatedBy(specialist);
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id).orElseThrow();
    }
    public List<Organization> findPending() {
        return organizationRepository.findByApprovedFalse();
    }

    public void approveOrganization(Long id) {
        Organization org = organizationRepository.findById(id).orElseThrow();
        org.setApproved(true);
        organizationRepository.save(org);
    }

    public void rejectOrganization(Long id) {
        organizationRepository.deleteById(id);
    }
}