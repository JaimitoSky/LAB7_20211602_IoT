package modelos.validacionservice.controller;


import modelos.validacionservice.dto.ValidacionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validar")
public class ValidacionController {

    @GetMapping("/dni/{dni}")
    public ValidacionResponse validarDni(@PathVariable String dni) {
        if (dni != null && dni.length() == 8 && dni.matches("\\d+")) {
            return new ValidacionResponse(true, "OK");
        }
        return new ValidacionResponse(false, "El DNI no tiene un formato válido");
    }

    @GetMapping("/correo/{correo}")
    public ValidacionResponse validarCorreo(@PathVariable String correo) {
        if (correo != null && correo.endsWith("@pucp.edu.pe")) {
            return new ValidacionResponse(true, "OK");
        }
        return new ValidacionResponse(false, "El correo debe ser @pucp.edu.pe");
    }
}
