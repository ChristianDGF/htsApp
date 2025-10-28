package com.hts.htsApp.controller;

import com.hts.htsApp.model.User;
import com.hts.htsApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/User")
public class UsuarioController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String usuarios(Model model) {
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "usuarios"; // resolves to templates/usuarios.html
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> crearUsuario(@ModelAttribute User nuevoUsuario) {

        if (userRepo.existsByUsername(nuevoUsuario.getUsername())) {
            return ResponseEntity.badRequest().body("El correo ya está en uso.");
        }

        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        User guardado = userRepo.save(nuevoUsuario);

        return ResponseEntity.ok(guardado);

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        try {
            userRepo.deleteById(id);
            return ResponseEntity.ok("Eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar");
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> editarUsuario(@PathVariable int id, @ModelAttribute User usuarioActualizado) {

        User usuario = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));



        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setEmpresa(usuarioActualizado.getEmpresa());

        if(usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        User guardado = userRepo.save(usuario);

        return ResponseEntity.ok(guardado);
    }

}
