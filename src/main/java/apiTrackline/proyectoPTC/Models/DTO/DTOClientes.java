package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOClientes {

    @NotBlank(message = "El campo 'clienteNit' no puede estar vacío", groups = {OnCreate.class})
    @Size(max = 20, message = "El campo 'clienteNit' debe tener como máximo 20 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    private String clienteNit;


    @Size(max = 100, message = "El campo 'nombre' debe tener como máximo 100 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    @NotBlank(message = "El campo 'nombre' no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    private String nombre;


    @Size(max = 100, message = "El campo 'apellido' debe tener como máximo 100 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    @NotBlank(message = "El nombre no puede estar vacio" , groups = {OnCreate.class, OnUpdate.class})
    private String apellido;

    @Size(max = 15, message = "El campo 'telefono' debe tener como máximo 15 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    private String telefono;




    @Email
    @Pattern(
            regexp = "^[\\w.%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$",
            message = "El correo solo puede ser de dominio @gmail.com, @yahoo.com o @outlook.com",
            groups = {OnCreate.class, OnUpdate.class, OnPatch.class}
    )
    @Size(max = 100, message = "El campo 'correo' debe tener como máximo 100 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    private String correo;

    @Size(max = 50, message = "El campo 'codEmpresa' debe tener como máximo 50 caracteres", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    private String codEmpresa;
    //Clientes ----> Usuarios
    //Atributos para la llave foranea de la tabla usuarios
    @Positive(message = "El id de usuario no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idUsuario;
    private String usuario;
    private String contrasenia;
    //Usuarios -----> Rol
    private Long idRol;
    private String nombreRol;

    //Clientes ------> TipoCliente
    @Positive(message = "El id del tipo cliente no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(message = "El id tipo cliente no puede ser nulo", groups = {OnCreate.class, OnUpdate.class})
    private Long idTipoCliente;
    private String tipo;


    public interface OnCreate{}
    public interface OnUpdate{}
    public interface OnPatch{}
}
