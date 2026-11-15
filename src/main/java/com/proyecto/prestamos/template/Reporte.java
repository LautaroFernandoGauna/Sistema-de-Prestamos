package com.proyecto.prestamos.template;

import java.util.List;


public class Reporte {
    private final String titulo;
    private final String[] encabezados;
    private final List<String[]> filas;

    public Reporte(String titulo, String[] encabezados, List<String[]> filas) {
        this.titulo = titulo;
        this.encabezados = encabezados;
        this.filas = filas;
    }

    public String getTitulo() { return titulo; }
    public String[] getEncabezados() { return encabezados; }
    public List<String[]> getFilas() { return filas; }
}
