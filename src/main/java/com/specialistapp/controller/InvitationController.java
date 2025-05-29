package com.specialistapp.controller;

import com.specialistapp.model.entity.Invitation;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.service.InvitationService;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/specialist/invitations")
@PreAuthorize("hasRole('SPECIALIST')")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private SpecialistService specialistService;

    @GetMapping
    public String viewInvitations(Model model, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        List<Invitation> invitations = invitationService.findPendingInvitations(specialist);
        model.addAttribute("invitations", invitations);
        return "specialist/invitations";
    }

    @PostMapping("/{id}/accept")
    public String acceptInvitation(@PathVariable Long id, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        invitationService.acceptInvitation(id, specialist);
        return "redirect:/specialist/invitations";
    }

    @PostMapping("/{id}/decline")
    public String declineInvitation(@PathVariable Long id, Principal principal) {
        Specialist specialist = specialistService.findByEmail(principal.getName());
        invitationService.declineInvitation(id, specialist);
        return "redirect:/specialist/invitations";
    }
}