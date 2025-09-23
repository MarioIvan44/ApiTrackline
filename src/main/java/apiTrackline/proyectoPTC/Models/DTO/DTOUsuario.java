package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DTOUsuario {

    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, message = "El máximo de caracteres para el nombre de usuario es 50", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String usuario;

    @NotBlank(message = "La contraseña del usuario no puede estar vacía", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 8, max = 16, message = "El mínimo de caracteres para la contraseña es 8 y el máximo de caracteres es 16", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String contrasenia;

    @NotNull(message = "El rol del usuario no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id de rol no puede ser negativo", groups = {OnPatch.class, OnUpdate.class, OnCreate.class})
    private Long idRol;
    private String rol;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
