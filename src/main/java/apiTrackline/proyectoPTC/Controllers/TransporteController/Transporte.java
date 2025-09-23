package apiTrackline.proyectoPTC.Controllers.TransporteController;

import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteRelacionado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransportistaNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTransporte;
import apiTrackline.proyectoPTC.Services.TransporteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apiTransporte")
@Slf4j
public class Transporte {

    @Autowired
    private TransporteService service;

    @GetMapping("/obtenerTransportePorId/{id}")
    public ResponseEntity<DTOTransporte> obtenerTransportePorId(@PathVariable Long id) {
        DTOTransporte transporte = service.buscarTransportePorId(id);
        return ResponseEntity.ok(transporte);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAduanas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
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

            Page<DTOTransporte> transportes = service.obtenerTransportes(page, size);
            if (transportes == null || transportes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "Error",
                        "message", "No hay transportes registrados"
                ));
            }
            return ResponseEntity.ok(transportes);

        } catch (Exception e) {
            log.error("Error inesperado al obtener transportes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Ocurrió un error inesperado"
            ));
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@Validated(DTOTransporte.OnCreate.class) @RequestBody DTOTransporte dto) {
        try {
            DTOTransporte respuesta = service.agregarTransporte(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Transporte creado correctamente"
            ));
        } catch (ExceptionServicioTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionTransportistaNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al registrar transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar transporte"
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Validated(DTOTransporte.OnUpdate.class) @RequestBody DTOTransporte dto) {
        try {
            DTOTransporte transporte = service.actualizarTransporte(id, dto);
            return ResponseEntity.ok(Map.of("status", "Éxito", "data", transporte));
        } catch (ExceptionTransporteNoEncontrado e) {
            log.error("Transporte no encontrado al actualizar", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        }  catch (ExceptionTransportistaNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        }  catch (ExceptionServicioTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al actualizar transporte"
            ));
        }
    }

    @PatchMapping("/actualizarParcial/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id, @Validated(DTOTransporte.OnPatch.class) @RequestBody DTOTransporte dto) {
        try {
            DTOTransporte actualizado = service.patchTransporte(id, dto);
            return ResponseEntity.ok(Map.of("status", "Éxito", "data", actualizado));
        } catch (ExceptionTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionServicioTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error: Llave foránea no encontrada", "message", e.getMessage()));
        } catch (ExceptionTransportistaNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error: Llave foránea no encontrada", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al editar parcialmente transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al editar parcialmente"
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminarTransporte(id);
            return ResponseEntity.ok(Map.of("status", "Éxito", "message", "El transporte ha sido eliminado correctamente"));
        } catch (ExceptionTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (ExceptionTransporteRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", "Error", "message", "El transporte no se pudo eliminar porque tiene registros relacionados"));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error inesperado",
                    "message", "Error inesperado al eliminar transporte"
            ));
        }
    }
}
