package modelos.validacionservice.dto;


public class ValidacionResponse {

    public boolean ok;
    public String mensaje;

    public ValidacionResponse() {
    }

    public ValidacionResponse(boolean ok, String mensaje) {
        this.ok = ok;
        this.mensaje = mensaje;
    }
}
