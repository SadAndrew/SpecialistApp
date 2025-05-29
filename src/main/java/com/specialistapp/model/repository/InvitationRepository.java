package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Invitation;
import com.specialistapp.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInviteeAndStatus(Specialist invitee, String status);
}