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

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private UserService userService;

    @GetMapping("/book/{specialistId}")
    public String showBookingForm(@PathVariable Long specialistId, Model model) {
        Specialist specialist = specialistService.findById(specialistId);
        model.addAttribute("specialist", specialist);
        model.addAttribute("appointment", new Appointment());
        return "appointments/book";
    }

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment,
                                  @RequestParam Long specialistId,
                                  Principal principal,
                                  Model model) {
        User user = userService.findByEmail(principal.getName());
        Specialist specialist = specialistService.findById(specialistId);

        appointment.setUser(user);
        appointment.setSpecialist(specialist);
        appointment.setStatus("PENDING");

        appointmentService.saveAppointment(appointment);

        return "redirect:/user/appointments?booked";
    }
}
