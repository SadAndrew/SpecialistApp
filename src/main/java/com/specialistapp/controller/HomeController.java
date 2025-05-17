package com.specialistapp.controller;

import com.specialistapp.model.entity.Specialist;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private SpecialistService specialistService;

    @GetMapping("/")
    public String index(@RequestParam(value = "profession", required = false) String profession,
                        Model model) {
        List<Specialist> specialists = (profession != null && !profession.isEmpty())
                ? specialistService.findByProfessionTypeName(profession)
                : specialistService.findAllApprovedSpecialists();

        model.addAttribute("specialists", specialists);
        model.addAttribute("selectedProfession", profession);
        model.addAttribute("title", "Home");
        model.addAttribute("content", "home/index");
        return "fragments/layout";
    }
}