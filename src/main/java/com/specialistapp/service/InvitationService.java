package com.specialistapp.service;

import com.specialistapp.model.entity.Invitation;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private SpecialistService specialistService;

    public List<Invitation> findPendingInvitations(Specialist specialist) {
        return invitationRepository.findByInviteeAndStatus(specialist, "PENDING");
    }

    @Transactional
    public void acceptInvitation(Long invitationId, Specialist specialist) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        if (!invitation.getInvitee().equals(specialist)) {
            throw new IllegalStateException("You are not authorized to accept this invitation");
        }
        invitation.setStatus("ACCEPTED");
        specialist.addOrganization(invitation.getOrganization());
        specialist.setApproved(true); // Approve the specialist upon accepting
        specialistService.save(specialist);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void declineInvitation(Long invitationId, Specialist specialist) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        if (!invitation.getInvitee().equals(specialist)) {
            throw new IllegalStateException("You are not authorized to decline this invitation");
        }
        invitation.setStatus("DECLINED");
        invitationRepository.save(invitation);
    }
}