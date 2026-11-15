package com.proyecto.prestamos.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Perfil;

@Component 
public interface PoliticaPrestamo {
    Perfil perfil();
    int plazoDias();
    BigDecimal calcularMulta(long diasRetraso);
}
