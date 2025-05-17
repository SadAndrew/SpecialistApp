package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByApprovedFalse();
    List<Organization> findByApprovedTrue();
    List<Organization> findByCreatedBy(Specialist specialist);

    @Query("SELECT o FROM Organization o JOIN o.specialists s WHERE s.id = :specialistId")
    List<Organization> findBySpecialistMember(@Param("specialistId") Long specialistId);
}