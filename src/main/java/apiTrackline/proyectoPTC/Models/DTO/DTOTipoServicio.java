package apiTrackline.proyectoPTC.Models.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@EqualsAndHashCode
public class DTOTipoServicio {
    private Long idTipoServicio;

    private String tipoServicio;
}
