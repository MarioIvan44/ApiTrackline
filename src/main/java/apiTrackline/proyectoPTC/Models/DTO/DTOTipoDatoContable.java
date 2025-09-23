package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOTipoDatoContable {

    private Long idTipoDatoContable;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La primera modalidad solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotBlank(message = "El nombre es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 30, message = "El nombre no debe exceder los 30 caracteres.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String nombre;

    @Positive(message = "El precio unitario debe de ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Double precioUni;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
