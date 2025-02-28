package com.turnos.turnos.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.turnos.turnos.Model.Turno;
import com.turnos.turnos.Repository.TurnoRepository;
import com.turnos.turnos.Ser.EmailSer;
import com.turnos.turnos.Ser.TurnoSer;
import com.turnos.turnos.TurnosDTO.Propiedades;



@Controller
@RequestMapping("/") 
public class TurnoController {
    
    @Autowired
    private EmailSer emailService;
    
    @Autowired
    private TurnoSer turnoSer;
    
    @Autowired
    private TurnoRepository turnoRes;
    
    private Propiedades propiedades; 

    @GetMapping
    public String inicio(Model model) { 
      propiedades = new Propiedades();
        List<LocalTime> horasOcupadas = turnoRes.findHorasByFecha(LocalDate.now());
        List<LocalTime> horasDisponibles = generarHorariosDisponibles(horasOcupadas);
        List<String> trabajos = obtenerListaTrabajos();
        
        model.addAttribute("trabajos", trabajos);
        model.addAttribute("horasDisponibles", horasDisponibles);
        model.addAttribute("turno", new Turno());
        model.addAttribute("titulo", propiedades.getTitulo());
        model.addAllAttributes(propiedades.getArticulos());
        model.addAttribute("secciones", propiedades.getSecciones());
        
        return "index";
    }

    @PostMapping
    public String procesarFormulario(Turno turno,RedirectAttributes redirectAttributes) {
        try {
            turnoSer.guardar(turno);
            //Mensaje para propietario
            String mensajeP = "Hola Day soy tu Bot de turnos!🤖 \n Tenes un turno para: "+ turno.getNombre()+" \n su correo es: "+ turno.getCorreo()+"\n se realiza un: "+ turno.getSer()
            +"\n el dia "+ turno.getFecha()+"\n a las: "+turno.getHora();
            emailService.sendSimpleEmail("dayrabarros2002@gmail.com", "Turno", mensajeP);
            //Mensaje para el cliente
            String mensajeC = "Se agendo el turno para el dia "+turno.getFecha()+" a las "+turno.getHora()
            +"\n Si necesitas cambiar el tunro o cancelar, se puede hacer con el codigo: #"+turno.getId()
            +"\n gracias por confiar en LOVERS";
            emailService.sendSimpleEmail(turno.getCorreo(), "Turno", mensajeC);

            redirectAttributes.addFlashAttribute("success", "Turno registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado, por favor intenta nuevamente.");
        }
        return "redirect:/#TURNO"; // Redirección ajustada al prefijo
    }
    @GetMapping("/error")
    public String manejarError() {
        return "error"; // Nombre del archivo HTML de tu página de error
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/turno/lista")
    public String listarTurnos(Model model) {
        model.addAttribute("turnos", turnoSer.listarTurnos());
        return "turno-lista";
    }

    @PostMapping("/editar")
    public String procesarId(@RequestParam(name = "id", defaultValue = "0") Integer id ,RedirectAttributes redirectAttributes)
    {
        if (id == 0) {
            redirectAttributes.addFlashAttribute("error", "El código no puede estar vacío.");
            return "redirect:/#TURNO";
        }
        try{
            turnoSer.existeCodigo(id);
            redirectAttributes.addFlashAttribute("success", "Turno registrado exitosamente.");
            return "redirect:/editar/"+id;
        } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/#TURNO"; // Redirige a la sección de turnos en caso de error
        }
      
    }

    @GetMapping("/editar/{id}")
    public String editarTurno(@PathVariable Integer id, Model model) {
        Turno turno = turnoSer.obtenerPorId(id);
        if (turno != null) {
            List<LocalTime> horasOcupadas = turnoRes.findHorasByFecha(LocalDate.now());
            List<LocalTime> horasDisponibles = generarHorariosDisponibles(horasOcupadas);
            List<String> trabajos = obtenerListaTrabajos();
            model.addAttribute("trabajos", trabajos);
            model.addAttribute("horasDisponibles", horasDisponibles);
            model.addAttribute("turno", turno);

            return "turno-form";
        }
        return "redirect:/#TURNO"; // Redirección ajustada al prefijo
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTurno(@PathVariable Integer id,Turno turno ,RedirectAttributes redirectAttributes) {
        turno = turnoSer.obtenerPorId(id);
        turnoSer.Eliminar(id);

        //mensaje de modificacion
        String mensajeC =  "Hola Day soy tu Bot de turnos!🤖 \n Se elimino el turno para: "+ turno.getNombre()+
        "\n se libero el dia "+ turno.getFecha()+"\n a las: "+turno.getHora();
        emailService.sendSimpleEmail("dayrabarros2002@gmail.com", "Turno modificado", mensajeC);

        redirectAttributes.addFlashAttribute("success", "Turno cancelado exitosamente.");
        return "redirect:/"; // Redirección ajustada al prefijo
    }


    @GetMapping("/turno/editar/{id}")
    public String editarTurnoAdmin(@PathVariable Integer id, Model model) {
        Turno turno = turnoSer.obtenerPorId(id);
        if (turno != null) {
            List<LocalTime> horasOcupadas = turnoRes.findHorasByFecha(LocalDate.now());
            List<LocalTime> horasDisponibles = generarHorariosDisponibles(horasOcupadas);
            List<String> trabajos = obtenerListaTrabajos();
            model.addAttribute("trabajos", trabajos);
            model.addAttribute("horasDisponibles", horasDisponibles);
            model.addAttribute("turno", turno);

            return "turno-form";
        }
        return "redirect:/turno/lista"; // Redirección ajustada al prefijo
    }

    @GetMapping("/turno/eliminar/{id}")
    public String eliminarTurnoAdmin(@PathVariable Integer id,Turno turno) {
        turno = turnoSer.obtenerPorId(id);
        turnoSer.Eliminar(id);

        //mensaje de eliminacion
        String mensajeC =  "Hola Day soy tu Bot de turnos!🤖 \n Se elimino el turno para: "+ turno.getNombre()+
        "\n se libero el dia "+ turno.getFecha()+"\n a las: "+turno.getHora();
        emailService.sendSimpleEmail("dayrabarros2002@gmail.com", "Turno moficado", mensajeC);

        return "redirect:/turno/lista"; // Redirección ajustada al prefijo
    }

    private List<LocalTime> generarHorariosDisponibles(List<LocalTime> horasOcupadas) {
        List<LocalTime> horasDisponibles = new ArrayList<>();
        LocalTime inicio = LocalTime.of(13, 0);
        LocalTime fin = LocalTime.of(19, 0);
        
        while (!inicio.isAfter(fin)) {
            if (!horasOcupadas.contains(inicio)) {
                horasDisponibles.add(inicio);
            }
            inicio = inicio.plusHours(1);
        }
        return horasDisponibles;
    }

    private List<String> obtenerListaTrabajos() {
        List<String> trabajos = new ArrayList<>();
        trabajos.add("SEMIPERMANENTE");
        trabajos.add("CAPPING");
        trabajos.add("SOFTGEL LISO");
        trabajos.add("SOFTGEL + DISEÑO SIMPLE");
        trabajos.add("SOFTGEL + APLIQUES");
        trabajos.add("SOFTGEL + FULL");
        trabajos.add("PRESSON");
        trabajos.add("PRESSON FULL SET");
        return trabajos;
    }
}
