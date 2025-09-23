package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOPermisos {

    private Long idPermiso;

    @NotBlank(message = "El nombre del permiso es obligatorio", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(min = 3, max = 20, message = "El nombre del permiso debe de contener como mínimo 3 caracteres y 20 como máximo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String nombrePermiso;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch{}
}
