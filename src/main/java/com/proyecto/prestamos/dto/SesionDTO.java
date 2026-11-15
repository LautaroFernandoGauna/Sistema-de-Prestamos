package com.proyecto.prestamos.dto;

import java.util.List;

public record SesionDTO(String usuario, List<String> roles, String mensaje) {

}
