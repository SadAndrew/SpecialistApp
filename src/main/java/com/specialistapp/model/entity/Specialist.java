package com.specialistapp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specialists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Specialist extends User {

    @Column(length = 1000)
    private String bio;

    private String photoUrl; // ссылка на загруженное фото
    private String workSchedule;


    @ManyToOne
    @JoinColumn(name = "profession_type_id")
    private ProfessionType professionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ProfessionType getProfessionType() {
        return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
        this.professionType = professionType;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public void setRole(String role) {
        if (role == null || role.isEmpty()) {
            role = "ROLE_SPECIALIST";
        }
        super.setRole(role);
    }
}
