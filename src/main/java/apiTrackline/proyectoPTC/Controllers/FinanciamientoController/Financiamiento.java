package apiTrackline.proyectoPTC.Controllers.FinanciamientoController;

import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOFinanciamiento;
import apiTrackline.proyectoPTC.Services.FinanciamientoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/apiFinanciamiento")
public class Financiamiento {

    @Autowired
    private FinanciamientoService service;

    // MÉTODO GET (sin paginación)
    @GetMapping("/get")
    public ResponseEntity<?> getFinanciamiento() {
        try {
            List<DTOFinanciamiento> financiamiento = service.getSinPaginacion();
            return ResponseEntity.ok(financiamiento);
        } catch (Exception e) {
            log.error("Error al obtener los financiamientos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener los financiamientos"
            ));
        }
    }


    // MÉTODO GET (con paginación)
    @GetMapping("/obtenerDatos")
    public ResponseEntity<?> obtenerFinanciamientos(
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

        Page<DTOFinanciamiento> pageResult = service.obtenerFinanciamiento(page, size);
        if (pageResult == null || pageResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay financiamientos registrados"
            ));
        }
        return ResponseEntity.ok(pageResult);
    }

    // MÉTODO POST
    @PostMapping("/agregarFinanciamiento")
    public ResponseEntity<?> agregarFinanciamiento(@Validated(DTOFinanciamiento.OnCreate.class) @RequestBody DTOFinanciamiento dto) {
        try {
            DTOFinanciamiento respuesta = service.agregarFinanciamineto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Financiamiento creado correctamente"
            ));
        } catch (ExceptionTipoFinanciamientoNoEncontrado | ExceptionFinanciamientoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionFinanciamientoNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al registrar el financiamiento"
            ));
        }
    }

    // MÉTODO PUT
    @PutMapping("/actualizarFinanciamiento/{id}")
    public ResponseEntity<?> actualizarFinanciamiento(
            @PathVariable Long id,
            @Validated(DTOFinanciamiento.OnUpdate.class) @RequestBody DTOFinanciamiento dto
    ) {
        try {
            DTOFinanciamiento actualizado = service.actualizarFinanciamiento(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionFinanciamientoNoEncontrado | ExceptionTipoFinanciamientoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar financiamiento"
            ));
        }
    }

    // MÉTODO PATCH
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> patchFinanciamiento(
            @PathVariable Long id,
            @Validated(DTOFinanciamiento.OnPatch.class) @RequestBody DTOFinanciamiento dto
    ) {
        try {
            DTOFinanciamiento actualizado = service.patchFinanciamiento(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionFinanciamientoNoEncontrado | ExceptionTipoFinanciamientoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al hacer patch de financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar parcialmente el financiamiento"
            ));
        }
    }

    // MÉTODO DELETE
    @DeleteMapping("/eliminarFinanciamiento/{id}")
    public ResponseEntity<?> eliminarFinanciamiento(@PathVariable Long id) {
        try {
            service.eliminarFinanciamiento(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El financiamiento ha sido eliminado correctamente"
            ));
        } catch (ExceptionFinanciamientoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionFinanciamientoRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El financiamiento no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar financiamiento"
            ));
        }
    }
}


