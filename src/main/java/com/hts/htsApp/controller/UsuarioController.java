package com.hts.htsApp.controller;

import com.hts.htsApp.model.Empresa;
import com.hts.htsApp.model.User;
import com.hts.htsApp.repo.EmpresaRepo;
import com.hts.htsApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/User")
public class UsuarioController {

    @Autowired private UserRepo userRepo;
    @Autowired private EmpresaRepo empresaRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String usuarios(Model model) {
        List<User> users = userRepo.findAllWithEmpresa(); // evita LAZY en vista
        List<Empresa> empresas = empresaRepo.findAll();   // para el <select> del modal
        model.addAttribute("users", users);
        model.addAttribute("empresas", empresas);
        return "usuarios";
    }

    // DTO plano para devolver lo que espera el JS (empresa como String)
    private Map<String, Object> toView(User u) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", u.getId());
        m.put("username", u.getUsername());
        m.put("empresa", u.getEmpresa() != null ? u.getEmpresa().getNombre() : ""); // <- lo que pinta la tabla
        m.put("empresaId", u.getEmpresa() != null ? u.getEmpresa().getId() : null); // <- útil para editar
        return m;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> crearUsuario(@ModelAttribute User nuevoUsuario,
                                          @RequestParam(value = "empresaId", required = false) Integer empresaId) {

        if (userRepo.existsByUsername(nuevoUsuario.getUsername())) {
            return ResponseEntity.badRequest().body("El correo ya está en uso.");
        }

        // vincular empresa por id (si llega)
        Empresa empresa = null;
        if (empresaId != null) {
            empresa = empresaRepo.findById(empresaId)
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        }
        nuevoUsuario.setEmpresa(empresa);

        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        User guardado = userRepo.save(nuevoUsuario);
        return ResponseEntity.ok(toView(guardado));
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
    public ResponseEntity<?> editarUsuario(@PathVariable int id,
                                           @ModelAttribute User usuarioActualizado,
                                           @RequestParam(value = "empresaId", required = false) Integer empresaId) {

        User usuario = userRepo.findByIdWithEmpresa(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(usuarioActualizado.getUsername());

        if (empresaId != null) {
            Empresa empresa = empresaRepo.findById(empresaId)
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
            usuario.setEmpresa(empresa);
        } else {
            usuario.setEmpresa(null);
        }

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        userRepo.save(usuario);

        // <<< CLAVE: re-cargar con empresa inicializada
        User full = userRepo.findByIdWithEmpresa(id).orElse(usuario);

        return ResponseEntity.ok(toView(full));
    }
}
