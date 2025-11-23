package modelos.registroservice.controller;

import modelos.registroservice.client.ValidacionClient;
import modelos.registroservice.dto.RegistroRequest;
import modelos.registroservice.dto.ValidacionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/registro")
public class RegistroController {

    private final ValidacionClient client;

    public RegistroController(ValidacionClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest req) {

        ValidacionResponse dniRes = client.validarDni(req.dni);
        if (!dniRes.ok) {
            return ResponseEntity.badRequest().body(dniRes.mensaje);
        }

        ValidacionResponse correoRes = client.validarCorreo(req.correo);
        if (!correoRes.ok) {
            return ResponseEntity.badRequest().body(correoRes.mensaje);
        }

        return ResponseEntity.ok("OK");
    }
}
