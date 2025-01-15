package com.turnos.turnos.Model;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Turno {
    @Id
    private Integer id;
    private String correo;
    private LocalDate fecha;
    @Column
    private LocalTime hora;
    private String nombre;
    private String Ser;
    private String retiro = "no";
}
