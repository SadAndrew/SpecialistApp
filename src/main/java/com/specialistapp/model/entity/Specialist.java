package com.specialistapp.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Specialist extends User {
    @Column(length = 1000)
    private String bio;

    private String photoUrl;
    private String workSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private boolean blocked;
    private boolean approved = false;
    @ManyToOne
    @JoinColumn(name = "profession_type_id")
    private ProfessionType professionType;

    @ManyToMany(mappedBy = "specialists")
    private List<Organization> organizations = new ArrayList<>();

    // Getters and Setters
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public ProfessionType getProfessionType() { return professionType; }
    public void setProfessionType(ProfessionType professionType) { this.professionType = professionType; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(String workSchedule) { this.workSchedule = workSchedule; }
    public List<Organization> getOrganizations() { return organizations; }
    public void setOrganizations(List<Organization> organizations) { this.organizations = organizations; }

    @Override
    public void setRole(String role) {
        if (role == null || role.isEmpty()) {
            role = "ROLE_SPECIALIST";
        }
        super.setRole(role);
    }

    public void addOrganization(Organization organization) {
        this.organizations.add(organization);
        organization.getSpecialists().add(this);
    }
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
}