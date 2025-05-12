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

    @GetMapping("/book")
    public String showBookingForm(@RequestParam("specialistId") Long specialistId, Model model) {
        Specialist specialist = specialistService.findById(specialistId);
        model.addAttribute("specialist", specialist);
        model.addAttribute("slots", appointmentService.findAvailableSlots(specialist));
        return "appointments/book";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam("specialistId") Long specialistId,
                                  @RequestParam("slot") String slot,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Specialist specialist = specialistService.findById(specialistId);
        LocalDateTime selectedSlot = LocalDateTime.parse(slot);
        appointmentService.createAppointment(user, specialist, selectedSlot);
        return "redirect:/user/appointments";
    }
}
