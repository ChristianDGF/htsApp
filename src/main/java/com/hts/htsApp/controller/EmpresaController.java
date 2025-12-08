package com.hts.htsApp.controller;

import com.hts.htsApp.model.Empresa;
import com.hts.htsApp.model.User;
import com.hts.htsApp.repo.EmpresaRepo;
import com.hts.htsApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepo empresaRepo;

    @GetMapping
    public String empresas(Model model) {
        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("empresas", empresas);
        return "empresas"; // resolves to templates/usuarios.html
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> crearEmpresa(@ModelAttribute Empresa nuevaEmpresa) {

        if (empresaRepo.existsByNombre(nuevaEmpresa.getNombre())) {
            return ResponseEntity.badRequest().body("El nombre de la empresa ya está en uso.");
        }

        nuevaEmpresa.setNombre(nuevaEmpresa.getNombre());

        Empresa guardado = empresaRepo.save(nuevaEmpresa);

        return ResponseEntity.ok(guardado);

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarEmpresa(@PathVariable int id) {
        try {
            empresaRepo.deleteById(id);
            return ResponseEntity.ok("Eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar");
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> editarEmpresa(@PathVariable int id, @ModelAttribute Empresa empresaActualizada) {

        Empresa empresa = empresaRepo.findById(id).orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        empresa.setNombre(empresaActualizada.getNombre());

        Empresa guardado = empresaRepo.save(empresa);

        return ResponseEntity.ok(guardado);
    }
}
