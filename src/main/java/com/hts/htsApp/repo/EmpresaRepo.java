package com.hts.htsApp.repo;

import com.hts.htsApp.model.Empresa;
import com.hts.htsApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepo extends JpaRepository<Empresa, Integer> {
    boolean existsByNombre(String nombre);
}
