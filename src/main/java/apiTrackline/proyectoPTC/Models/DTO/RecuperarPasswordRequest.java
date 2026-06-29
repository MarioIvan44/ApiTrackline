package apiTrackline.proyectoPTC.Models.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class RecuperarPasswordRequest {
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    private String correo;
}