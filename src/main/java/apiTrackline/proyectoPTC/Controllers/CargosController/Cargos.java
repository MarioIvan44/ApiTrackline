package apiTrackline.proyectoPTC.Controllers.CargosController;

import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOCargos;
import apiTrackline.proyectoPTC.Services.CargosService;
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

@RestController
@RequestMapping("/apiCargos")
@CrossOrigin
@Slf4j
public class Cargos {

    @Autowired
    private CargosService service;

    // MÉTODO GET POR ID
    @GetMapping("/obtenerCargoPorId/{id}")
    public ResponseEntity<?> obtenerCargoPorId(@PathVariable Long id) {
        try {
            DTOCargos cargo = service.buscarCargoPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", cargo
            ));
        } catch (ExceptionCargoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al buscar el cargo por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar cargo"
            ));
        }
    }

    // MÉTODO GET (con paginación)
    @GetMapping("/obtenerDatos")
    public ResponseEntity<?> getCargos(
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

        Page<DTOCargos> cargos = service.obtenerCargos(page, size);
        if (cargos == null || cargos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay cargos registrados"
            ));
        }
        return ResponseEntity.ok(cargos);
    }

    // MÉTODO POST
    @PostMapping("/agregarCargo")
    public ResponseEntity<?> agregarCargo(@Validated(DTOCargos.OnCreate.class) @RequestBody DTOCargos dto) {
        try {
            DTOCargos respuesta = service.agregarCargo(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Cargo creado correctamente"
            ));
        } catch (ExceptionTipoDatoContableNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionCargoNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar el cargo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al registrar el cargo"
            ));
        }
    }

    // MÉTODO PUT
    @PutMapping("/actualizarCargo/{id}")
    public ResponseEntity<?> actualizarCargo(@PathVariable Long id, @Validated(DTOCargos.OnUpdate.class) @RequestBody DTOCargos dto) {
        try {
            DTOCargos actualizado = service.actualizarCargo(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionCargoNoEncontrado | ExceptionTipoDatoContableNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el cargo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar cargo"
            ));
        }
    }

    // MÉTODO PATCH
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteCargo(@PathVariable Long id, @Validated(DTOCargos.OnPatch.class) @RequestBody DTOCargos dto) {
        try {
            DTOCargos actualizado = service.patchCargo(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionCargoNoEncontrado | ExceptionTipoDatoContableNoEncontrado | ExceptionOrdenServicioNoEncontrado e) {
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
            log.error("Error inesperado al actualizar parcialmente el cargo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar parcialmente el cargo"
            ));
        }
    }

    // MÉTODO DELETE
    @DeleteMapping("/eliminarCargo/{id}")
    public ResponseEntity<?> eliminarCargo(@PathVariable Long id) {
        try {
            service.eliminarCargo(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El cargo ha sido eliminado correctamente"
            ));
        } catch (ExceptionCargoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionCargoRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El cargo no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar el cargo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar"
            ));
        }
    }
}
