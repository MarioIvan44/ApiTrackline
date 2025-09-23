package apiTrackline.proyectoPTC.Controllers.UsuarioController;

import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionRolNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioDuplicado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioNoRegistrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Services.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiUsuario")
@CrossOrigin
@Slf4j
public class Usuario {

    @Autowired
    private UsuarioService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiUsuario/obtenerUsuarioPorId/{id}
    @GetMapping("/obtenerUsuarioPorId/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            DTOUsuario usuario = service.buscarUsuarioPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", usuario
            ));
        } catch (ExceptionUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener usuario por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar usuario por ID"
            ));
        }
    }

    // MÉTODO GET (paginación)
    // RUTA: localhost:8080/apiUsuario/dataUsuario
    @GetMapping("/dataUsuario")
    public ResponseEntity<?> getUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (page < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El número de página no puede ser negativo"
            ));
        }

        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<DTOUsuario> usuarios = service.obtenerUsuarios(page, size);
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay usuarios registrados"
            ));
        }
        return ResponseEntity.ok(usuarios);
    }

    // MÉTODO POST
    // RUTA: localhost:8080/apiUsuario/postUsuario
    @PostMapping("/postUsuario")
    public ResponseEntity<?> agregarUsuario(@Validated(DTOUsuario.OnCreate.class) @RequestBody DTOUsuario json) {
        try {
            DTOUsuario respuesta = service.agregarUsuario(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Usuario creado correctamente"
            ));
        } catch (ExceptionUsuarioDuplicado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionRolNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionUsuarioNoRegistrado e){
            log.error("Error inesperado al agregar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar usuario"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiUsuario/updateUsuario/{id}
    @PutMapping("/updateUsuario/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Validated(DTOUsuario.OnUpdate.class) @RequestBody DTOUsuario dtoUsuario) {
        try {
            DTOUsuario actualizado = service.actualizarUsuario(id, dtoUsuario);
            return ResponseEntity.ok(actualizado);
        } catch (ExceptionUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionUsuarioDuplicado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionRolNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar usuario"
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiUsuario/patchUsuario/{id}
    @PatchMapping("/patchUsuario/{id}")
    public ResponseEntity<?> actualizarParcialmenteUsuario(@PathVariable Long id, @Validated(DTOUsuario.OnPatch.class) @RequestBody DTOUsuario json) {
        try {
            DTOUsuario actualizado = service.patchUsuario(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionRolNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionUsuarioDuplicado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al editar parcialmente"
            ));
        }
    }

    @DeleteMapping("/deleteUsuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            service.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El usuario ha sido eliminado correctamente"
            ));
        } catch (ExceptionUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",   // ✅ corregido
                    "message", e.getMessage()
            ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "Usuario con el ID especificado no existe"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar usuario"
            ));
        }
    }

    // MÉTODO GET POR NOMBRE
// RUTA: localhost:8080/apiUsuario/buscarUsuarioPorNombre/{usuario}
    @GetMapping("/buscarUsuarioPorNombre/{usuario}")
    public ResponseEntity<?> buscarUsuarioPorNombre(@PathVariable String usuario) {
        try {
            DTOUsuario dtoUsuario = service.buscarUsuarioPorNombre(usuario);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", dtoUsuario
            ));
        } catch (ExceptionUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al buscar usuario por nombre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar usuario por nombre"
            ));
        }
    }

}
