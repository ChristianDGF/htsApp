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

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_ADMIN"));
        if (isAdmin) {
            model.addAttribute("username", user.getUsername());
            return "admin/home";
        }

        //Por cada nueva empresa agregar un nuevo condicional ejemplo:
        //if(user.getEmpresa().getNombre().equals("HTS")){
        //    return "dashboard/hts";
        //}

        if(user.getEmpresa().getNombre().equals("Congelados")){
            return "dashboard/congelados";
        }

        return "home";
    }


    @GetMapping("/formulario-caso")
    public String formularioCaso(Authentication authentication) {
        return "forms/generico-form";
    }

    @GetMapping("/encuesta")
    public String encuesta(Authentication authentication) {
        return "encuesta";
    }
    
}
