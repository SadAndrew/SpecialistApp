package com.specialistapp.controller;

import com.specialistapp.model.entity.Appointment;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.SpecialistService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private UserService userService;

    @GetMapping("/book")
    public String showBookingForm(@RequestParam Long specialistId, Model model) {
        try {
            Specialist specialist = specialistService.findById(specialistId);
            List<LocalDateTime> availableSlots = appointmentService.findAvailableSlots(specialist);

            model.addAttribute("specialist", specialist);
            model.addAttribute("availableSlots", availableSlots); // Добавляем слоты в модель
            return "appointments/book";
        } catch (Exception e) {
            return "redirect:/error?message=Specialist not found";
        }
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long specialistId,
                                  @RequestParam String appointmentDate,
                                  Principal principal,
                                  Model model) {
        try {
            User user = userService.findByEmail(principal.getName());
            Specialist specialist = specialistService.findById(specialistId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(appointmentDate, formatter);

            // Проверка доступности времени
            if (!appointmentService.isTimeSlotAvailable(specialist, dateTime)) {
                throw new IllegalArgumentException("Это время уже занято");
            }

            Appointment appointment = new Appointment();
            appointment.setUser(user);
            appointment.setSpecialist(specialist);
            appointment.setAppointmentDate(dateTime);
            appointment.setStatus("PENDING");

            appointmentService.saveAppointment(appointment);
            return "redirect:/user/appointments?success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при записи: " + e.getMessage());
            model.addAttribute("specialist", specialistService.findById(specialistId));
            return "appointments/book";
        }
    }
}