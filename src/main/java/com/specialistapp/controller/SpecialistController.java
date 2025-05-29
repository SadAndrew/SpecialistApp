package com.specialistapp.controller;

import com.specialistapp.model.entity.Appointment;
import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.service.AppointmentService;
import com.specialistapp.service.OrganizationService;
import com.specialistapp.service.ReviewService;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/specialist")
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/schedule")
    public String getSchedule(Model model, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        List<Appointment> appointments = appointmentService.getSpecialistAppointments(specialist);
        List<Organization> organizations = organizationService.getOrganizationsByCurrentSpecialist(principal);

        model.addAttribute("appointments", appointments);
        model.addAttribute("organizations", organizations);
        return "specialist/schedule";
    }

    @GetMapping("/organization/create")
    public String showCreateOrganizationForm(Model model) {
        model.addAttribute("organization", new Organization());
        return "specialist/organization-create";
    }

    @PostMapping("/organization/create")
    public String createOrganization(@ModelAttribute Organization organization, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        organization.setCreatedBy(specialist);
        organization.setApproved(false);
        organizationService.saveOrganization(organization);
        return "redirect:/specialist/schedule";
    }

    @GetMapping("/organization/{id}/invite")
    public String showInviteForm(
            @PathVariable Long id,
            Model model,
            Principal principal
    ) {
        Organization organization = organizationService.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "specialist/invite-specialist";
    }

    @PostMapping("/organization/{id}/invite")
    public String handleInvite(
            @PathVariable Long id,
            @RequestParam String email,
            Principal principal
    ) {
        Specialist inviter = specialistService.findByEmail(principal.getName());
        organizationService.inviteSpecialist(id, email, inviter.getId());
        return "redirect:/specialist/schedule";
    }
    @GetMapping("/organization/invite")
    public String showGlobalInviteForm(Model model, Principal principal) {
        List<Organization> organizations = organizationService.getOrganizationsByCurrentSpecialist(principal);
        model.addAttribute("organizations", organizations);
        return "specialist/invite-form";
    }

    @GetMapping("/list")
    public String listSpecialists(Model model) {
        List<Specialist> specialists = specialistService.findAllApprovedSpecialists();
        model.addAttribute("specialists", specialists);
        return "specialist/list"; // Возврат имени шаблона
    }

    @GetMapping("/{id}")
    public String viewSpecialist(@PathVariable Long id, Model model) {
        Specialist specialist = specialistService.findById(id);
        model.addAttribute("specialist", specialist);
        model.addAttribute("reviews", reviewService.findBySpecialist(specialist));
        return "specialists/profile";
    }

    @PostMapping("/appointments/confirm")
    public String confirmAppointment(@RequestParam Long appointmentId) {
        appointmentService.confirmAppointment(appointmentId);
        return "redirect:/specialist/schedule/edit";
    }

    @PostMapping("/appointments/reject")
    public String rejectAppointment(@RequestParam Long appointmentId) {
        appointmentService.rejectAppointment(appointmentId);
        return "redirect:/specialist/schedule/edit";
    }

    @PostMapping("/organization/invite")
    public String handleInviteSpecialist(
            @RequestParam Long organizationId,
            @RequestParam String email, // Используйте email, а не ID
            Principal principal
    ) {
        Specialist inviter = specialistService.findByEmail(principal.getName());
        organizationService.inviteSpecialist(organizationId, email, inviter.getId());
        return "redirect:/specialist/schedule";
    }
    @PostMapping("/organization/{orgId}/remove/{specId}")
    public String removeFromOrganization(
            @PathVariable Long orgId,
            @PathVariable Long specId
    ) {
        organizationService.removeSpecialist(orgId, specId);
        return "redirect:/specialist/schedule";
    }
    @GetMapping("/organization/{orgId}/manage")
    public String manageOrganization(
            @PathVariable Long orgId,
            Model model
    ) {
        Organization organization = organizationService.getOrganizationWithMembers(orgId);
        model.addAttribute("organization", organization);
        return "specialist/manage-organization";
    }

    @GetMapping("/schedule/edit")
    public String editSchedule(Model model, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        List<Appointment> appointments = appointmentService.getSpecialistAppointments(specialist);
        model.addAttribute("appointments", appointments);
        return "specialist/schedule-edit";
    }

}