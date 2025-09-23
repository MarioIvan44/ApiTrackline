package apiTrackline.proyectoPTC.Models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter @Setter @EqualsAndHashCode @ToString

public class DTOAduana {
    private Long idAduana;

    //Tipo de servicio (Llave foranea)
    @NotNull(message = "El id del tipo de servicio es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id del tipo de servicio no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTipoServicio;
    private String nombreTipoServicio;

    @JsonProperty("DM")
    @Size(max = 50, message = "El máximo de caracteres para DM es 50", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    @NotBlank(message = "El DM es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private String DM;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La primera modalidad solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 10, message = "El máximo de caracteres para la primera modalidad es 10", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String primeraModalidad;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La segunda modalidad solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 10, message = "El máximo de caracteres para la segunda modalidad es 10", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String segundaModalidad;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El digitador solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 30, message = "El máximo de caracteres para el digitador es 30", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotBlank(message = "El campo de digitador es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private String digitador;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El tramitador solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 50, message = "El máximo de caracteres para el tramitador es 50", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String tramitador;

    // Grupos de validación
    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}