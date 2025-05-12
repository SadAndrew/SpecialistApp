package com.specialistapp.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profession_types")
public class ProfessionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // название профессии

    @OneToMany(mappedBy = "professionType")
    private List<Specialist> specialists;

    // Getters и Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
