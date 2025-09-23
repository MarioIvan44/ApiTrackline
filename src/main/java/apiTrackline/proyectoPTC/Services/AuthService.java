package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Config.Argon2.Argon2Password;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

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

}
