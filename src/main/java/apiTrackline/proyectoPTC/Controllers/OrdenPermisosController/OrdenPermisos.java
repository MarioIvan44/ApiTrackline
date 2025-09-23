package apiTrackline.proyectoPTC.Controllers.OrdenPermisosController;

import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenPermisos;
import apiTrackline.proyectoPTC.Services.OrdenPermisosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apiOrdenPermisos")
@CrossOrigin
@Slf4j
public class OrdenPermisos {

    @Autowired
    private OrdenPermisosService service;

    // MÉTODO GET POR ID
    @GetMapping("/obtenerOrdenPermisoPorId/{id}")
    public ResponseEntity<?> obtenerOrdenPermisoPorId(@PathVariable Long id) {
        try {
            DTOOrdenPermisos orden = service.buscarOrdenPermisoPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", orden
            ));
        } catch (ExceptionOrdenPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al buscar la ordenPermiso por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar ordenPermiso"
            ));
        }
    }

    // MÉTODO GET (con paginación)
    @GetMapping("/obtenerDatos")
    public ResponseEntity<?> getOrdenesPermisos(
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
                    "message", "El tamaño de página debe estar entre 1 y 50"
            ));
        }

        Page<DTOOrdenPermisos> ordenes = service.obtenerOrdenesPermisos(page, size);
        if (ordenes == null || ordenes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay órdenes de permisos registradas"
            ));
        }
        return ResponseEntity.ok(ordenes);
    }

    // MÉTODO POST
    @PostMapping("/agregarOrdenPermiso")
    public ResponseEntity<?> agregarOrdenPermiso(@Valid @RequestBody DTOOrdenPermisos dto) {
        try {
            DTOOrdenPermisos respuesta = service.agregarOrdenPermiso(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "OrdenPermiso creada correctamente"
            ));
        } catch (ExceptionPermisoNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionOrdenPermisoNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar la ordenPermiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al registrar la ordenPermiso"
            ));
        }
    }

    // MÉTODO PUT
    @PutMapping("/actualizarOrdenPermiso/{id}")
    public ResponseEntity<?> actualizarOrdenPermiso(@PathVariable Long id, @Valid @RequestBody DTOOrdenPermisos dto) {
        try {
            DTOOrdenPermisos actualizada = service.actualizarOrdenPermiso(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizada
            ));
        } catch (ExceptionOrdenPermisoNoEncontrado | ExceptionPermisoNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar la ordenPermiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar ordenPermiso"
            ));
        }
    }

    // MÉTODO PATCH
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteOrdenPermiso(@PathVariable Long id, @Valid @RequestBody DTOOrdenPermisos dto) {
        try {
            DTOOrdenPermisos actualizada = service.patchOrdenPermiso(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizada
            ));
        } catch (ExceptionOrdenPermisoNoEncontrado | ExceptionPermisoNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
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
            log.error("Error inesperado al actualizar parcialmente la ordenPermiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar parcialmente la ordenPermiso"
            ));
        }
    }

    // MÉTODO DELETE
    @DeleteMapping("/eliminarOrdenPermiso/{id}")
    public ResponseEntity<?> eliminarOrdenPermiso(@PathVariable Long id) {
        try {
            service.eliminarOrdenPermiso(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "La ordenPermiso ha sido eliminada correctamente"
            ));
        } catch (ExceptionOrdenPermisoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionOrdenPermisoRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "La ordenPermiso no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar la ordenPermiso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar"
            ));
        }
    }
}
