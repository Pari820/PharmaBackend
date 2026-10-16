package pe.edu.upeu.PharmaBackend.exception;

public class RecursosNoEncontradoException extends RuntimeException {
    public RecursosNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}