package pe.edu.upeu.PharmaBackend.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class AsignarRolesRequestDTO {

    @NotEmpty(message = "Debe asignar al menos un rol")
    private Set<@NotNull(message = "El rol no puede ser nulo") Long> rolIds;

    public Set<Long> getRolIds() {
        return rolIds;
    }

    public void setRolIds(Set<Long> rolIds) {
        this.rolIds = rolIds;
    }
}