package com.specialistapp.service;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SpecialistService specialistService;

    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getOrganizationsBySpecialist(Specialist specialist) {
        return organizationRepository.findByCreatedBy(specialist);
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    public List<Organization> findPending() {
        return organizationRepository.findByApprovedFalse();
    }

    public List<Organization> findApproved() {
        return organizationRepository.findByApprovedTrue();
    }

    @Transactional
    public void approveOrganization(Long id) {
        Organization org = getOrganizationById(id);
        org.setApproved(true);
        organizationRepository.save(org);
    }

    @Transactional
    public void rejectOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    @Transactional
    public void inviteSpecialist(Long organizationId, Long specialistId, Long inviterId) {
        Organization organization = getOrganizationById(organizationId);
        Specialist inviter = specialistService.findById(inviterId);
        if (!organization.getCreatedBy().equals(inviter)) {
            throw new IllegalStateException("Only the organization creator can invite specialists");
        }
        Specialist specialist = specialistService.findById(specialistId);
        List<Specialist> specialists = organization.getSpecialists();
        if (specialists == null) {
            specialists = new java.util.ArrayList<>();
            organization.setSpecialists(specialists);
        }
        if (!specialists.contains(specialist)) {
            specialists.add(specialist);
            organizationRepository.save(organization);
        }
    }
}