package com.turnos.turnos.ControllerAd;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        // Agrega el mensaje de error al modelo para mostrarlo en la vista
        model.addAttribute("error", ex.getMessage());
        return "/"; // Devuelve el nombre de la vista del formulario
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        // Maneja cualquier otra excepción y muestra un mensaje genérico
        model.addAttribute("error", "Ocurrió un error inesperado. Por favor, intenta nuevamente.");
        return "/"; // Devuelve el nombre de la vista del formulario
    }
}

