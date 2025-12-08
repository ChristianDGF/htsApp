package com.hts.htsApp.repo;

import com.hts.htsApp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    
    @EntityGraph(attributePaths = "empresa")  // fuerza fetch de empresa
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select u from User u left join fetch u.empresa")
    List<User> findAllWithEmpresa();

    @Query("select u from User u left join fetch u.empresa where u.id = :id")
    Optional<User> findByIdWithEmpresa(@Param("id") int id);
}
