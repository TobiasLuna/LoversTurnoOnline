package com.turnos.turnos.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.turnos.turnos.Model.Turno;

public interface TurnoRepository extends JpaRepository <Turno,Long>{
    boolean existsById(String id);

    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);

    //Rango horario
    @Query("SELECT COUNT(t) > 0 FROM Turno t WHERE t.fecha = ?1 AND t.hora BETWEEN ?2 AND ?3")
    boolean existsEnRangoHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    @Query("SELECT t.hora FROM Turno t WHERE t.fecha = ?1")
    List<LocalTime> findHorasByFecha(LocalDate fecha);
}
