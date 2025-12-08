package com.hts.htsApp.controller;

import com.hts.htsApp.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        User user = (User) authentication.getPrincipal();

        // If user has ADMIN role show admin landing page
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_ADMIN"));
        if (isAdmin) {
            // provide username to template instead of relying on Thymeleaf security extras
            model.addAttribute("username", user.getUsername());
            return "admin/home";
        }

        if(user.getEmpresa().getNombre().equals("Congelados")){
            return "dashboard/congelados"; // resolves to templates/dashboard/congelados.html
        }
        return "home"; // resolves to templates/home.html
    }

    @GetMapping("/formulario-caso")
    public String formularioCaso(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String empresa = user.getEmpresa().getNombre();

        if ("Congelados".equals(empresa)) {
            return "forms/congelados-form";
        }

        return "forms/generico-form";
    }

    @GetMapping("/encuesta")
    public String encuesta(Authentication authentication) {
        return "encuesta";
    }
    
}
