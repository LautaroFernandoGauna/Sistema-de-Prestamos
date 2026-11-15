package com.proyecto.prestamos.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Perfil;

@Component 
public class PoliticaStaff implements PoliticaPrestamo {
    @Override
    public Perfil perfil() {
        return Perfil.STAFF;
    }

    @Override
    public int plazoDias() {
        return 7;
    }

    @Override
    public BigDecimal calcularMulta(long diasRetraso) {
        return BigDecimal.ZERO;
    }
}
