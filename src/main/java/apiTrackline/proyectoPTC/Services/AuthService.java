package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Config.Argon2.Argon2Password;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
import apiTrackline.proyectoPTC.Utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private Argon2Password argon2Password;

    public boolean Login(String Usuario, String contrasena){
        Argon2Password objHash = new Argon2Password();
        Optional<UsuarioEntity> list = repo.findByUsuario(Usuario).stream().findFirst();
        if (list.isPresent()){
            UsuarioEntity usuario = list.get();
            String nombreTipoUsuario = usuario.getRol().getRol();
            System.out.println("Usuario ID encontrado: " + usuario.getIdUsuario() +
                    ", Usuario: " + usuario.getUsuario() +
                    ", rol: " + nombreTipoUsuario);
            return objHash.VerifyPassword(usuario.getContrasenia(), contrasena);
        }
        return false;
    }

    public Optional<UsuarioEntity> obtenerUsuario(String usuario){
        Optional<UsuarioEntity> userOpt = repo.findByUsuario(usuario);
        return (userOpt != null) ? userOpt : null;
    }

    // AuthService.java
    public void actualizarPassword(UsuarioEntity user, String nuevaContrasenia) {
        String hash = argon2Password.EncryptPassword(nuevaContrasenia);
        user.setContrasenia(hash);
        repo.save(user);
    }

    // Enviar correo usando Gmail
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        // Configuración básica JavaMailSender
        // Asegúrate de tener dependencias y propiedades en application.properties
        // spring.mail.host=smtp.gmail.com
        // spring.mail.port=587
        // spring.mail.username=TracklineSV@gmail.com
        // spring.mail.password=Trackline1768$
    }

}