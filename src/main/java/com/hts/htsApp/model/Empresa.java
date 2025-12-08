package com.hts.htsApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Empresa {


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    @Getter
    private String nombre;


    public Empresa(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Empresa() {}


}
