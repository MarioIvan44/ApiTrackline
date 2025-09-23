package apiTrackline.proyectoPTC.Controllers.TipoDatoContableController;

import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoDatoContable;
import apiTrackline.proyectoPTC.Services.TipoDatoContableService;
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
@RequestMapping("/apiTipoDatoContable")
@CrossOrigin
public class TipoDatoContable {

    @Autowired
    private TipoDatoContableService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiTipoDatoContable/buscarDatoContablePorID/{id}
    @GetMapping("/obtenerDatoContablePorId/{id}")
    public ResponseEntity<?> obtenerDatos(@PathVariable Long id) {
        try {
            DTOTipoDatoContable tipoDatoContable = service.buscarTipoContablePorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", tipoDatoContable
            ));
        } catch (ExceptionTipoDatoContableNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener dato contable por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar aduana por ID"
            ));
        }
    }

    //MÉTODO GET
    //Consultar todos los datos e implementación de Pageable
    //RUTA: localhost:8080/apiTipoDatoContable/datosContable
    @GetMapping("/datosContables")
    public ResponseEntity<?> getDatosContables(
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

        Page<DTOTipoDatoContable> tipoDatoContables = service.obtenerTiposContables(page, size);
        if (tipoDatoContables == null || tipoDatoContables.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay aduanas registradas"
            ));
        }
        return ResponseEntity.ok(tipoDatoContables);
    }


    // POST - Crear nuevo
    @PostMapping("/agregarTipoDatoContable")
    public ResponseEntity<?> create(@Validated(DTOTipoDatoContable.OnCreate.class) @RequestBody DTOTipoDatoContable dto) {
        try {
            DTOTipoDatoContable resultado = service.agregarTipoContable(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", resultado
            ));
        } catch (ExceptionTipoDatoContableNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al crear TipoDatoContable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al crear el registro"
            ));
        }
    }

    // PUT - Actualizar completo
    @PutMapping("/actualizarDatoContable/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Validated(DTOTipoDatoContable.OnUpdate.class) @RequestBody DTOTipoDatoContable dto) {
        try {
            DTOTipoDatoContable resultado = service.actualizarTipoContable(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", resultado
            ));
        } catch (ExceptionTipoDatoContableNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el dato contable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al actualizar el registro"
            ));
        }
    }

    // PATCH - Actualización parcial
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id,
                                   @Validated(DTOTipoDatoContable.OnPatch.class) @RequestBody DTOTipoDatoContable dto) {
        try {
            DTOTipoDatoContable resultado = service.patchTipoContable(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", resultado
            ));
        } catch (ExceptionTipoDatoContableNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al hacer patch en TipoDatoContable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al editar parcialmente"
            ));
        }
    }

    // DELETE - Eliminar
    @DeleteMapping("eliminarDatoContable/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            String resultado = service.eliminarTipoContable(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", resultado
            ));
        } catch (ExceptionTipoDatoContableNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTipoDatoContableRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar TipoDatoContable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al eliminar el registro"
            ));
        }
    }
}