package com.specialistapp.controller;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.service.ModerationService;
import com.specialistapp.service.OrganizationService;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/moderator")
@PreAuthorize("hasRole('MODERATOR')")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SpecialistService specialistService;

    @GetMapping("/dashboard")
    public String getModeratorDashboard(Model model) {
        List<Organization> pendingOrganizations = moderationService.getPendingOrganizations();
        List<Organization> allOrganizations = organizationService.getAllOrganizations();
        List<User> users = moderationService.getAllUsers();
        List<User> regularUsers = users.stream().filter(u -> u.getRole().equals("ROLE_USER")).collect(Collectors.toList());
        List<User> specialists = users.stream().filter(u -> u.getRole().equals("ROLE_SPECIALIST")).collect(Collectors.toList());
        model.addAttribute("pendingOrganizations", pendingOrganizations);
        model.addAttribute("allOrganizations", allOrganizations);
        model.addAttribute("regularUsers", regularUsers);
        model.addAttribute("specialists", specialists);
        return "moderation/dashboard";
    }

    @PostMapping("/organizations/{id}/approve")
    public String approveOrganization(@PathVariable Long id) {
        organizationService.approveOrganization(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/organizations/{id}/reject")
    public String rejectOrganization(@PathVariable Long id) {
        organizationService.rejectOrganization(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable Long id) {
        moderationService.blockUser(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable Long id) {
        moderationService.unblockUser(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/organizations/{id}/block")
    public String blockOrganization(@PathVariable Long id) {
        organizationService.blockOrganization(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/organizations/{id}/unblock")
    public String unblockOrganization(@PathVariable Long id) {
        organizationService.unblockOrganization(id);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/specialists/{id}/block")
    public String blockSpecialist(@PathVariable Long id) {
        Specialist specialist = specialistService.findById(id);
        specialist.setBlocked(true);
        specialistService.save(specialist);
        return "redirect:/moderator/dashboard";
    }

    @PostMapping("/specialists/{id}/unblock")
    public String unblockSpecialist(@PathVariable Long id) {
        Specialist specialist = specialistService.findById(id);
        specialist.setBlocked(false);
        specialistService.save(specialist);
        return "redirect:/moderator/dashboard";
    }
}