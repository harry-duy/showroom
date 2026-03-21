package com.ngothanhduy.showroom.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final Auth auth;

    public HomeController(Auth auth) {
        this.auth = auth;
    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("isUserLoggedIn", auth.isUserLoggedIn(request));
        return "home";
    }
}