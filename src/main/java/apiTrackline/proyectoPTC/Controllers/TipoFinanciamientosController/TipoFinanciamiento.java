package apiTrackline.proyectoPTC.Controllers.TipoFinanciamientosController;

import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoDuplicado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoFinanciamientos;
import apiTrackline.proyectoPTC.Services.TipoFinanciamientosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/apiTipoF")
@CrossOrigin
public class TipoFinanciamiento {

    @Autowired
    private TipoFinanciamientosService service;

    // GET - Obtener todos con paginación
    // Ruta: GET localhost:8080/apiTipoF/obtenerTF?page=0&size=5
    @GetMapping("/obtenerTF")
    public ResponseEntity<?> obtenerTiposFinanciamiento(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<DTOTipoFinanciamientos> lista = service.obtenerTiposFinanciamiento(page, size);
            if (lista.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "Error",
                        "message", "No hay registros de TipoFinanciamiento"
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", lista
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener tipos de financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al obtener los registros"
            ));
        }
    }

    // GET - Buscar por ID
    @GetMapping("buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            DTOTipoFinanciamientos dto = service.buscarPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", dto
            ));
        } catch (Exception e) {
            log.error("Error al buscar TipoFinanciamiento con id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // POST - Crear nuevo
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody DTOTipoFinanciamientos dto) {
        try {
            DTOTipoFinanciamientos creado = service.agregarTipoFinanciamiento(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", creado
            ));
        }  catch (ExceptionTipoFinanciamientoDuplicado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error, datos duplicados",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error al crear el tipo de financiamento", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // PUT - Actualizar completo
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,@Valid @RequestBody DTOTipoFinanciamientos dto) {
        try {
            DTOTipoFinanciamientos actualizado = service.actualizarTipoFinanciamiento(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionTipoFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTipoFinanciamientoDuplicado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error, datos duplicados",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error al actualizar el tipo de financiamiento", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // PATCH - Actualización parcial
    @PatchMapping("/patch/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id,@Valid @RequestBody DTOTipoFinanciamientos dto) {
        try {
            DTOTipoFinanciamientos actualizado = service.patchTipoFinanciamiento(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        }  catch (ExceptionTipoFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTipoFinanciamientoDuplicado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error, datos duplicados",
                    "message", e.getMessage()
            ));
        }
        catch (Exception e) {
            log.error("Error en patch", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // DELETE - Eliminar por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            String mensaje = service.eliminarTipoFinanciamiento(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", mensaje
            ));
        } catch (ExceptionTipoFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
        catch (ExceptionTipoFinanciamientoRelacionado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error al eliminar",
                    "message", e.getMessage()
            ));
        }catch (Exception e) {
            log.error("Error al eliminar el tipo financiamiento", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }
}
