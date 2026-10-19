package pe.edu.upeu.PharmaBackend.dto.auth;

import jakarta.validation.constraints.NotNull;

public class EstadoUsuarioRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}