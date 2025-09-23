package apiTrackline.proyectoPTC.Controllers.PermisosController;

import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOPermisos;
import apiTrackline.proyectoPTC.Services.PermisosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apiPermisos")
@CrossOrigin
@Slf4j
public class Permisos {
    @Autowired
    private PermisosService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiPermiso/obtenerPermisoPorId/{id}
    @GetMapping("/obtenerPermisoPorId/{id}")
    public ResponseEntity<?> obtenerPermisoPorId(@PathVariable Long id) {
        try {
            DTOPermisos permiso = service.buscarPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", permiso
            ));
        } catch (ExceptionPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener permiso por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar permiso por ID"
            ));
        }
    }

    // MÉTODO GET - Consultar todos los permisos con paginación
    // RUTA: localhost:8080/apiPermiso/datosPermiso
    @GetMapping("/datosPermiso")
    public ResponseEntity<?> getPermisos(
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

        Page<DTOPermisos> permisos = service.obtenerPermisos(page, size);
        if (permisos == null || permisos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay permisos registrados"
            ));
        }
        return ResponseEntity.ok(permisos);
    }

    // MÉTODO POST - Crear nuevo permiso
    // RUTA: localhost:8080/apiPermiso/agregarPermiso
    @PostMapping("/agregarPermiso")
    public ResponseEntity<?> agregarPermiso(@Validated(DTOPermisos.OnCreate.class) @RequestBody DTOPermisos json) {
        try {
            DTOPermisos respuesta = service.agregarPermiso(json);
            if (respuesta == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verifique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Permiso creado correctamente"
            ));
        } catch (ExceptionPermisoNoRegistrado e) {
            log.error("Error inesperado al agregar permiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }catch (Exception e) {
            log.error("Error inesperado al agregar permiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar permiso"
            ));
        }
    }

    // MÉTODO PUT - Actualizar permiso completo
    // RUTA: localhost:8080/apiPermiso/actualizarPermiso/{id}
    @PutMapping("/actualizarPermiso/{id}")
    public ResponseEntity<?> actualizarPermiso(@PathVariable Long id, @Validated(DTOPermisos.OnUpdate.class) @RequestBody DTOPermisos dtoPermiso) {
        try {
            DTOPermisos permiso = service.actualizarPermiso(id, dtoPermiso);
            return ResponseEntity.ok(permiso);
        } catch (ExceptionPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar permiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar el permiso"
            ));
        }
    }

    // MÉTODO PATCH - Actualizar parcialmente permiso
    // RUTA: localhost:8080/apiPermiso/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmentePermiso(@PathVariable Long id, @Validated(DTOPermisos.OnPatch.class) @RequestBody DTOPermisos json) {
        try {
            DTOPermisos actualizado = service.patchPermiso(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente el permiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al editar parcialmente"
            ));
        }
    }

    // MÉTODO DELETE - Eliminar permiso
    // RUTA: localhost:8080/apiPermiso/eliminarPermiso/{id}
    @DeleteMapping("/eliminarPermiso/{id}")
    public ResponseEntity<?> eliminarPermiso(@PathVariable Long id) {
        try {
            service.eliminarPermiso(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El permiso ha sido eliminado correctamente"
            ));
        } catch (ExceptionPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status:", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionPermisoRelacionado e) {
            log.error("Error al eliminar permiso", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El permiso no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar permiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar"
            ));
        }
    }
}
