package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DTOTipoFinanciamientos {

    private Long idTipoFinanciamiento;

    @NotBlank(message = "El nombre del tipo de financiamiento no puede estar vacío")
    @Size(min = 4, max = 30,message = "El nombre solo puede contener 4 caracteres como mínimo y 30 como máximo")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")
    private String nombre;

    @Positive(message = "El precio unitario no puede ser negativo")
    private Double precioUni;
}