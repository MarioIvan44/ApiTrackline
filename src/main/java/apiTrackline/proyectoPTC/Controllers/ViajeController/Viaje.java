package apiTrackline.proyectoPTC.Controllers.ViajeController;

import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeRelacionada;

import apiTrackline.proyectoPTC.Models.DTO.DTOViaje;
import apiTrackline.proyectoPTC.Services.ViajeService;
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
@RequestMapping("/apiViaje")
@CrossOrigin
@Slf4j
public class Viaje {

    @Autowired
    private ViajeService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiViaje/buscarPorId/{id}
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarViajePorId(@PathVariable Long id) {
        try {
            DTOViaje viaje = service.buscarPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", viaje
            ));
        } catch (ExceptionViajeNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener viaje por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error inesperado al buscar viaje por ID"
            ));
        }
    }

    //METODO GET CON PAGINACIÓN
    @GetMapping("/datosViaje")
    public ResponseEntity<?> getViajes(
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

        Page<DTOViaje> viajes = service.obtenerViajes(page, size);
        if (viajes == null || viajes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay viajes registrados"
            ));
        }
        return ResponseEntity.ok(viajes);
    }

    // MÉTODO GET (SIN PAGINACIÓN) - Obtener todos sin paginación
    // RUTA: localhost:8080/apiViaje/obtener
    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerViajes() {
        List<DTOViaje> viajes = service.getAll();
        if (viajes == null || viajes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay viajes registrados"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "Éxito",
                "data", viajes
        ));
    }

    // MÉTODO POST
    // RUTA: localhost:8080/apiViaje/crear
    @PostMapping("/crear")
    public ResponseEntity<?> crearViaje(@Validated(DTOViaje.OnCreate.class) @RequestBody DTOViaje dto) {
        try {
            DTOViaje creado = service.create(dto, dto.getIdOrdenServicio(), dto.getIdTransporteViaje(), dto.getIdEstado());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", creado,
                    "message", "Viaje creado correctamente"
            ));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionTransporteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionViajeNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al crear viaje", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar viaje"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiViaje/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarViaje(@PathVariable Long id,
                                             @Validated(DTOViaje.OnUpdate.class) @RequestBody DTOViaje dto) {
        try {
            DTOViaje actualizado = service.putUpdate(id, dto, dto.getIdOrdenServicio(), dto.getIdTransporteViaje(), dto.getIdEstado());
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado,
                    "message", "Viaje actualizado correctamente"
            ));
        } catch (ExceptionViajeNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionTransporteNoEncontrado llaveForaneaNoEncontrada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error llave foránea no encontrada",
                    "message", llaveForaneaNoEncontrada.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar viaje", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al actualizar viaje"
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiViaje/actualizarParcial/{id}
    @PatchMapping("/actualizarParcial/{id}")
    public ResponseEntity<?> patchViaje(@PathVariable Long id,
                                        @Validated(DTOViaje.OnPatch.class) @RequestBody DTOViaje dto) {
        try {
            DTOViaje actualizado = service.patch(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado,
                    "message", "Viaje actualizado parcialmente"
            ));
        } catch (ExceptionViajeNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionTransporteNoEncontrado llaveForaneaNoEncontrada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error llave foránea no encontrada",
                    "message", llaveForaneaNoEncontrada.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al hacer patch en viaje", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al actualizar parcialmente el viaje"
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: localhost:8080/apiViaje/eliminar/{id}
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarViaje(@PathVariable Long id) {
        try {
            String mensaje = service.delete(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", mensaje
            ));
        } catch (ExceptionViajeNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionViajeRelacionada e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar viaje", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al eliminar viaje"
            ));
        }
    }
}
