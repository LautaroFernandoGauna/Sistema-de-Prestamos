package com.proyecto.prestamos.dto;

import com.proyecto.prestamos.model.Equipo;
import com.proyecto.prestamos.model.EstadoEquipo;
import com.proyecto.prestamos.model.TipoEquipo;

public record EquipoDTO(Long id, String codigo, TipoEquipo tipo, String marca, String modelo,String especificacion, EstadoEquipo estado) {
    public static EquipoDTO de(Equipo e) {
        return new EquipoDTO(e.getId(), e.getCodigo(), e.getTipo(), e.getMarca(), e.getModelo(),
                e.getEspecificacion(), e.getEstado());
    }

}
