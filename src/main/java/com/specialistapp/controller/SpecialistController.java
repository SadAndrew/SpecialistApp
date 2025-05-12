package com.specialistapp.controller;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.OrganizationService;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/specialist")
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/schedule")
    public String getSchedule(Model model, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        model.addAttribute("appointments",
                appointmentService.getSpecialistAppointments(specialist));
        return "specialist/schedule";
    }

    @GetMapping("/organization/create")
    public String showCreateOrganizationForm(Model model) {
        model.addAttribute("organization", new Organization());
        return "specialist/organization-create";
    }

    @PostMapping("/organization/create")
    public String createOrganization(@ModelAttribute Organization organization,
                                     Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        organization.setCreatedBy(specialist);
        organizationService.saveOrganization(organization);
        return "redirect:/specialist/dashboard";
    }
    @GetMapping("/register/specialist")
    public String showSpecialistRegistrationForm(Model model) {
        model.addAttribute("specialist", new Specialist()); // 
        return "auth/register-specialist";
    }
    @GetMapping("/specialists/find")
    public String findSpecialists(Model model) {
        model.addAttribute("title", "Поиск специалистов");
        model.addAttribute("content", "specialists/find");
        return "fragments/layout";
    }

}