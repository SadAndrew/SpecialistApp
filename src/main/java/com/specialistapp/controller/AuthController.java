package com.specialistapp.controller;

import com.specialistapp.model.entity.ProfessionType;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.service.ProfessionTypeService;
import com.specialistapp.service.RegistrationService;
import com.specialistapp.service.SpecialistService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ProfessionTypeService professionTypeService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверно имя пользоввателя или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из аккаунта");
        }
        return "auth/login";
    }

    @GetMapping("/register/user")
    public String showUserRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "User Registration");
        model.addAttribute("content", "auth/register-user");
        return "fragments/layout";
    }

    @PostMapping("/register/user")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            registrationService.registerUser(user);
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "auth/register-user";
        }
    }


    @GetMapping("/register/specialist")
    public String showSpecialistRegistrationForm(Model model) {
        List<ProfessionType> professionTypes = professionTypeService.findAll();
        model.addAttribute("specialist", new Specialist());
        model.addAttribute("professions", professionTypes);
        return "auth/register-specialist";
    }

    @PostMapping("/register/specialist")
    public String registerSpecialist(
            @ModelAttribute("specialist") Specialist specialist,
            @RequestParam("photo") MultipartFile file,
            @RequestParam("professionType.id") Long professionTypeId,
            Model model) {
        try {
            ProfessionType professionType = professionTypeService.findById(professionTypeId);
            specialist.setProfessionType(professionType);

            // Обработка файла
            if (!file.isEmpty()) {
                // Создаем директорию в корне проекта, а не во временной папке Tomcat
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File uploadFolder = new File(uploadDir);

                // Создаем директорию, если она не существует
                if (!uploadFolder.exists()) {
                    boolean created = uploadFolder.mkdirs();
                    if (!created) {
                        throw new IOException("Could not create upload directory");
                    }
                }

                // Генерируем уникальное имя файла
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath = uploadDir + fileName;

                // Сохраняем файл
                file.transferTo(new File(filePath));

                // Сохраняем относительный путь для доступа через веб
                specialist.setPhotoUrl("/uploads/" + fileName);
            }

            // Сохранение специалиста
            registrationService.registerSpecialist(specialist);
            return "redirect:/auth/login?registered";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            model.addAttribute("professions", professionTypeService.findAll());
            return "auth/register-specialist";
        }
    }
}