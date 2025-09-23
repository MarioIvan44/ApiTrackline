package apiTrackline.proyectoPTC.Controllers.AduanaController;

import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaRelacionada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionTipoServicioNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOAduana;
import apiTrackline.proyectoPTC.Services.AduanaService;
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
@RequestMapping("/apiAduana")
@CrossOrigin
public class Aduana {

    @Autowired
    private AduanaService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiAduana/buscarAduanaPorId/{id}
    @GetMapping("/obtenerAduanaPorId/{id}")
    public ResponseEntity<?> obtenerAduanaPorId(@PathVariable Long id) {
        try {
            DTOAduana aduana = service.buscarAduanaPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", aduana
            ));
        } catch (ExceptionAduanaNoEncontrada e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener aduana por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar aduana por ID"
            ));
        }
    }

    //MÉTODO GET
    //Consultar todos los datos e implementación de Pageable
    //RUTA: localhost:8080/apiAduana/datosAduana
    @GetMapping("/datosAduana")
    public ResponseEntity<?> getAduanas(
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

        Page<DTOAduana> aduanas = service.obtenerAduanas(page, size);
        if (aduanas == null || aduanas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay aduanas registradas"
            ));
        }
        return ResponseEntity.ok(aduanas);
    }

    //MÉTODO POST
    //RUTA: localhost:8080/apiAduana/agregarAduana
    @PostMapping("/agregarAduana")
    //Se pasan los valores de DTO al método del service
    public ResponseEntity<?> agregarAduana(@Validated(DTOAduana.OnCreate.class) @RequestBody DTOAduana json){
        try {
            DTOAduana respuesta = service.agregarAduana(json);
            if (respuesta == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verfique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Aduana creada correctamente"
            ));
        } //Cuando se ingresen los datos, no se encuentra el id del tipo de servicio (llave foránea) se captura la exception
        catch (ExceptionTipoServicioNoEncontrado noEncontrado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", noEncontrado.getMessage()
            ));
        }
        //"Red de seguridad" para que capture todos los errores no previstos ya sea lógicos o de BD
        catch (Exception e){
            log.error("Error inesperado al agregar aduana", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar aduana"
            ));
        }
    }

    //MÉTODO PUT
    //RUTA: localhost:8080/apiAduana/actualizarAduana/{id}
    @PutMapping("/actualizarAduana/{id}")
    public ResponseEntity<?> actualizarAduana(@PathVariable Long id, @Validated(DTOAduana.OnUpdate.class) @RequestBody DTOAduana dtoAduana){
        try {
            //Intentar actualizar la aduana llamando al método del service
            DTOAduana aduana = service.actualizarAduana(id, dtoAduana);
            return ResponseEntity.ok(aduana);
        }
        catch (ExceptionAduanaNoEncontrada e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
        //Cuando se ingresen los datos, no se encuentra el id del tipo de servicio (llave foránea) se captura la exception
        catch (ExceptionTipoServicioNoEncontrado noEncontrado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", noEncontrado.getMessage()
            ));
        }
        catch (Exception e){
            log.error("Error inesperado al actualizar aduana", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar la aduana"
            ));
        }
    }

    //MÉTODO PATCH
    //RUTA: Localhost:8080/apiAduana/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteAduana(@PathVariable Long id, @Validated(DTOAduana.OnPatch.class) @RequestBody DTOAduana json){
        try {
            DTOAduana actualizada = service.patchAduana(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizada
            ));
        } catch (ExceptionAduanaNoEncontrada e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTipoServicioNoEncontrado exeption){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error: LLave foránea no encontrada",
                    "message", exeption.getMessage()
            ));
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "Error de validación",
                "message", e.getMessage()
            ));
        }
        catch (Exception exceptionCodigo){
            log.error("Error inesperado al actualizar parcialmente la aduana", exceptionCodigo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al editar parcialmente"
            ));
        }
    }

    //MÉTODO DELETE
    //RUTA: localhost:8080/apiAduana/eliminarAduana/{id}
    @DeleteMapping("/eliminarAduana/{id}")
    public ResponseEntity<?> eliminarAduana(@PathVariable Long id){
       try {
           service.eliminarAduana(id);
           return ResponseEntity.ok(Map.of(
                   "status", "Éxito",
                   "message", "La aduana ha sido eliminada correctamente"
           ));
       } catch (ExceptionAduanaNoEncontrada e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                   "status:", "Error",
                   "message", e.getMessage()
           ));
       } catch (ExceptionAduanaRelacionada e){
           log.error("Error al eliminar", e);
           return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                   "status", "Error",
                   "message", "La aduana no se pudo eliminar porque tiene registros relacionados"
           ));
       } catch (Exception e){
           log.error("Error inesperado al eliminar la aduana", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                   "status", "Error no controlado",
                   "message", "Error no controlado al eliminar"
           ));
       }
    }
}
