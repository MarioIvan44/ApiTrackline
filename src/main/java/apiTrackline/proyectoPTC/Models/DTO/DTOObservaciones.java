package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOObservaciones {

    private Long idObservaciones;

    @NotNull(message = "El ID del selectivo es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id del selectivo no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idSelectivo;
    private String colorSelectivo;

    @Size(max = 50, message = "El campo 'observaciones' no debe exceder los 50 caracteres.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String observaciones;

    // Grupos de validación
    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
