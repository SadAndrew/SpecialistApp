package com.specialistapp.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private Specialist inviter;

    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private Specialist invitee;

    private String status; // PENDING, ACCEPTED, DECLINED
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Specialist getInviter() {
        return inviter;
    }

    public void setInviter(Specialist inviter) {
        this.inviter = inviter;
    }

    public Specialist getInvitee() {
        return invitee;
    }

    public void setInvitee(Specialist invitee) {
        this.invitee = invitee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}