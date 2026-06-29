package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Config.Argon2.Argon2Password;
import apiTrackline.proyectoPTC.Entities.ClientesEntity;
import apiTrackline.proyectoPTC.Entities.TransporteEntity;
import apiTrackline.proyectoPTC.Entities.TransportistaEntity;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Repositories.ClientesRepository;
import apiTrackline.proyectoPTC.Repositories.TransporteRepository;
import apiTrackline.proyectoPTC.Repositories.TransportistaRepository;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
import apiTrackline.proyectoPTC.Utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private Argon2Password argon2Password;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private TransportistaRepository transportistaRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

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

    public Optional<UsuarioEntity> obtenerUsuarioPorCorreo(String correo) {
        // Buscar cliente
        Optional<ClientesEntity> clienteOpt = clientesRepository.findByCorreo(correo);
        if (clienteOpt.isPresent()) {
            return Optional.ofNullable(clienteOpt.get().getUsuario());
        }

        // Buscar transportista
        Optional<TransportistaEntity> transpOpt = transportistaRepository.findByCorreo(correo);
        if (transpOpt.isPresent()) {
            return Optional.ofNullable(transpOpt.get().getUsuarioT());
        }

        // Buscar transporte a través del transportista
        Optional<TransporteEntity> transporteOpt = transporteRepository.findByTransportistaCorreo(correo);
        if (transporteOpt.isPresent()) {
            return Optional.ofNullable(transporteOpt.get().getTransportista().getUsuarioT());
        }

        return Optional.empty();
    }

    // AuthService.java
    public void actualizarPassword(UsuarioEntity user, String nuevaContrasenia) {
        String hash = argon2Password.EncryptPassword(nuevaContrasenia);
        user.setContrasenia(hash);
        repo.save(user);
    }

    // Enviar correo usando Gmail
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(remitente);
            email.setTo(destinatario);
            email.setSubject(asunto);
            email.setText(mensaje);
            mailSender.send(email);

            System.out.println("Correo enviado correctamente a: " + destinatario);
        } catch (Exception e) {
            System.err.println(" Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

}