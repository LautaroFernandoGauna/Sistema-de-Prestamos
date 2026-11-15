package com.proyecto.prestamos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.proyecto.prestamos.model.EstadoPrestamo;
import com.proyecto.prestamos.model.Prestamo;

public record PrestamoDTO(Long id, String solicitante, String equipo,LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista,
                          LocalDate fechaDevolucionReal, EstadoPrestamo estado, BigDecimal multa, boolean vencido) {
    public static PrestamoDTO de(Prestamo p) {
        return new PrestamoDTO(p.getId(),
                p.getSolicitante().getNombre() + " " + p.getSolicitante().getApellido(),
                p.getEquipo().getCodigo() + " - " + p.getEquipo().getMarca() + " " + p.getEquipo().getModelo(),
                p.getFechaPrestamo(), p.getFechaDevolucionPrevista(), p.getFechaDevolucionReal(),
                p.getEstado(), p.getMulta(), p.isVencido());

}
}
