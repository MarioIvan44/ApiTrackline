package apiTrackline.proyectoPTC.Controllers.ServicioTransporteController;

import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteNoRegistrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOServicioTransporte;
import apiTrackline.proyectoPTC.Services.ServicioTransporteService;
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
@RequestMapping("/apiServicioTransporte")
@CrossOrigin
public class ServicioTransporte {

    @Autowired
    private ServicioTransporteService service;

    // MÉTODO GET TODOS
    // RUTA: GET localhost:8080/apiServicioTransporte/data
    @GetMapping("/data")
    public ResponseEntity<?> obtenerServicios(
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

        try {
            Page<DTOServicioTransporte> servicios = service.obtenerServiciosTransporte(page, size);
            if (servicios == null || servicios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "Error",
                        "message", "No hay servicios de transporte registrados"
                ));
            }
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al obtener servicios de transporte",
                    "description", e.getMessage()
            ));
        }
    }

    // MÉTODO POST
    // RUTA: POST localhost:8080/apiServicioTransporte/postST
    @PostMapping("/postST")
    public ResponseEntity<?> agregarServicio(@Validated(DTOServicioTransporte.OnCreate.class) @RequestBody DTOServicioTransporte dto) {
        try {
            DTOServicioTransporte nuevo = service.agregarServicioTransporte(dto);
            if (nuevo == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verifique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", nuevo,
                    "message", "Servicio de transporte creado correctamente"
            ));
        }catch (ExceptionServicioTransporteNoRegistrado e) {
            log.error("Error inesperado al agregar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
        catch (Exception e) {
            log.error("Error inesperado al agregar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar servicio de transporte"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: PUT localhost:8080/apiServicioTransporte/updateTS/{id}
    @PutMapping("/updateTS/{id}")
    public ResponseEntity<?> actualizarServicio(@PathVariable Long id, @Validated(DTOServicioTransporte.OnUpdate.class) @RequestBody DTOServicioTransporte dto) {
        try {
            DTOServicioTransporte actualizado = service.actualizarServicioTransporte(id, dto);
            if (actualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "Error",
                        "message", "No se encontró el servicio de transporte con el ID especificado"
                ));
            }
            return ResponseEntity.ok(actualizado);
        } catch (ExceptionServicioTransporteNoRegistrado e) {
            log.error("Error inesperado al actualizar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al actualizar servicio de transporte"
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: PATCH localhost:8080/apiServicioTransporte/patchST/{id}
    @PatchMapping("/patchST/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @Validated(DTOServicioTransporte.OnPatch.class) @RequestBody DTOServicioTransporte dto) {
        try {
            DTOServicioTransporte actualizado = service.patchServicioTransporte(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        }catch (ExceptionServicioTransporteNoRegistrado e) {
            log.error("Error inesperado al agregar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar servicio transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al editar parcialmente servicio de transporte"
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: DELETE localhost:8080/apiServicioTransporte/deleteST/{id}
    @DeleteMapping("/deleteST/{id}")
    public ResponseEntity<?> eliminarServicio(@PathVariable Long id) {
        try {
            service.eliminarServicioTransporte(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El servicio de transporte ha sido eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al eliminar servicio de transporte",
                    "description", e.getMessage()
            ));
        }
    }
}