package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    @Query("SELECT s FROM Specialist s WHERE s.professionType.id = :professionTypeId")
    List<Specialist> findByProfessionTypeId(@Param("professionTypeId") Long professionTypeId);

    @Query("SELECT s FROM Specialist s WHERE s.organization.id = :organizationId")
    List<Specialist> findByOrganizationId(@Param("organizationId") Long organizationId);
    @Query("SELECT s FROM Specialist s WHERE " +
            "(:name IS NULL OR LOWER(s.username) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:professionId IS NULL OR s.professionType.id = :professionId)")
    List<Specialist> searchSpecialists(String name, Long professionId);
    List<Specialist> findByOrganization_ApprovedTrue();
    List<Specialist> findByProfessionType_NameAndOrganization_ApprovedTrue(String name);
}