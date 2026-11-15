package com.proyecto.prestamos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.prestamos.model.User;

@Repository 
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
    boolean existsByUsername(String username);



}
