package com.specialistapp.controller;

import com.specialistapp.model.entity.ProfessionType;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.service.ProfessionTypeService;
import com.specialistapp.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private ProfessionTypeService professionTypeService;

    @GetMapping
    public String searchSpecialists(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "professionId", required = false) Long professionId,
                                    Model model) {

        List<Specialist> specialists = specialistService.searchSpecialists(name, professionId);
        List<ProfessionType> professions = professionTypeService.findAll();

        model.addAttribute("specialists", specialists);
        model.addAttribute("professions", professions);
        model.addAttribute("name", name);
        model.addAttribute("professionId", professionId);

        return "search/specialists";
    }
}
