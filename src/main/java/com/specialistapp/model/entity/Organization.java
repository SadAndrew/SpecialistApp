package com.specialistapp.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean blocked = false;
    private String name;
    private String description;
    private String logoUrl;
    private boolean approved = false;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Specialist createdBy;

    @ManyToMany
    @JoinTable(
            name = "organization_specialists",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "specialist_id")
    )
    private List<Specialist> specialists = new ArrayList<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public Specialist getCreatedBy() { return createdBy; }
    public void setCreatedBy(Specialist createdBy) { this.createdBy = createdBy; }
    public List<Specialist> getSpecialists() { return specialists; }
    public void setSpecialists(List<Specialist> specialists) { this.specialists = specialists; }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}