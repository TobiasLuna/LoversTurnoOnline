package com.turnos.turnos.TurnosDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Propiedades {

    private String titulo;
    private List<String> secciones = new ArrayList<String>();
    private Map<String,Integer> articulos = new HashMap<String,Integer>();

    public Propiedades()
    {
        titulo = "LOVERS";
        
        secciones.add("INICIO");
        secciones.add("SERVICIOS");
        secciones.add("CURSOS");
        secciones.add("PRECIOS");
        secciones.add("TURNO");

        articulos.put("SOFTLISO", 17000);
        articulos.put("SOFTDISEÑOSIMPLE", 17500);
        articulos.put("SOFTFULL", 20000);
        articulos.put("SOFTAPLIQUES", 20000);
        articulos.put("SEMI", 15500);
        articulos.put("CAPPING", 15500);
        articulos.put("PRESSON", 13000);
        articulos.put("PRESSONFULLSET", 16500);
    }
}
