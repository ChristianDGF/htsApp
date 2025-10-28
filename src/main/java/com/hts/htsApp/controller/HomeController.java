package com.hts.htsApp.controller;

import com.hts.htsApp.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if(user.getEmpresa().equals("Acero Estrella")){
            return "dashboard/acero_estrella"; // resolves to templates/dashboard/acero_estrella.html
        }
        return "home"; // resolves to templates/home.html
    }
}
