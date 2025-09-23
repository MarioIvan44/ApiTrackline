package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode
public class DTOTipoCliente {

    private Long idTipoCliente;

    @NotBlank(message = "El tipo de cliente no puede estar en blanco")
    @Size(message = "El número de caracter máximo para el tipo de cliente es 20")
    private String tipo;
}
