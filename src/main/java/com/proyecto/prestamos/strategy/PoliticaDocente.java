package com.proyecto.prestamos.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Perfil;

@Component 
public class PoliticaDocente implements PoliticaPrestamo {
    private static final BigDecimal FIJA = new BigDecimal("2000");

    @Override
    public Perfil perfil() {
        return Perfil.DOCENTE;
    }

    @Override
    public int plazoDias() {
        return 7;
    }

    @Override
    public BigDecimal calcularMulta(long diasRetraso) {
        return diasRetraso <= 0 ? BigDecimal.ZERO : FIJA;
    }
}
