package com.specialistapp.controller;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moderator")
@PreAuthorize("hasRole('MODERATOR')")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @GetMapping("/organizations/pending")
    public String getPendingOrganizations(Model model) {
        model.addAttribute("organizations", moderationService.getPendingOrganizations());
        return "moderation/organizations";
    }

    @PostMapping("/organizations/{id}/approve")
    public String approveOrganization(@PathVariable Long id) {
        moderationService.approveOrganization(id);
        return "redirect:/moderator/organizations/pending";
    }

    @PostMapping("/organizations/{id}/reject")
    public String rejectOrganization(@PathVariable Long id) {
        moderationService.rejectOrganization(id);
        return "redirect:/moderator/organizations/pending";
    }

    @GetMapping("/users")
    public String getUserManagement(Model model) {
        model.addAttribute("users", moderationService.getAllUsers());
        return "moderation/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        moderationService.deleteUser(id);
        return "redirect:/moderator/users";
    }
}