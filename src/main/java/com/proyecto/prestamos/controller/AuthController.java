package com.proyecto.prestamos.controller;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.prestamos.dto.LoginRequest;
import com.proyecto.prestamos.dto.RegistroRequest;
import com.proyecto.prestamos.dto.SesionDTO;
import com.proyecto.prestamos.dto.SolicitanteDTO;
import com.proyecto.prestamos.dto.TokenResponse;
import com.proyecto.prestamos.model.User;
import com.proyecto.prestamos.repository.UserRepository;
import com.proyecto.prestamos.security.JwtUtil;
import com.proyecto.prestamos.service.SolicitanteService;
import com.proyecto.prestamos.service.TokenRevocationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final UserRepository users;
    private final SolicitanteService solicitantes;
    private final JwtUtil jwtUtil;
    private final TokenRevocationService revocacion;

    public AuthController(AuthenticationManager manager, UserRepository users,
                          SolicitanteService solicitantes, JwtUtil jwtUtil,
                          TokenRevocationService revocacion) {
        this.manager = manager;
        this.users = users;
        this.solicitantes = solicitantes;
        this.jwtUtil = jwtUtil;
        this.revocacion = revocacion;
    }

    // Registro público: siempre crea una cuenta USER con perfil ALUMNO.
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitanteDTO registrarse(@RequestBody RegistroRequest datos) {
        return solicitantes.registrar(datos);
    }

    @PostMapping("/signin")
    public TokenResponse ingresar(@RequestBody LoginRequest datos) {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(datos.username(), datos.password()));
        User user = users.findByUsername(auth.getName());
        return new TokenResponse(jwtUtil.generateToken(auth.getName()), user.getUsername(), user.getRole());
    }

    @GetMapping("/me")
    public SesionDTO miSesion(Authentication auth) {
        return new SesionDTO(auth.getName(),
                auth.getAuthorities().stream().map(a -> a.getAuthority()).toList(), "Autenticado");
    }

    @PostMapping("/logout")
    public String salir(@RequestHeader("Authorization") String authorization) {
        // Solo llega aquí cuando el filtro JWT autenticó la petición.
        revocacion.revoke(authorization.substring(7));
        return "JWT revocado correctamente.";
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String errorCredenciales() { return "Usuario o contraseña incorrectos."; }
}
