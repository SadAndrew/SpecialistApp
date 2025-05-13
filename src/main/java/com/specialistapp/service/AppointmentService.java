package com.specialistapp.service;

import com.specialistapp.model.entity.Appointment;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Duration APPOINTMENT_DURATION = Duration.ofMinutes(30);
    private static final LocalTime WORKDAY_START = LocalTime.of(9, 0);
    private static final LocalTime WORKDAY_END = LocalTime.of(17, 0);

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(User user, Specialist specialist, LocalDateTime time) {
        if (!isTimeSlotAvailable(specialist, time)) {
            throw new IllegalArgumentException("Time slot not available");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setSpecialist(specialist);
        appointment.setAppointmentDate(time);
        return appointmentRepository.save(appointment);
    }

    public List<LocalDateTime> findAvailableSlots(Specialist specialist) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = startDate.plusDays(7);

        List<Appointment> existingAppointments = appointmentRepository
                .findBySpecialistAndAppointmentDateBetween(specialist, startDate, endDate);

        List<LocalDateTime> availableSlots = new ArrayList<>();

        for (int day = 0; day < 7; day++) {
            LocalDateTime currentDay = startDate.plusDays(day);

            if (currentDay.getDayOfWeek().getValue() >= 6) continue;

            LocalDateTime slot = currentDay.with(WORKDAY_START);
            while (slot.toLocalTime().isBefore(WORKDAY_END)) {
                if (isTimeSlotAvailable(specialist, slot, existingAppointments)) {
                    availableSlots.add(slot);
                }
                slot = slot.plus(APPOINTMENT_DURATION);
            }
        }

        return availableSlots.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private boolean isTimeSlotAvailable(Specialist specialist, LocalDateTime time) {
        List<Appointment> conflicting = appointmentRepository
                .findBySpecialistAndAppointmentDateBetween(
                        specialist,
                        time.minus(APPOINTMENT_DURATION),
                        time.plus(APPOINTMENT_DURATION)
                );
        return conflicting.isEmpty();
    }

    private boolean isTimeSlotAvailable(Specialist specialist, LocalDateTime time,
                                        List<Appointment> existingAppointments) {
        return existingAppointments.stream()
                .noneMatch(appt -> isOverlapping(appt.getAppointmentDate(), time));
    }

    private boolean isOverlapping(LocalDateTime existing, LocalDateTime newSlot) {
        LocalDateTime existingEnd = existing.plus(APPOINTMENT_DURATION);
        LocalDateTime newEnd = newSlot.plus(APPOINTMENT_DURATION);

        return newSlot.isBefore(existingEnd) && newEnd.isAfter(existing);
    }

    public List<Appointment> getSpecialistAppointments(Specialist specialist) {
        return appointmentRepository.findBySpecialistOrderByAppointmentDate(specialist);
    }

    public List<Appointment> findUpcomingByUser(Long userId, LocalDateTime now) {
        return appointmentRepository.findUpcomingByUser(userId, now);
    }

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void confirmAppointment(Long id) {
        Appointment app = appointmentRepository.findById(id).orElseThrow();
        app.setConfirmed(true);
        appointmentRepository.save(app);
    }

    public void rejectAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }


}
