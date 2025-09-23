package apiTrackline.proyectoPTC.Controllers.ObservacionesController;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionRelacionada;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionSelectivoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOObservaciones;
import apiTrackline.proyectoPTC.Services.ObservacionesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apiObservaciones")
@CrossOrigin
@Slf4j
public class Observaciones {
    @Autowired
    private ObservacionesService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiObservaciones/obtenerObservacionPorId/{id}
    @GetMapping("/obtenerObservacionPorId/{id}")
    public ResponseEntity<?> obtenerObservacionPorId(@PathVariable Long id) {
        try {
            DTOObservaciones observacion = service.buscarObservacionPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", observacion
            ));
        } catch (ExceptionObservacionNoEncontrada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar observación por ID"
            ));
        }
    }

    // MÉTODO GET (con paginación)
    // RUTA: localhost:8080/apiObservaciones/datosObservaciones
    @GetMapping("/datosObservaciones")
    public ResponseEntity<?> getObservaciones(
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

        Page<DTOObservaciones> observaciones = service.obtenerObservaciones(page, size);
        if (observaciones == null || observaciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay observaciones registradas"
            ));
        }
        return ResponseEntity.ok(observaciones);
    }

    // MÉTODO POST
    // RUTA: localhost:8080/apiObservaciones/agregarObservacion
    @PostMapping("/agregarObservacion")
    public ResponseEntity<?> agregarObservacion(@Validated(DTOObservaciones.OnCreate.class) @RequestBody DTOObservaciones dto) {
        try {
            DTOObservaciones respuesta = service.agregarObservacion(dto);
            if (respuesta == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verifique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Observación creada correctamente"
            ));
        } catch (ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar la observación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar observación"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiObservaciones/actualizarObservacion/{id}
    @PutMapping("/actualizarObservacion/{id}")
    public ResponseEntity<?> actualizarObservacion(@PathVariable Long id, @Validated(DTOObservaciones.OnUpdate.class) @RequestBody DTOObservaciones dto) {
        try {
            DTOObservaciones actualizada = service.actualizarObservacion(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (ExceptionObservacionNoEncontrada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al actualizar la observación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar la observación"
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiObservaciones/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteObservacion(@PathVariable Long id, @Validated(DTOObservaciones.OnPatch.class) @RequestBody DTOObservaciones dto) {
        try {
            DTOObservaciones actualizada = service.patchObservacion(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizada
            ));
        } catch (ExceptionObservacionNoEncontrada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        }catch (Exception e){
            log.error("Error inesperado al actualizar parcialmente la observación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar parcialmente la observación"
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: localhost:8080/apiObservaciones/eliminarObservacion/{id}
    @DeleteMapping("/eliminarObservacion/{id}")
    public ResponseEntity<?> eliminarObservacion(@PathVariable Long id) {
        try {
            service.eliminarObservacion(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "La observación ha sido eliminada correctamente"
            ));
        } catch (ExceptionObservacionNoEncontrada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionObservacionRelacionada e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "La observación no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e){
            log.error("Error inesperado al agregar la observación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar"
            ));
        }
    }
}
