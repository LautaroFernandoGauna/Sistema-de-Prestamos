package com.proyecto.prestamos.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.prestamos.model.RevokedToken;
import com.proyecto.prestamos.repository.RevokedTokenRepository;
import com.proyecto.prestamos.security.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class TokenRevocationService {

    private final RevokedTokenRepository revokedTokens;
    private final JwtUtil jwtUtil;

    public TokenRevocationService(RevokedTokenRepository revokedTokens, JwtUtil jwtUtil) {
        this.revokedTokens = revokedTokens;
        this.jwtUtil = jwtUtil;
    }

    public boolean isRevoked(String jti) {
        // También rechazamos JWT antiguos que no tienen jti.
        return jti == null || jti.isBlank() || revokedTokens.existsById(jti);
    }

    @Transactional
    public void revoke(String jwt) {
        // Compruebo si se puede revocar. Se comprueban firma y vencimiento otra vez, 
        // además de la verificación del filtro.
        Claims claims = jwtUtil.getClaims(jwt);
        String jti = claims.getId();
        if (jti == null || jti.isBlank() || isRevoked(jti)) {
            throw new IllegalArgumentException("JWT inválido o previamente revocado");
        }

        // Se registra en la base de datos un JWT que ya no debe ser aceptado.
        // El identificador se conserva hasta la fecha de vencimiento del JWT.
        revokedTokens.save(new RevokedToken(jti, claims.getExpiration().toInstant()));

        // Busca los token ya vencidos y los elimina.
        // Mantenimiento simple para que los registros vencidos no crezcan indefinidamente.
        revokedTokens.deleteByExpiresAtBefore(Instant.now());
    }
}
