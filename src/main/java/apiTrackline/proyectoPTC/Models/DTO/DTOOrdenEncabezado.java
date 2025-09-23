package apiTrackline.proyectoPTC.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.annotation.*;
import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DTOOrdenEncabezado {

    private Long IdOrdenEncabezado;
    //@PastOrPresent(message = "La fecha solo puede ")

    @NotNull(message = "La fecha no puede estar en blanco.", groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;


    @NotBlank(message = "El encargado no puede estar en blanco.", groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo de encargado solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 100, message = "El campo de encargado tiene un máximo de 100 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String encargado;

    @Size(message = "El campo de referencia tiene un máximo de 20 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String referencia;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo de importador solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotBlank(message = "El importador no puede estar en blanco.", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, message = "El campo para el importador tiene un máximo de 100 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String importador;

    @JsonProperty("NIT")
    @Size(max = 18, message = "El campo para el NIT(1) tiene un máximo de 18 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String NIT;

    @Size(max = 10, message = "El campo para el registro de IVA(1) tiene un máximo de 10 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String registroIVA;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo de 'facturaA' solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 20, message = "El campo 'facturaA' tiene un máximo de 20 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String facturaA;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El campo 'encargado2' solo debe contener letras y espacios", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 100, message = "El campo de encargado(2) tiene un máximo de 100 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String encargado2;

    @JsonProperty("NIT2")
    @Size(max = 18, message = "El campo para el NIT(2) tiene un máximo de 18 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String NIT2;

    @Size(max = 10, message = "El campo para el registro de IVA(2) tiene un máximo de 10 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String registroIVA2;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
