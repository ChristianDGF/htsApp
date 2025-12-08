package com.hts.htsApp.repo;

import com.hts.htsApp.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentPageRepo extends JpaRepository<ContentPage, Integer> {
    Optional<ContentPage> findBySlug(String slug);
    boolean existsBySlug(String slug);
}

