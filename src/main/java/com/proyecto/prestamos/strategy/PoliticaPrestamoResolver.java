package com.proyecto.prestamos.strategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Perfil;

@Component 
public class PoliticaPrestamoResolver {
     private final Map<Perfil, PoliticaPrestamo> politicas = new EnumMap<>(Perfil.class);

    public PoliticaPrestamoResolver(List<PoliticaPrestamo> lista) {
        lista.forEach(p -> politicas.put(p.perfil(), p));
    }

    public PoliticaPrestamo para(Perfil perfil) {
        PoliticaPrestamo p = politicas.get(perfil);
        if (p == null) throw new IllegalStateException("No hay política para el perfil " + perfil);
        return p;
    }
}
