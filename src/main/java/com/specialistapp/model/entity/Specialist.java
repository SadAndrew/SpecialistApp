package com.specialistapp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specialists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Specialist extends User {

    private String bio;
    private String photoUrl; // ссылка на загруженное фото
    private String workSchedule;


    @ManyToOne
    @JoinColumn(name = "profession_type_id")
    private ProfessionType professionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public void setPhotoUrl(String s) {
        this.photoUrl = s;
    }
    public ProfessionType getProfessionType() {
        return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
        this.professionType = professionType;
    }


}
