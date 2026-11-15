package com.proyecto.prestamos.dto;

import com.proyecto.prestamos.model.TipoEquipo;

public record EquipoRequest(TipoEquipo tipo, String codigo, String marca, String modelo, String especificacion) {

}
