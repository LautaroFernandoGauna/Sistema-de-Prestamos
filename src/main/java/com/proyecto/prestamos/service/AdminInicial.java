package com.proyecto.prestamos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.prestamos.model.User;
import com.proyecto.prestamos.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service 
@Transactional 
public class AdminInicial implements CommandLineRunner {
     private final UserRepository users;
    private final PasswordEncoder encoder;

    @Value("${demo.admin.username}") private String adminUser;
    @Value("${demo.admin.password}") private String adminPass;
    @Value("${demo.encargado.username}") private String encargadoUser;
    @Value("${demo.encargado.password}") private String encargadoPass;

    public AdminInicial(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        asegurarCuenta(adminUser, adminPass, "ADMIN");
        asegurarCuenta(encargadoUser, encargadoPass, "ENCARGADO");
    }

    private void asegurarCuenta(String username, String password, String rol) {
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalStateException("Configurar usuario y contraseña de " + rol + " en application.properties");
        }
        User cuenta = users.findByUsername(username);
        if (cuenta == null) {
            users.save(new User(null, username, encoder.encode(password), rol));
        } else if (!rol.equals(cuenta.getRole())) {
            // Un usuario común no puede convertirse en personal por tener el mismo nombre.
            throw new IllegalStateException("El usuario " + username + " ya existe con otro rol");
        } else if (!encoder.matches(password, cuenta.getPassword())) {
            cuenta.setPassword(encoder.encode(password));
            users.save(cuenta);
        }
    }
}
