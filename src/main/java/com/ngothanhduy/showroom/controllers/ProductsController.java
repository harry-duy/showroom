package com.ngothanhduy.showroom.controllers;

import com.ngothanhduy.showroom.services.ProductsService;
import com.ngothanhduy.showroom.services.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductsController {
    private final Auth auth;
    private final SettingsService settingsService;
    private final ProductsService productsService;

    public ProductsController(Auth auth, SettingsService settingsService, ProductsService productsService) {
        this.auth = auth;
        this.settingsService = settingsService;
        this.productsService = productsService;
    }

    @GetMapping("/add-product")
    public String addProductGet(HttpServletRequest request, Model model) {
        if (!auth.isUserLoggedIn(request)) {
            return "redirect:/login";
        }
        model.addAttribute("isUserLoggedIn", true)
                .addAttribute("companyName", settingsService.getCompanyName());
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProductPost(
            @RequestParam String productName,
            @RequestParam MultipartFile productImage,
            HttpServletRequest request,
            Model model) {
        if (!auth.isUserLoggedIn(request)) {
            return "redirect:/login";
        }
        try {
            productsService.addProduct(productName, productImage);
            return "redirect:/#products";
        } catch (Exception e) {
            model.addAttribute("isUserLoggedIn", true)
                    .addAttribute("companyName", settingsService.getCompanyName())
                    .addAttribute("productName", productName)
                    .addAttribute("errorMsg", e.getMessage());
            return "add-product";
        }
    }
}