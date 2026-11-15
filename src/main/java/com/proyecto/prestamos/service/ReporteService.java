package com.proyecto.prestamos.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyecto.prestamos.template.Reporte;
import com.proyecto.prestamos.template.ReporteTemplate;

@Service 
public class ReporteService {
    private final Map<String, ReporteTemplate> reportes;

    public ReporteService(List<ReporteTemplate> lista) {
        this.reportes = lista.stream().collect(Collectors.toMap(ReporteTemplate::clave, Function.identity()));
    }

    public List<String> claves() { return reportes.keySet().stream().sorted().toList(); }

    public Reporte generar(String clave) { return buscar(clave).generar(); }

    public String csv(String clave) {
        ReporteTemplate plantilla = buscar(clave);
        return plantilla.aCsv(plantilla.generar());
    }

    private ReporteTemplate buscar(String clave) {
        ReporteTemplate r = reportes.get(clave);
        if (r == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte inexistente.");
        return r;
    }
}
