package apiTrackline.proyectoPTC.Controllers.EstadosController;

import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.*;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionSelectivoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOEstados;
import apiTrackline.proyectoPTC.Services.EstadosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/apiEstados")
@CrossOrigin
public class Estados {

    @Autowired
    private EstadosService service;

    // GET POR ID
    @GetMapping("/obtenerEstadoPorId/{id}")
    public ResponseEntity<?> obtenerEstadoPorId(@PathVariable Long id) {
        try {
            DTOEstados estado = service.buscarEstadoPorId(id);
            return ResponseEntity.ok(Map.of("status", "Éxito", "data", estado));
        } catch (ExceptionEstadoNoEncontrado | ExceptionOrdenServicioNoEncontrado | ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al obtener estado por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "Error no controlado", "message", "Error inesperado"));
        }
    }

    // GET PAGINADO
    @GetMapping("/datosEstados")
    public ResponseEntity<?> getEstados(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        if (page < 0) return ResponseEntity.badRequest().body(Map.of("status", "Error", "message", "Página inválida"));
        if (size <= 0 || size > 50) return ResponseEntity.badRequest().body(Map.of("status", "Error", "message", "El tamaño debe estar entre 1 y 50"));

        Page<DTOEstados> estados = service.obtenerEstados(page, size);
        if (estados == null || estados.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", "No hay estados registrados"));

        return ResponseEntity.ok(estados);
    }

    // POST
    @PostMapping("/agregarEstado")
    public ResponseEntity<?> agregarEstado(@Validated(DTOEstados.OnCreate.class) @RequestBody DTOEstados json) {
        try {
            DTOEstados respuesta = service.agregarEstado(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "Éxito", "data", respuesta, "message", "Estado creado correctamente"));
        } catch (ExceptionEstadoNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al agregar estado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "Error", "message", "Error no controlado"));
        }
    }

    // PUT
    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @Validated(DTOEstados.OnUpdate.class) @RequestBody DTOEstados dto) {
        try {
            DTOEstados estado = service.actualizarEstado(id, dto);
            return ResponseEntity.ok(estado);
        } catch (ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar estado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "Error", "message", "Error no controlado"));
        }
    }

    // PATCH
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> patchEstado(@PathVariable Long id, @Validated(DTOEstados.OnPatch.class) @RequestBody DTOEstados json) {
        try {
            DTOEstados actualizado = service.patchEstado(id, json);
            return ResponseEntity.ok(Map.of("status", "Éxito", "data", actualizado));
        } catch (ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionSelectivoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente estado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "Error", "message", "Error no controlado"));
        }
    }

    // DELETE
    @DeleteMapping("/eliminarEstado/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id) {
        try {
            service.eliminarEstado(id);
            return ResponseEntity.ok(Map.of("status", "Éxito", "message", "El estado ha sido eliminado correctamente"));
        } catch (ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionEstadoRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", "Error", "message", "El estado no se pudo eliminar porque tiene registros relacionados"));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar estado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "Error", "message", "Error no controlado"));
        }
    }
}
