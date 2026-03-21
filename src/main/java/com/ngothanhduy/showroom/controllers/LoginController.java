package com.ngothanhduy.showroom.controllers;

import com.ngothanhduy.showroom.services.LoginService;
import com.ngothanhduy.showroom.services.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final LoginService loginService;
    private final Auth auth;
    private final SettingsService settingsService;

    public LoginController(LoginService loginService, Auth auth, SettingsService settingsService) {
        this.loginService = loginService;
        this.auth = auth;
        this.settingsService = settingsService;
    }

    @GetMapping("/login")
    public String loginGet(HttpServletRequest request, Model model) {
        if (auth.isUserLoggedIn(request)) {
            return "redirect:/";
        }
        model.addAttribute("companyName", settingsService.getCompanyName());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        if (auth.isUserLoggedIn(request)) {
            return "redirect:/";
        }
        if (loginService.checkLogin(username, password)) {
            auth.setUserLoggedIn(response, username);
            return "redirect:/";
        }
        model.addAttribute("errorMsg", "Wrong username or password")
                .addAttribute("username", username)
                .addAttribute("password", password)
                .addAttribute("companyName", settingsService.getCompanyName());
        return "login";
    }
}