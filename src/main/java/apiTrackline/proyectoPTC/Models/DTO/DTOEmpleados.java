package apiTrackline.proyectoPTC.Models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class DTOEmpleados {

    private Long idEmpleado;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo 'nombre' solo debe contener letras y espacios", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    @Size(max = 100, message = "El máximo de caracteres para el campo 'nombre' es 100", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotBlank(message = "El campo 'nombre' es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private String nombre;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo 'apellido' solo debe contener letras y espacios", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    @Size(max = 100, message = "El máximo de caracteres para el campo 'apellido' es 100", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String apellido;

    @Size(max = 15, message = "El máximo de caracteres para el campo 'telefono' es 15", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String telefono;

    @Email
    @Pattern(
            regexp = "^[\\w.%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$",
            message = "El correo solo puede ser de dominio @gmail.com, @yahoo.com o @outlook.com",
            groups = {OnCreate.class, OnUpdate.class, OnPatch.class}
    )
    @Size(max = 100, message = "El máximo de caracteres para el campo 'correo' es 100", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String correo;

    @JsonProperty("NIT")
    @Size(max = 20, message = "El máximo de caracteres para el campo 'NIT' es 20",groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String nit;

    //Atributos para la llave foranea de la tabla usuarios
    //Empleados ----> Usuarios
    @Positive(message = "El id de usuario no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idUsuario;
    private String usuario;
    private String contrasenia;
    //Usuarios -----> Rol
    private Long idRol;
    private String nombreRol;

    public interface OnCreate{}
    public interface OnUpdate{}
    public interface OnPatch{}
}
