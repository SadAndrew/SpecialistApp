package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    @Query("SELECT s FROM Specialist s WHERE s.professionType.id = :professionTypeId")
    List<Specialist> findByProfessionTypeId(@Param("professionTypeId") Long professionTypeId);

    @Query("SELECT DISTINCT s FROM Specialist s LEFT JOIN s.organizations o WHERE s.approved = true OR (o.approved = true AND o.blocked = false)")
    List<Specialist> findByApprovedOrganizations();

    @Query("SELECT s FROM Specialist s JOIN s.organizations o WHERE o.approved = true AND " +
            "(:name IS NULL OR LOWER(s.username) LIKE LOWER(CAST(CONCAT('%', :name, '%') AS string))) " +
            "AND (:professionId IS NULL OR s.professionType.id = :professionId)")
    List<Specialist> searchSpecialists(@Param("name") String name, @Param("professionId") Long professionTypeId);

    @Query("SELECT s FROM Specialist s JOIN s.organizations o WHERE o.approved = true AND o.id = :organizationId")
    List<Specialist> findByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT s FROM Specialist s JOIN s.organizations o WHERE o.approved = true AND s.professionType.name = :professionName")
    List<Specialist> findByProfessionTypeNameAndOrganizationApprovedTrue(@Param("professionName") String professionName);

    @Query("SELECT s FROM Specialist s JOIN s.professionType p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :professionName, '%'))")
    List<Specialist> findByProfessionTypeNameContainingIgnoreCase(@Param("professionName") String professionName);

    @Query("SELECT s FROM Specialist s WHERE s.email = :email")
    Optional<Specialist> findByEmail(@Param("email") String email);

    List<Specialist> findByApprovedTrue();
}