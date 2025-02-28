package com.turnos.turnos.Ser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turnos.turnos.Model.Turno;
import com.turnos.turnos.Repository.TurnoRepository;

@Service
public class TurnoSer {
    @Autowired
    private TurnoRepository turnoRepository;

    public List<Turno> listarTurnos() {
        return turnoRepository.findAll();
    }

    public Turno guardar(Turno t) {
        t.setId(generarCodigoAleatorio());
        validarFecha(t.getFecha(),t.getHora());
        validarCorreo(t.getCorreo());
        return turnoRepository.save(t);
    }

    public Turno obtenerPorId(Integer id) {
        return turnoRepository.findById(id).orElse(null);
    }

    public void Eliminar(Integer id) {
        turnoRepository.deleteById(id);
    }

    private void validarFecha(LocalDate fecha,LocalTime hora) {
        LocalDate Fecha_minima = LocalDate.of(2025,3,5);
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser anterior a hoy");
        }
        if (turnoRepository.existsByFechaAndHora(fecha, hora)) {
            throw new IllegalArgumentException("Ya existe un turno para la fecha: ");
        }
        if(fecha.isBefore(Fecha_minima))
        {
            throw new IllegalArgumentException("Los turnos deben ser a partir del 04-03-2025");
        }
    
    }

    private void validarCorreo(String correo)
    {
        String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        if (correo == null || correo.isEmpty() || !EMAIL_PATTERN.matcher(correo).matches()) {
            throw new IllegalArgumentException("El correo electrónico proporcionado es inválido");
        }
    } 

    public void existeCodigo(Integer codigo) {
        if(!turnoRepository.existsById(codigo) || codigo == null)
        {
            throw new IllegalArgumentException("El codigo no es correcto");
        }
    }

      // Método para generar un código aleatorio de 4 dígitos
    private Integer generarCodigoAleatorio() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
    }
}
