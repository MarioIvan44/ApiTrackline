package apiTrackline.proyectoPTC.Controllers.RecoleccionController;

import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionRelacionada;
import apiTrackline.proyectoPTC.Models.DTO.DTORecoleccion;
import apiTrackline.proyectoPTC.Services.RecoleccionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/apiRecoleccion")
public class Recoleccion {

    @Autowired
    private RecoleccionService service;

    // MÉTODO GET (PAGINADO)
    // RUTA: localhost:8080/apiRecoleccion/obtenerDatosPaginados?page=0&size=5
    @GetMapping("/obtenerDatosPaginados")
    public ResponseEntity<?> obtenerDatosPaginados(
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

        Page<DTORecoleccion> recolecciones = service.obtenerRecolecciones(page, size);
        if (recolecciones == null || recolecciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay registros de recolección"
            ));
        }
        return ResponseEntity.ok(recolecciones);
    }

    // MÉTODO POST
    // RUTA: localhost:8080/apiRecoleccion/agregarRecoleccion
    @PostMapping("/agregarRecoleccion")
    public ResponseEntity<?> agregarRecoleccion(@Valid @RequestBody DTORecoleccion dto) {
        try {
            DTORecoleccion respuesta = service.agregarRecoleccion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Recolección creada correctamente"
            ));
        } catch (ExceptionRecoleccionNoRegistrada e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error al guardar",
                    "message", e.getMessage()
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiRecoleccion/actualizarRecoleccion/{id}
    @PutMapping("/actualizarRecoleccion/{id}")
    public ResponseEntity<?> actualizarRecoleccion(@PathVariable Long id, @Valid @RequestBody DTORecoleccion dto) {
        try {
            DTORecoleccion respuesta = service.actualizarRecoleccion(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Recolección actualizada correctamente"
            ));
        } catch (ExceptionRecoleccionNoEncontrado noEncontrado){
            log.error("Error al actualizar recoleccion", noEncontrado);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", noEncontrado.getMessage()
            ));
        }
        catch (ExceptionRecoleccionNoRegistrada e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error al actualizar",
                    "message", e.getMessage()
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiRecoleccion/actualizarParcialmenteRecoleccion/{id}
    @PatchMapping("/actualizarParcialmenteRecoleccion/{id}")
    public ResponseEntity<?> actualizarParcialmenteRecoleccion(@PathVariable Long id, @Valid @RequestBody DTORecoleccion dto) {
        try {
            DTORecoleccion respuesta = service.patchRecoleccion(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Recolección actualizada parcialmente correctamente"
            ));
        } catch (ExceptionRecoleccionNoRegistrada e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error al actualizar parcialmente",
                    "message", e.getMessage()
            ));
        } catch (ExceptionRecoleccionNoEncontrado e){
            log.error("Error al actualizar parcialmente", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: localhost:8080/apiRecoleccion/eliminarRecoleccion/{id}
    @DeleteMapping("/eliminarRecoleccion/{id}")
    public ResponseEntity<?> eliminarRecoleccion(@PathVariable Long id) {
        try {
            String mensaje = service.eliminarRecoleccion(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", mensaje
            ));
        } catch (ExceptionRecoleccionRelacionada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar la recolección", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar recolección"
            ));
        }
    }

}
