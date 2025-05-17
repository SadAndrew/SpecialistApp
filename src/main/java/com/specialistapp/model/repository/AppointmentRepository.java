package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Appointment;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findBySpecialistOrderByAppointmentDate(Specialist specialist);

    List<Appointment> findBySpecialistAndAppointmentDateAfter(Specialist specialist, LocalDateTime now);

    @Query("SELECT a FROM Appointment a WHERE a.specialist = :specialist " +
            "AND a.appointmentDate BETWEEN :start AND :end " +
            "ORDER BY a.appointmentDate")
    List<Appointment> findBySpecialistAndAppointmentDateBetween(
            @Param("specialist") Specialist specialist,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId " +
            "AND a.appointmentDate >= :now " +
            "ORDER BY a.appointmentDate")
    List<Appointment> findUpcomingByUser(@Param("userId") Long userId,
                                         @Param("now") LocalDateTime now);

    List<Appointment> findByUser(User user);

    List<Appointment> findByUserAndAppointmentDateAfter(User user, LocalDateTime time);
}