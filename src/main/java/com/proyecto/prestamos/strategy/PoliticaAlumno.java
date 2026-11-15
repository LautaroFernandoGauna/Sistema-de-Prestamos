package com.proyecto.prestamos.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Perfil;

@Component 
public class PoliticaAlumno implements PoliticaPrestamo {
    private static final BigDecimal POR_DIA = new BigDecimal("500");

    @Override
    public Perfil perfil() {
        return Perfil.ALUMNO;
    }

    @Override
    public int plazoDias() {
        return 3;
    }

    @Override
    public BigDecimal calcularMulta(long diasRetraso) {
        return diasRetraso <= 0 ? BigDecimal.ZERO : POR_DIA.multiply(BigDecimal.valueOf(diasRetraso));
    }
}
