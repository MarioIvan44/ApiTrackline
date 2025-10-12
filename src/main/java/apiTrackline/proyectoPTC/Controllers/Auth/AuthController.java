package apiTrackline.proyectoPTC.Controllers.Auth;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Models.DTO.RecuperarPasswordRequest;
import apiTrackline.proyectoPTC.Models.DTO.ResetPasswordRequest;
import apiTrackline.proyectoPTC.Services.AuthService;
import apiTrackline.proyectoPTC.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<String> login(@RequestBody DTOUsuario data, HttpServletResponse response) {
        if (data.getUsuario() == null || data.getUsuario().isBlank() ||
                data.getContrasenia() == null || data.getContrasenia().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        if (service.Login(data.getUsuario(), data.getContrasenia())) {
            addTokenCookie(response, data.getUsuario()); //
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    /**
     * Se genera el token y se guarda en la Cookie
     * @param response
     * @param
     */
    private void addTokenCookie(HttpServletResponse response, String correo) {
        Optional<UsuarioEntity> userOpt = service.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UsuarioEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIdUsuario()),
                    user.getUsuario(),
                    user.getRol().getRol()
            );

            String cookieValue = String.format(
                    "authToken=%s; " +
                            "Path=/; " +
                            "HttpOnly; " +
                            "Secure; " +
                            "SameSite=None; " +
                            "MaxAge=86400; " +
                            "Domain=apitrackline-3047cf7af332.herokuapp.com",
                    token
            );
            response.addHeader("Set-Cookie", cookieValue);
            response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "No autenticado"
                        ));
            }

            // Manejar diferentes tipos de Principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
            } else {
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }

            Optional<UsuarioEntity> userOpt = service.obtenerUsuario(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "Usuario no encontrado"
                        ));
            }

            UsuarioEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", user.getIdUsuario(),
                            "usuario", user.getUsuario(),
                            "rol", user.getRol().getRol(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            //log.error("Error en /me endpoint: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }

    // Endpoint para pedir recuperación
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperarPassword(@RequestBody RecuperarPasswordRequest request) {
        Optional<UsuarioEntity> userOpt = service.obtenerUsuarioPorCorreo(request.getCorreo());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No existe ningún usuario con ese correo"));
        }

        UsuarioEntity user = userOpt.get();

        // Generar token de reset
        String resetToken = jwtUtils.createResetToken(user.getUsuario());
        String link = "https://apitrackline-3047cf7af332.herokuapp.com/reset-password?token=" + resetToken;

        // Enviar correo con link
        service.enviarCorreo(request.getCorreo(), "Recupera tu contraseña",
                "Haz clic en este enlace para restablecer tu contraseña: " + link);

        return ResponseEntity.ok(Map.of("message", "Se envió un correo con las instrucciones"));
    }


    // Endpoint para cambiar la contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String username = jwtUtils.validateResetToken(request.getToken());
            Optional<UsuarioEntity> userOpt = service.obtenerUsuario(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Usuario no encontrado"));
            }

            UsuarioEntity user = userOpt.get();
            service.actualizarPassword(user, request.getNuevaContrasenia());

            return ResponseEntity.ok(Map.of("message", "Contraseña cambiada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token inválido o expirado"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Crear cookie de expiración con SameSite=None
        String cookieValue = "authToken=; Path=/; HttpOnly; Secure; SameSite=None; MaxAge=0; Domain=apitrackline-3047cf7af332.herokuapp.com";

        response.addHeader("Set-Cookie", cookieValue);
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");

        // También agregar headers CORS para la respuesta
        String origin = request.getHeader("Origin");
        if (origin != null &&
                (origin.contains("localhost") || origin.contains("herokuapp.com"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        return ResponseEntity.ok()
                .body("Logout exitoso");
    }
}