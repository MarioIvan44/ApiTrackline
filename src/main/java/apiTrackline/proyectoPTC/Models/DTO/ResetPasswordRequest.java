package apiTrackline.proyectoPTC.Models.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResetPasswordRequest {
    public String getToken() {
        return Token;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

    private String Token;
    private String nuevaContrasenia;
}