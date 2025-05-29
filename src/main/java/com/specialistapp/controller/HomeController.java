package com.specialistapp.controller;

import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.SpecialistService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    private final SpecialistService specialistService;
    private final UserService userService;
    private final AppointmentService appointmentService;

    @Autowired
    public HomeController(SpecialistService specialistService,
                          UserService userService,
                          AppointmentService appointmentService) {
        this.specialistService = specialistService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "profession", required = false) String profession,
                        Model model,
                        Principal principal) {

        List<Specialist> specialists = (profession != null && !profession.isEmpty())
                ? specialistService.findByProfessionTypeNameContainingIgnoreCase(profession)
                : specialistService.findAllApprovedSpecialists();

        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            if (user != null && !user.isBlocked()) {
                model.addAttribute("userAppointments",
                        appointmentService.findUpcomingByUser(user.getId(), LocalDateTime.now()));
            } else {
                return "redirect:/auth/login?error=true&blocked=true";
            }
        }

        model.addAttribute("specialists", specialists);
        model.addAttribute("selectedProfession", profession);
        model.addAttribute("title", "Home");
        model.addAttribute("content", "home/index");

        return "fragments/layout";
    }
}