package com.specialistapp.controller;

import com.specialistapp.model.entity.User;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("title", "Профиль");
        model.addAttribute("content", "user/profile");
        return "fragments/layout";
    }


    @GetMapping("/appointments")
    public String getUserAppointments(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("appointments",
                appointmentService.findUpcomingByUser(user.getId(), LocalDateTime.now()));
        return "user/appointments";
    }

}