package com.proyecto.prestamos.dto;

import com.proyecto.prestamos.model.Perfil;
import com.proyecto.prestamos.model.Solicitante;

public record SolicitanteDTO(Long idSolicitante, String username, String nombre, String apellido,String email, Perfil perfil) {
    public static SolicitanteDTO de(Solicitante s) {
        return new SolicitanteDTO(s.getIdSolicitante(), s.getUsuario() == null ? null : s.getUsuario().getUsername(),
            s.getNombre(), s.getApellido(), s.getEmail(), s.getPerfil());
    }
}
