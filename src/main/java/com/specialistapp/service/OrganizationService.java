package com.specialistapp.service;

import com.specialistapp.model.entity.Invitation;
import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.InvitationRepository;
import com.specialistapp.model.repository.OrganizationRepository;
import com.specialistapp.model.repository.SpecialistRepository;
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

    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    private InvitationRepository invitationRepository;

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
        Specialist creator = org.getCreatedBy();
        if (creator != null) {
            creator.setApproved(true);
            specialistRepository.save(creator);
        }

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
        Specialist invitee = specialistService.findById(specialistId);

        Invitation invitation = new Invitation();
        invitation.setOrganization(organization);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setStatus("PENDING");
        invitationRepository.save(invitation);

        // Simulate sending an email (in a real app, use a proper email service)
        System.out.println("Email sent to " + invitee.getEmail() + " with invitation to join " + organization.getName());
    }

    @Transactional
    public void blockOrganization(Long id) {
        Organization org = getOrganizationById(id);
        org.setBlocked(true);
        organizationRepository.save(org);
    }

    @Transactional
    public void unblockOrganization(Long id) {
        Organization org = getOrganizationById(id);
        org.setBlocked(false);
        organizationRepository.save(org);
    }
}