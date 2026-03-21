package com.ngothanhduy.showroom.controllers;

import com.ngothanhduy.showroom.services.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final Auth auth;
    private final SettingsService settingsService;

    public HomeController(Auth auth, SettingsService settingsService) {
        this.auth = auth;
        this.settingsService = settingsService;
    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("isUserLoggedIn", auth.isUserLoggedIn(request))
                .addAttribute("companyName", settingsService.getCompanyName());
        return "home";
    }
}