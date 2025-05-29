package com.specialistapp.controller;

import com.specialistapp.model.entity.User;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String getProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login"; // Перенаправление, если пользователь не авторизован
        }

        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "User not found for email: " + email);
            return "error"; // Страница ошибки, если пользователь не найден
        }

        model.addAttribute("user", user); // Передаём объект user в модель
        model.addAttribute("title", "User Profile"); // Устанавливаем заголовок
        return "user/profile"; // Рендерим user/profile.html
    }

    @GetMapping("/appointments")
    public String getUserAppointments(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("appointments", appointmentService.findUpcomingByUser(user.getId(), LocalDateTime.now()));
        model.addAttribute("title", "Your Appointments");
        return "user/appointments";
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/user/appointments";
    }
}