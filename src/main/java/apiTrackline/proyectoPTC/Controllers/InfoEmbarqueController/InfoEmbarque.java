package apiTrackline.proyectoPTC.Controllers.InfoEmbarqueController;

import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOInfoEmbarque;
import apiTrackline.proyectoPTC.Services.InfoEmbarqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiInfoEmbarque")
@CrossOrigin
public class InfoEmbarque {

    @Autowired //Inyectamos la clase InfoEmbarqueService
    private InfoEmbarqueService service;

    @GetMapping("/datosInfoEmbarque")
    public ResponseEntity<?> getInfoEmbarques(
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

        Page<DTOInfoEmbarque> pageResult = service.obtenerInfoEmbarques(page, size);
        if (pageResult == null || pageResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay información de embarque registrada"
            ));
        }
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("/agregarInfoEmbarque")
    public ResponseEntity<?> agregarInfoEmbarque(@Valid @RequestBody DTOInfoEmbarque json) {
        try {
            DTOInfoEmbarque respuesta = service.agregarInfoEmbarque(json);
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
                    "message", "Información de embarque creada correctamente"
            ));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al agregar la información del embarque",
                    "description", e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizarInfoEmbarque/{id}")
    public ResponseEntity<?> actualizarInfoEmbarque(@PathVariable Long id, @Valid @RequestBody DTOInfoEmbarque dto) {
        try {
            DTOInfoEmbarque actualizado = service.actualizarInfoEmbarque(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (ExceptionInfoEmbarqueNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }  catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar la informacíon del embarque",
                    "description", e.getMessage()
            ));
        }
    }
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> patchInfoEmbarque(@PathVariable Long id, @Valid @RequestBody DTOInfoEmbarque json) {
        try {
            DTOInfoEmbarque actualizado = service.patchInfoEmbarque(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionInfoEmbarqueNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar parcialmente la informacíon del embarque",
                    "description", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarInfoEmbarque/{id}")
    public ResponseEntity<?> eliminarInfoEmbarque(@PathVariable Long id) {
        try {
            service.eliminarInfoEmbarque(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "La información de embarque ha sido eliminada correctamente"
            ));
        } catch (ExceptionInfoEmbarqueNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionInfoEmbarqueRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "La información de embarque no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar",
                    "description", e.getMessage()
            ));
        }
    }
}
